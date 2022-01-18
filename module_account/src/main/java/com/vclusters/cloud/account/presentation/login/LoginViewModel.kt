package com.vclusters.cloud.account.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vclusters.cloud.account.data.AccountDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountDataSource: AccountDataSource,
) : ViewModel() {

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            accountDataSource.login(phone, password)
        }
    }

}