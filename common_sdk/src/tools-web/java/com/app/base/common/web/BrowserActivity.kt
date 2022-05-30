package com.app.base.common.web

import android.os.Bundle
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import com.android.base.architecture.fragment.tools.doFragmentTransaction
import com.app.base.R
import com.app.base.app.AppBaseActivity
import com.app.base.config.AppSettings
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * 应用内浏览器
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:29
 */
@AndroidEntryPoint
class BrowserActivity : AppBaseActivity() {

    //BrowserPath.FRAGMENT_KEY
    private val fragmentClass: String? = null

    // BrowserPath.SHOW_HEADER_KEY
    private val showHeader: Boolean = true

    //BrowserPath.URL_KEY
    private val targetUrl: String? = null

    //BrowserPath.ARGUMENTS_KEY
    private val bundle: Bundle? = null

    //BrowserPath.JS_CALL_INTERCEPTOR_CLASS_KEY
    private val customJsCallInterceptor: String? = null

    //name = BrowserPath.CACHE_ENABLE
    private val cacheEnable: Boolean = false

    @Inject lateinit var appSettings: AppSettings

    override fun provideLayout() = R.layout.app_base_web_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        Timber.d("fragmentClass = $fragmentClass")
        Timber.d("showHeader = $showHeader")
        Timber.d("targetUrl = $targetUrl")
        Timber.d("bundle = $bundle")
        Timber.d("customJsCallInterceptor = $customJsCallInterceptor")
        Timber.d("cacheEnable = $cacheEnable")

        var urlStr = targetUrl ?: ""

        if (!URLUtil.isValidUrl(urlStr)) {
            urlStr = if (urlStr.startsWith("/")) {
                WebUtils.removePath(appSettings.baseWebUrl()) + urlStr
            } else {
                appSettings.baseWebUrl() + urlStr
            }
        }

        Timber.d("final = $urlStr")

        val argument = Bundle().apply {
            putString(BrowserPath.URL_KEY, urlStr)
            putString(BrowserPath.JS_CALL_INTERCEPTOR_CLASS_KEY, customJsCallInterceptor ?: "")
            putBoolean(BrowserPath.SHOW_HEADER_KEY, showHeader)
            putBoolean(BrowserPath.CACHE_ENABLE, cacheEnable)
            bundle?.let { putBundle(BrowserPath.ARGUMENTS_KEY, it) }
        }

        if (savedInstanceState == null) {
            val clazz = fragmentClass
            if (!clazz.isNullOrEmpty()) {
                showCustomFragment(argument, clazz)
            } else {
                showInnerFragment(argument)
            }
        }

    }

    private fun showInnerFragment(argument: Bundle) {
        BaseWebFragment().apply {
            arguments = argument
        }.let {
            doFragmentTransaction { addFragment(it) }
        }
    }

    private fun showCustomFragment(argument: Bundle, fragmentClass: String) {
        val instantiate = Fragment.instantiate(this, fragmentClass, argument)
        doFragmentTransaction { addFragment(instantiate) }
    }

}