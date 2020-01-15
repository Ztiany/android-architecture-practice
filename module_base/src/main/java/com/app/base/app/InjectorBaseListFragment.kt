package com.app.base.app

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.base.app.dagger.Injectable
import com.android.base.app.fragment.BaseListFragment
import com.android.base.app.ui.UIErrorHandler
import com.app.base.router.AppRouter
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-01-15 12:57
 */
open class InjectorBaseListFragment<T> : BaseListFragment<T>(), Injectable, UIErrorHandler {

    @Inject protected lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject protected lateinit var appRouter: AppRouter
    @Inject protected lateinit var errorHandler: ErrorHandler

    inline fun <reified VM : ViewModel> injectViewModel(): Lazy<VM> {
        return viewModels { viewModelFactory }
    }

    inline fun <reified VM : ViewModel> injectActivityViewModel(): Lazy<VM> {
        return viewModels(
                ownerProducer = { requireActivity() },
                factoryProducer = { viewModelFactory })
    }

    override fun handleError(throwable: Throwable) {
        errorHandler.handleError(throwable)
    }

    override fun generateErrorMessage(throwable: Throwable) {
        errorHandler.generateMessage(throwable)
    }
    
}