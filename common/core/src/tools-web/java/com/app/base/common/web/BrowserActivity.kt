package com.app.base.common.web

import android.os.Bundle
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.utils.android.activityArgument
import com.android.base.utils.android.activityArgumentNullable
import com.app.base.R
import com.app.base.app.AppBaseActivity
import com.app.base.common.web.BrowserStarter.ARGUMENTS_KEY
import com.app.base.common.web.BrowserStarter.CACHE_ENABLE
import com.app.base.common.web.BrowserStarter.FRAGMENT_KEY
import com.app.base.common.web.BrowserStarter.JS_CALL_INTERCEPTOR_CLASS_KEY
import com.app.base.common.web.BrowserStarter.SHOW_HEADER_KEY
import com.app.base.common.web.BrowserStarter.URL_KEY
import com.app.base.config.AppSettings
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * 应用内浏览器
 *
 * @author Ztiany
 */
@AndroidEntryPoint
class BrowserActivity : AppBaseActivity() {

    private val fragmentClass by activityArgumentNullable<String>(FRAGMENT_KEY)

    private val bundle: Bundle? by activityArgumentNullable<Bundle>(ARGUMENTS_KEY)

    private val customJsCallInterceptor by activityArgumentNullable<String>(JS_CALL_INTERCEPTOR_CLASS_KEY)

    private val showHeader by activityArgument(SHOW_HEADER_KEY, true)

    private val targetUrl by activityArgument<String>(URL_KEY)

    private val cacheEnable by activityArgument(CACHE_ENABLE, false)

    @Inject lateinit var appSettings: AppSettings

    override fun initialize(savedInstanceState: Bundle?) {
        enableTraditionalBackPressHandling()
    }

    override fun provideLayout() = R.layout.app_base_web_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        Timber.d("fragmentClass = $fragmentClass")
        Timber.d("showHeader = $showHeader")
        Timber.d("targetUrl = $targetUrl")
        Timber.d("bundle = $bundle")
        Timber.d("customJsCallInterceptor = $customJsCallInterceptor")
        Timber.d("cacheEnable = $cacheEnable")

        var urlStr = targetUrl

        if (!URLUtil.isValidUrl(urlStr)) {
            urlStr = if (urlStr.startsWith("/")) {
                WebUtils.removePath(appSettings.baseWebUrl()) + urlStr
            } else {
                appSettings.baseWebUrl() + urlStr
            }
        }

        Timber.d("final = $urlStr")

        val argument = Bundle().apply {
            putString(URL_KEY, urlStr)
            putString(JS_CALL_INTERCEPTOR_CLASS_KEY, customJsCallInterceptor ?: "")
            putBoolean(SHOW_HEADER_KEY, showHeader)
            putBoolean(CACHE_ENABLE, cacheEnable)
            bundle?.let { putBundle(ARGUMENTS_KEY, it) }
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