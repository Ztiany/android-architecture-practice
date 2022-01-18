package com.vclusters.cloud.main.home.mine

import androidx.lifecycle.ViewModel
import com.app.base.services.usermanager.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-05 15:48
 */
@HiltViewModel
class MineViewModel @Inject constructor(
    val userManager: UserManager
) : ViewModel() {

}