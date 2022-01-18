package com.app.base.common.web

import android.os.Bundle
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
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
@Route(path = BrowserPath.PATH)
@AndroidEntryPoint
class BrowserActivity : AppBaseActivity() {

    @JvmField @Autowired(name = BrowserPath.FRAGMENT_KEY) var fragmentClass: String? = null

    @JvmField @Autowired(name = BrowserPath.SHOW_HEADER_KEY) var showHeader: Boolean = true

    @JvmField @Autowired(name = BrowserPath.URL_KEY) var targetUrl: String? = null

    @JvmField @Autowired(name = BrowserPath.ARGUMENTS_KEY) var bundle: Bundle? = null

    @JvmField @Autowired(name = BrowserPath.JS_CALL_INTERCEPTOR_CLASS_KEY) var customJsCallInterceptor: String? = null

    @JvmField @Autowired(name = BrowserPath.CACHE_ENABLE) var cacheEnable: Boolean = false

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