package me.ztiany.arch.home.main.index.data

import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-05 15:34
 */
interface IndexDataSource {
    fun loadData(): String
}

class IndexRepository @Inject constructor() : IndexDataSource {

    override fun loadData(): String {
        return "Index"
    }

}