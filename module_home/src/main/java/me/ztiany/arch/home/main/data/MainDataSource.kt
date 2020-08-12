package me.ztiany.arch.home.main.data

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-05 15:34
 */
@ActivityScoped
class MainRepository @Inject constructor() {

    fun loadData(): String {
        return "Index"
    }

}