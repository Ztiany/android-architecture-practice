package me.ztiany.arch.home.main.data

import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-05 15:34
 */
interface MainDataSource {
    fun loadData(): String
}

class MainRepository @Inject constructor() : MainDataSource {

    override fun loadData(): String {
        return "Index"
    }

}