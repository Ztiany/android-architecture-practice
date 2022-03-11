package com.app.base.common.web

import android.net.http.SslError
import android.os.Build
import android.webkit.*
import androidx.annotation.RequiresApi
import timber.log.Timber

/**
 * @author Ztiany
 * Date : 2017-12-20 15:43
 */
internal abstract class AppWebViewClient : WebViewClient() {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        Timber.d("shouldOverrideUrlLoading() called with: view = $view, request = $request")
        val url = request.url.toString()
        return appShouldOverrideUrlLoading(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        Timber.d("shouldOverrideUrlLoading() called with: view = [$view], url = [$url]")
        return appShouldOverrideUrlLoading(view, url)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
        super.onReceivedError(view, request, error)
        onAppPageError(request.url.toString(), error.errorCode, request.isForMainFrame)
    }

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        Timber.d("onReceivedError() called with: view = [$view], errorCode = [$errorCode], description = [$description], failingUrl = [$failingUrl]")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return
        }
        onAppPageError(failingUrl, errorCode, true)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onReceivedHttpError(view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse) {
        super.onReceivedHttpError(view, request, errorResponse)
        Timber.d("onReceivedHttpError url = %s, isMain = %b, code = %d", request.url.toString(), request.isForMainFrame, errorResponse.statusCode)
        onAppPageError(request.url.toString(), -1, request.isForMainFrame)
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        super.onReceivedSslError(view, handler, error)
        Timber.d("onReceivedSslError, url = %s", view.url)
    }

    abstract fun onAppPageError(url: String, code: Int, isMainFrame: Boolean)

    abstract fun appShouldOverrideUrlLoading(view: WebView, url: String): Boolean

}