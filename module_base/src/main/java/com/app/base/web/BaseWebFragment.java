package com.app.base.web;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.base.app.fragment.BaseDialogFragment;
import com.android.base.utils.android.WebViewUtils;
import com.app.base.R;
import com.app.base.app.ToolbarUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-09-04 17:02
 */
public class BaseWebFragment extends BaseDialogFragment {

    protected View mLayout;

    protected WebView mWebView;
    protected Toolbar mToolbar;

    private View mErrorLayout;
    private WebProgress mWebProgress;

    protected String mCurrentUrl;

    private AppWebChromeClient mWebChromeClient;

    private JsBridgeHandler mJsBridgeHandler;

    @Override
    public void onAttach(Context context) {
        mJsBridgeHandler = new JsBridgeHandler(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogStyle();
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLayout == null) {
            mLayout = inflater.inflate(getWebLayoutRes(), container, false);
            setupViews();
        }
        return mLayout;
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

    ///////////////////////////////////////////////////////////////////////////
    // Back handle
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onBackPressed() {
        if (isVisible() && isInLayout() && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onBackPressed();
    }

    @NonNull
    @Override
    public View getView() {
        View view = super.getView();
        assert view != null;
        return view;
    }

    ///////////////////////////////////////////////////////////////////////////
    // WebView setup
    ///////////////////////////////////////////////////////////////////////////

    protected void setupViews() {
        /*Find view*/
        mWebView = mLayout.findViewById(R.id.web_view);
        mWebProgress = new WebProgress(mLayout.findViewById(R.id.web_pb));
        mErrorLayout = mLayout.findViewById(R.id.layout_error);
        mToolbar = mLayout.findViewById(R.id.common_toolbar);

        /*Title*/
        if (autoHandleTitle()) {
            mToolbar.setVisibility(View.VISIBLE);
            ToolbarUtils.setupToolBar(this, mLayout);
        } else {
            mToolbar.setVisibility(View.GONE);
        }

        /*Error layout*/
        mErrorLayout.setBackgroundColor(Color.WHITE);

        /*WebView*/
        DefaultWebSetting defaultWebSetting = new DefaultWebSetting(mWebView);
        defaultWebSetting.setupBasic();
        defaultWebSetting.setupCache();

        mWebChromeClient = new AppWebChromeClient(this);
        mWebChromeClient.setAppWebChromeClientCallback(appWebChromeClientCallback);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mAppWebViewClient);
    }

    private AppWebChromeClient.AppWebChromeClientCallback appWebChromeClientCallback = new AppWebChromeClient.AppWebChromeClientCallback() {
        @Override
        public void onReceivedTitle(String title) {
            processReceivedTitle(title);
        }

        @Override
        public void onProgressChanged(int newProgress) {
            progressProgress(newProgress);
        }
    };


    private WebViewClient mAppWebViewClient = new AppWebViewClient() {

        @Override
        boolean appShouldOverrideUrlLoading(WebView view, String url) {
            return processShouldOverrideUrlLoading(view, url);
        }

        @Override
        void onAppPageError(String url) {
            onLoadError(url);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWebChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dialog
    ///////////////////////////////////////////////////////////////////////////
    private void setDialogStyle() {
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_Common);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.Style_Anim_Fragment_Scale_In);
        }
        return dialog;
    }

    ///////////////////////////////////////////////////////////////////////////
    // extends
    ///////////////////////////////////////////////////////////////////////////
    protected void onLoadFinished(@SuppressWarnings("unused") String url) {

    }

    protected void progressProgress(int newProgress) {
        mWebProgress.onProgress(newProgress);
    }

    protected void processReceivedTitle(String title) {
        if (autoHandleTitle()) {
            mToolbar.setTitle(title);
        }
    }

    protected void onLoadStart(@SuppressWarnings("unused") String url) {
        mErrorLayout.setVisibility(View.GONE);
    }

    protected void onLoadError(@SuppressWarnings("unused") String url) {
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorLayout.findViewById(R.id.base_retry_btn).setOnClickListener(view1 -> mWebView.loadUrl(mCurrentUrl));
    }

    protected void startLoad(String url) {
        Timber.d("firstLoadUrl() called with: currentUrl = [" + url + "]");
        loadUrl(url);
    }

    protected void startLoad(String url, Map<String, String> header) {
        Timber.d("startLoadUrl() called with: url = [" + url + "], header = [" + header + "]");
        mCurrentUrl = url;
        mWebView.loadUrl(mCurrentUrl, header);
    }

    protected void startPostLoad(String url, String postData) {
        Timber.d("startLoadUrl() called with: url = [" + url + "], postData = [" + postData + "]");
        mCurrentUrl = url;
        try {
            mWebView.postUrl(url, postData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void loadUrl(String url) {
        mCurrentUrl = url;
        mWebView.loadUrl(mCurrentUrl);
    }

    protected boolean autoHandleTitle() {
        return false;
    }

    protected boolean processShouldOverrideUrlLoading(@SuppressWarnings("unused") WebView webView, String url) {
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

    protected int getWebLayoutRes() {
        return R.layout.app_base_fragment_web;
    }

    boolean handleJsCall(@SuppressWarnings("unused") WebView view, String message) {
        return mJsBridgeHandler.handleJsCall(message);
    }

    @SuppressWarnings("unused")
    public final JsBridgeHandler getJsBridgeHandler() {
        return mJsBridgeHandler;
    }

    protected final void refresh() {
        loadUrl(mCurrentUrl);
    }
    
}
