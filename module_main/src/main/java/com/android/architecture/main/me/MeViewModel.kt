package com.android.architecture.main.me

import com.android.base.app.mvvm.RxViewModel
import com.android.base.concurrent.SchedulerProvider
import com.app.base.services.usermanager.AppDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-05 15:48
 */
@HiltViewModel
class MeViewModel @Inject constructor(
    val appDataSource: AppDataSource,
    private val schedulerProvider: SchedulerProvider,
) : RxViewModel() {

}