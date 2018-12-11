package me.ztiany.arch.home.main.mine.presentation

import com.android.base.app.mvvn.ArchViewModel
import me.ztiany.arch.home.main.mine.data.MineDataSource
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-05 15:48
 */
class MineViewModel @Inject constructor(private val mineDataSource: MineDataSource) : ArchViewModel()