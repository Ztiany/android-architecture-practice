package com.app.base.web;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.blankj.utilcode.util.NetworkUtils;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-12-20 15:55
 */
class DefaultWebSetting {

    private final WebView mWebView;

    DefaultWebSetting(WebView webView) {
        mWebView = webView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    void setupBasic() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setTextZoom(100);
        webSettings.setDefaultFontSize(16);
        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
    }

    void setupCache() {
        WebSettings webSettings = mWebView.getSettings();
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);//开启 database storage API 功能，默认值为false
        webSettings.setDomStorageEnabled(true); //开启 DOM storage API 功能，这样 JS 的 localStorage,sessionStorage 对象才可以使用
        if (NetworkUtils.isConnected()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
    }
}
