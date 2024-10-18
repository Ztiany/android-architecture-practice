package me.ztiany.wan.###template###.presentation

import androidx.lifecycle.ViewModel
import me.ztiany.wan.###template###.data.$$$template$$$Repository
import com.app.common.api.usermanager.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class $$$template$$$ViewModel @Inject constructor(
    private val userManager: UserManager,
    private val ###template###Repository: $$$template$$$Repository,
) : ViewModel() {

    val userState = userManager.userFlow()

}