package com.android.architecture.main.me

import androidx.hilt.lifecycle.ViewModelInject
import com.android.base.app.mvvm.RxViewModel
import com.android.base.concurrent.SchedulerProvider
import com.app.base.data.app.AppDataSource

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-05 15:48
 */
class MeViewModel @ViewModelInject constructor(
        val appDataSource: AppDataSource,
        private val schedulerProvider: SchedulerProvider,
) : RxViewModel() {

}