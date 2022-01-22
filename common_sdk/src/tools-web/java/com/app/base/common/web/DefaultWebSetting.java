package com.app.base.common.web;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.base.utils.android.network.NetworkUtils;
import com.android.base.utils.android.compat.AndroidVersion;

import java.nio.charset.StandardCharsets;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-12-20 15:55
 */
public class DefaultWebSetting {

    private final WebView mWebView;

    public DefaultWebSetting(WebView webView) {
        mWebView = webView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setupBasic() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setSavePassword(false);
        webSettings.setTextZoom(100);
        webSettings.setDefaultFontSize(14);
        //设置 WebView 支持的最小字体大小，默认为 8
        webSettings.setMinimumFontSize(8);
        webSettings.setDefaultTextEncodingName(StandardCharsets.UTF_8.name());
        //混合http和https
        if (AndroidVersion.atLeast(21)) {
            webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public void setupCache() {
        WebSettings webSettings = mWebView.getSettings();
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        //开启 database storage API 功能，默认值为false
        webSettings.setDatabaseEnabled(true);
        //开启 DOM storage API 功能，这样 JS 的 localStorage,sessionStorage 对象才可以使用
        webSettings.setDomStorageEnabled(true);
    }

    public void setUsingCache(boolean useCache, boolean checkNetworkStatus) {
        WebSettings webSettings = mWebView.getSettings();
        if (useCache) {
            if (checkNetworkStatus) {
                if ((NetworkUtils.isConnected())) {
                    Timber.d("使用 LOAD_DEFAULT");
                } else {
                    Timber.d("使用 LOAD_CACHE_ELSE_NETWORK");
                }
                webSettings.setCacheMode(NetworkUtils.isConnected() ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK);
            } else {
                Timber.d("使用 LOAD_CACHE_ELSE_NETWORK");
                webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
        } else {
            Timber.d("使用 LOAD_DEFAULT");
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }
    }

}