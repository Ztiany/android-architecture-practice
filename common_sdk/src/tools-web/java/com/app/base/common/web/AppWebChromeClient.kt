package com.app.base.common.web

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.text.TextUtils
import android.webkit.*
import com.android.sdk.mediaselector.common.ResultListener
import com.android.sdk.mediaselector.system.SystemMediaSelector
import com.android.sdk.mediaselector.system.newSystemMediaSelector
import com.android.sdk.permission.AutoPermission
import timber.log.Timber

private const val IMAGE_TYPE = "image"

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-12-20 15:40
 */
internal class AppWebChromeClient(
        private val host: BaseWebFragment
) : WebChromeClient() {

    private var appWebChromeClientCallback: AppWebChromeClientCallback? = null

    private var uriValuesCallback: ValueCallback<Array<Uri>>? = null

    private val systemMediaSelector: SystemMediaSelector

    init {
        systemMediaSelector = newSystemMediaSelector(host, object : ResultListener {
            override fun onTakeSuccess(result: List<Uri>) {
                if (result.isEmpty()) {
                    returnEmptyFile()
                } else {
                    returnFile(result)
                }
            }

            override fun onCancel() = returnEmptyFile()
            override fun onTakeFail() = returnEmptyFile()
        })
    }

    fun setAppWebChromeClientCallback(appWebChromeClientCallback: AppWebChromeClientCallback?) {
        this.appWebChromeClientCallback = appWebChromeClientCallback
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        if (appWebChromeClientCallback != null) {
            appWebChromeClientCallback!!.onProgressChanged(newProgress)
        }
    }

    override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
        Timber.d("onJsAlert() called with: view = [$view], url = [$url], message = [$message], result = [$result]")
        onJsAlert(host, message, result)
        return true
    }

    override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
        Timber.d("onJsConfirm() called with: view = [$view], url = [$url], message = [$message], result = [$result]")
        if (host.handleJsCall(view, message, null)) {
            result.confirm()
        } else {
            onJsConfirm(host, message, result)
        }
        return true
    }

    override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
        Timber.d("onJsPrompt() called with: view = [$view], url = [$url], message = [$message], defaultValue = [$defaultValue], result = [$result]")
        if (host.handleJsCall(view, message, result)) {
            result.confirm()
            return true
        }
        return super.onJsPrompt(view, url, message, defaultValue, result)
    }

    override fun onReceivedTitle(view: WebView, title: String) {
        Timber.d("onReceivedTitle() called with: view = [$view], title = [$title]")
        appWebChromeClientCallback?.onReceivedTitle(title)
        super.onReceivedTitle(view, title)
    }

    //Android 5.0+
    /** refer: [https://isming.me/2015/12/21/android-webview-upload-file/]*/
    @SuppressLint("NewApi")
    override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
        Timber.d("Android 5.0+ onShowFileChooser() called with: webView = [$webView], filePathCallback = [$filePathCallback], fileChooserParams = [$fileChooserParams]")
        var acceptType: String? = null
        if (fileChooserParams.acceptTypes.isNotEmpty()) {
            acceptType = fileChooserParams.acceptTypes[0]
        }
        Timber.d("fileChooserParams.getAcceptTypes():$acceptType")
        doOpenFileChooserAboveL(filePathCallback, acceptType)
        return true
    }

    private fun doOpenFileChooserAboveL(filePathCallback: ValueCallback<Array<Uri>>, acceptType: String?) {
        if (uriValuesCallback != null) {
            uriValuesCallback!!.onReceiveValue(null)
        }
        uriValuesCallback = filePathCallback
        if (!TextUtils.isEmpty(acceptType) && acceptType?.contains(IMAGE_TYPE) == true) {
            takeImages()
        } else {
            systemMediaSelector.takeFileFromSystem().setMimeType(acceptType).start()
        }
    }

    private fun takeImages() {
        showTakeMediaDialog(host,
                {
                    AutoPermission.with(host)
                            .runtime()
                            .permission(Manifest.permission.CAMERA)
                            .onDenied { returnEmptyFile() }
                            .onGranted {
                                systemMediaSelector.takePhotoFromCamera().setNeedCrop().start()
                            }
                            .start()
                },
                {
                    AutoPermission.with(host)
                            .runtime()
                            .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .onDenied { returnEmptyFile() }
                            .onGranted {
                                systemMediaSelector.takePhotoFromSystem().setNeedCrop().start()
                            }
                            .start()
                },
                {
                    returnEmptyFile()
                })
    }

    private fun returnFile(uris: List<Uri>) {
        Timber.d("returnFile() called with $uris")
        uriValuesCallback?.onReceiveValue(uris.toTypedArray())
        uriValuesCallback = null
    }

    private fun returnEmptyFile() {
        Timber.d("returnEmptyFile() called")
        if (uriValuesCallback != null) {
            uriValuesCallback!!.onReceiveValue(null)
        }
        uriValuesCallback = null
    }

    internal interface AppWebChromeClientCallback {
        fun onReceivedTitle(title: String)
        fun onProgressChanged(newProgress: Int)
    }

}