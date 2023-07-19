package com.app.base.common.web

import android.os.Bundle
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.utils.android.argument
import com.android.base.utils.android.argumentNullable
import com.app.base.R
import com.app.base.app.AppBaseActivity
import com.app.base.common.web.BrowserStarter.*
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

    private val fragmentClass by argumentNullable<String>(FRAGMENT_KEY)

    private val bundle: Bundle? by argumentNullable<Bundle>(ARGUMENTS_KEY)

    private val customJsCallInterceptor by argumentNullable<String>(JS_CALL_INTERCEPTOR_CLASS_KEY)

    private val showHeader by argument(SHOW_HEADER_KEY, true)

    private val targetUrl by argument<String>(URL_KEY)

    private val cacheEnable by argument(CACHE_ENABLE, false)

    @Inject lateinit var appSettings: AppSettings

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