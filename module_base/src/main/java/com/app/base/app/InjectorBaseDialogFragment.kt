package com.app.base.app

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.base.app.dagger.Injectable
import com.android.base.app.fragment.BaseDialogFragment
import com.android.base.app.ui.UIErrorHandler
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-01-15 12:57
 */
open class InjectorBaseDialogFragment : BaseDialogFragment(), Injectable, UIErrorHandler {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var errorHandler: ErrorHandler

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

}