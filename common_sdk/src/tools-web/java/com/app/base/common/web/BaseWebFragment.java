package com.app.base.common.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.base.app.fragment.BaseUIFragment;
import com.android.base.app.fragment.tools.Fragments;
import com.android.base.utils.android.WebViewUtils;
import com.app.base.R;
import com.app.base.databinding.AppBaseWebFragmentBinding;
import com.app.base.widget.AppTitleLayout;
import com.blankj.utilcode.util.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-09-04 17:02
 */
public class BaseWebFragment extends BaseUIFragment {

    private WebView mWebView;
    private AppTitleLayout mTitleLayout;
    private View mErrorLayout;
    private FrameLayout mCustomLayout;
    private WebProgress mWebProgress;

    private JsBridgeHandler mJsBridgeHandler;

    private AppBaseWebFragmentBinding mLayout;

    private static final String TITLE_IS_HIDDEN_KEY = "title_is_hidden_key";

    private boolean mTitleIsHidden = false;
    private String customTitle = null;

    private String mCurrentUrl;

    private DefaultWebSetting mDefaultWebSetting;

    @Override
    public void onAttach(@NotNull Context context) {
        mJsBridgeHandler = new JsBridgeHandler(this);
        initArguments();
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTitleIsHidden = savedInstanceState.getBoolean(TITLE_IS_HIDDEN_KEY, false);
        } else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mTitleIsHidden = !arguments.getBoolean(BrowserPath.SHOW_HEADER_KEY, false);
            }
        }
    }

    @Override
    protected void onViewPrepared(@NotNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mLayout = AppBaseWebFragmentBinding.bind(view);
        setupViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String url = arguments.getString(BrowserPath.URL_KEY);
            if (!TextUtils.isEmpty(url)) {
                startLoad(url);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.resumeTimers();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebViewUtils.destroy(mWebView);
    }

    private void initArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            customTitle = arguments.getString(BrowserPath.PAGE_TITLE, "");
            initCustomJsCallInterceptorIfNeed(arguments);
        }
    }

    private void initCustomJsCallInterceptorIfNeed(Bundle arguments) {
        String className = arguments.getString(BrowserPath.JS_CALL_INTERCEPTOR_CLASS_KEY, "");
        Timber.d("initCustomJsCallInterceptorIfNeed = %s", className);
        if (TextUtils.isEmpty(className)) {
            return;
        }
        try {
            Object newInstance = Class.forName(className).newInstance();
            if (newInstance instanceof JsCallInterceptor) {
                JsCallInterceptor jsCallInterceptor = (JsCallInterceptor) newInstance;
                mJsBridgeHandler.setJsCallInterceptor(jsCallInterceptor);
                if (newInstance instanceof BaseCustomJsCallInterceptor) {
                    ((BaseCustomJsCallInterceptor) newInstance).onInit(this, arguments.getBundle(BrowserPath.ARGUMENTS_KEY));
                }
            }
        } catch (Exception error) {
            Timber.e(error, "initCustomJsCallInterceptorIfNeed error");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(BrowserPath.SHOW_HEADER_KEY, mTitleIsHidden);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected boolean handleBackPress() {
        boolean canGoBack = mWebView.canGoBack();
        Timber.d("canGoBack = %b", canGoBack);
        if (isVisible() && canGoBack) {
            mWebView.goBack();
            Timber.d("mWebView goBack");
            return true;
        }
        return false;
    }

    private void setupViews() {
        /*Find view*/
        mWebView = mLayout.webView;
        mWebProgress = new WebProgress(mLayout.webPb);
        mErrorLayout = mLayout.layoutError.getRoot();
        mTitleLayout = mLayout.atlWebRulesTitle;
        mCustomLayout = mLayout.webFlCustom;

        /*Title*/
        mTitleLayout.setOnNavigationOnClickListener(v -> Fragments.exitFragment(this));
        setTitleVisible(!mTitleIsHidden);
        if (!TextUtils.isEmpty(customTitle)) {
            mTitleLayout.setTitle(customTitle);
        }

        /*Error layout*/
        mErrorLayout.setBackgroundColor(Color.WHITE);

        /*WebView*/
        mDefaultWebSetting = new DefaultWebSetting(mWebView);
        mDefaultWebSetting.setupBasic();
        mDefaultWebSetting.setupCache();

        Bundle arguments = getArguments();
        if (arguments != null && arguments.getBoolean(BrowserPath.CACHE_ENABLE, false)) {
            mDefaultWebSetting.setUsingCache(true, true);
        }

        AppWebChromeClient webChromeClient = new AppWebChromeClient(this);
        webChromeClient.setAppWebChromeClientCallback(appWebChromeClientCallback);

        mWebView.setWebViewClient(mAppWebViewClient);
        mWebView.setWebChromeClient(webChromeClient);
    }

    private void setTitleVisible(boolean visible) {
        if (visible) {
            mTitleLayout.setVisibility(View.VISIBLE);
        } else {
            mTitleLayout.setVisibility(View.GONE);
        }
    }

    private final AppWebChromeClient.AppWebChromeClientCallback appWebChromeClientCallback = new AppWebChromeClient.AppWebChromeClientCallback() {
        @Override
        public void onReceivedTitle(@NotNull String title) {
            //设置页面title
            if (!mWebView.getUrl().contains(title))
                processReceivedTitle(title);
        }

        @Override
        public void onProgressChanged(int newProgress) {
            mWebProgress.onProgress(newProgress);
        }
    };

    private final WebViewClient mAppWebViewClient = new AppWebViewClient() {

        @Override
        public boolean appShouldOverrideUrlLoading(@NotNull WebView view, @NotNull String url) {
            return processShouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onAppPageError(@NotNull String url, int code, boolean isMainFrame) {
            onLoadError(url, code, isMainFrame);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onLoadFinished(url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            onLoadStart(url);
        }

    };

    protected void processReceivedTitle(String title) {
        if (TextUtils.isEmpty(customTitle) && autoHandleTitle() && WebUtils.isValidateTitle(title)) {
            mTitleLayout.setTitle(title);
        }
    }

    protected void onLoadFinished(@SuppressWarnings("unused") String url) {
        Timber.d("onLoadFinished() called with: url = [" + url + "]");
        mErrorLayout.setVisibility(View.GONE);
    }

    protected void onLoadStart(String url) {
        Timber.d("onLoadStart() called with: url = [" + url + "]");
    }

    protected void onLoadError(String url, int code, boolean isMainFrame) {
        Timber.d("onLoadError() called with: url = [" + url + "], code = [" + code + "], isMainFrame = [" + isMainFrame + "], mCurrentUrl = [" + mCurrentUrl + "] mCurrentUrl==url = [" + mCurrentUrl.equals(url) + "]");

        mErrorLayout.setVisibility(View.VISIBLE);

        if (!NetworkUtils.isConnected()) {
            mErrorLayout.<ImageView>findViewById(R.id.base_retry_icon).setImageResource(R.drawable.base_img_no_network);
            mErrorLayout.<TextView>findViewById(R.id.base_retry_tv).setText(R.string.error_net_error);
        } else {
            mErrorLayout.<ImageView>findViewById(R.id.base_retry_icon).setImageResource(R.drawable.base_img_error);
            mErrorLayout.<TextView>findViewById(R.id.base_retry_tv).setText(R.string.error_service_error);
        }

        boolean titleIsHidden = mTitleIsHidden;

        setTitleVisible(true);

        mErrorLayout.findViewById(R.id.base_retry_btn).setOnClickListener(view1 -> {
            Timber.d("onLoadError() called retry: url = [" + mCurrentUrl + "]");
            if (titleIsHidden) {
                setTitleVisible(false);
            }
            mWebView.reload();
        });
    }

    public void startLoad(String url) {
        Timber.d("firstLoadUrl() called with: currentUrl = [" + url + "]");
        loadUrl(url);
    }

    public void startLoad(String url, Map<String, String> header) {
        Timber.d("startLoadUrl() called with: url = [" + url + "], header = [" + header + "]");
        mCurrentUrl = url;
        mWebView.loadUrl(mCurrentUrl, header);
    }

    public void startPostLoad(String url, String postData) {
        Timber.d("startLoadUrl() called with: url = [" + url + "], postData = [" + postData + "]");
        mCurrentUrl = url;
        mWebView.postUrl(url, postData.getBytes(StandardCharsets.UTF_8));
    }

    private void loadUrl(String url) {
        mCurrentUrl = url;
        mWebView.loadUrl(mCurrentUrl);
    }

    protected boolean autoHandleTitle() {
        return true;
    }

    //https://juejin.im/post/5a5d8ef2f265da3e393a6b76
    protected boolean processShouldOverrideUrlLoading(WebView webView, String url) {
        if (URLUtil.isNetworkUrl(url)) {
            loadUrl(url);
        } else {
            processAppUrl(url);
        }
        return true;
    }

    protected void processAppUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    boolean handleJsCall(WebView view, String message, JsPromptResult jsPromptResult) {
        return mJsBridgeHandler.handleJsCall(message, jsPromptResult);
    }

    protected final JsBridgeHandler getJsBridgeHandler() {
        return mJsBridgeHandler;
    }

    public final void setHeaderVisible(boolean showHeader) {
        mTitleIsHidden = !showHeader;
        if (mTitleLayout != null) {
            setTitleVisible(showHeader);
        }
    }

    protected final AppTitleLayout getTitleLayout() {
        return mTitleLayout;
    }

    protected final WebView getWebView() {
        return mWebView;
    }

    public final DefaultWebSetting getDefaultWebSetting() {
        return mDefaultWebSetting;
    }

    public FrameLayout getCustomLayout() {
        return mCustomLayout;
    }

    @NonNull
    @Override
    public final Object provideLayout() {
        return R.layout.app_base_web_fragment;
    }

}