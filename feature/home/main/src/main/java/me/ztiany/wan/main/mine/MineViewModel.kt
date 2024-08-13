package me.ztiany.wan.main.mine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.app.common.api.usermanager.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *@author Ztiany
 */
@HiltViewModel
class MineViewModel @Inject constructor(
    userManager: UserManager
) : ViewModel(){

    val userState = userManager.userFlow().asLiveData()

}