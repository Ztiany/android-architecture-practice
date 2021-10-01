package com.android.architecture.main.feed

import androidx.lifecycle.ViewModel
import com.app.base.services.usermanager.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-05 14:23
 */
@HiltViewModel
class FeedViewModule @Inject constructor(
    val userManager: UserManager
) : ViewModel() {

}