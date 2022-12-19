package com.biyun.cg.box.account.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.architecture.data.*
import com.android.base.foundation.data.Resource
import com.app.common.api.usermanager.User
import com.app.base.app.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.biyun.cg.box.account.data.AccountDataSource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountDataSource: AccountDataSource,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<User>>()
    val loginState: LiveData<Resource<User>>
        get() = _loginState

    fun login(phone: String, password: String) {
        _loginState.setLoading()

        viewModelScope.launch(dispatcherProvider.io()) {
            try {
                val login = accountDataSource.login(phone, password)
                _loginState.setData(login)
            } catch (e: Exception) {
                _loginState.setError(e)
            }
        }
    }

}