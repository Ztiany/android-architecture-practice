package com.app.base.app

import android.arch.lifecycle.ViewModelProvider
import com.android.base.app.dagger.Injectable
import com.android.base.app.fragment.BaseListFragment
import com.android.base.rx.AutoDisposeLifecycleOwnerEx
import com.app.base.router.AppRouter
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-01-15 12:57
 */
open class InjectorBaseListFragment<T> : BaseListFragment<T>(), Injectable, AutoDisposeLifecycleOwnerEx {

    @Inject protected lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject protected lateinit var appRouter: AppRouter
    @Inject protected lateinit var errorHandler: ErrorHandler

}