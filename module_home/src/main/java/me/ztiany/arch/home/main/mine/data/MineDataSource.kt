package me.ztiany.arch.home.main.mine.data

import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-05 15:34
 */
interface MineDataSource {
    fun loadData(): String
}

class MineRepository @Inject constructor() : MineDataSource {

    override fun loadData(): String {
        return "Mine"
    }

}