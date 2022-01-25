package com.vclusters.cloud.main.home.phone

import androidx.lifecycle.ViewModel
import com.app.base.services.usermanager.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 */
@HiltViewModel
class PhoneRootViewModule @Inject constructor(
    val userManager: UserManager
) : ViewModel() {

}