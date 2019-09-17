package me.ztiany.arch.home.main.index.presentation

import com.android.base.app.mvvm.RxViewModel
import me.ztiany.arch.home.main.index.data.IndexDataSource
import javax.inject.Inject

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-05 14:23
 */
class IndexViewModule @Inject constructor(
        private val indexDataSource: IndexDataSource
) : RxViewModel()
