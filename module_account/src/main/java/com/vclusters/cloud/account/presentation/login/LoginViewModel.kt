package com.vclusters.cloud.account.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.data.Resource
import com.app.base.services.usermanager.User
import com.vclusters.cloud.account.data.AccountDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountDataSource: AccountDataSource,
) : ViewModel() {

    private val _loginState = MutableSharedFlow<Resource<User>>(0)
    val loginState: SharedFlow<Resource<User>>
        get() = _loginState

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loginState.emit(Resource.loading())
            accountDataSource.login(phone, password)
                .catch {
                    Timber.d("$it")
                    _loginState.emit(Resource.error(it))
                }.collect {
                    _loginState.emit(Resource.success(it))
                }
        }
    }

}