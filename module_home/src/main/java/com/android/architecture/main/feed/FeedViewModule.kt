package com.android.architecture.main.feed

import androidx.hilt.lifecycle.ViewModelInject
import com.android.base.app.mvvm.RxViewModel
import com.android.base.concurrent.SchedulerProvider
import com.app.base.data.app.AppDataSource

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-05 14:23
 */
class FeedViewModule @ViewModelInject constructor(
        val appDataSource: AppDataSource,
        private val schedulerProvider: SchedulerProvider,
) : RxViewModel() {

}