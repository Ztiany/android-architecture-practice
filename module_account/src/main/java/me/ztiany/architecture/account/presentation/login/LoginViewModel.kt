package me.ztiany.architecture.account.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.data.Resource
import com.android.base.foundation.data.postData
import com.android.base.foundation.data.postError
import com.android.base.foundation.data.postLoading
import com.app.base.app.DispatcherProvider
import com.app.base.component.usermanager.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ztiany.architecture.account.data.AccountDataSource
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
        _loginState.postLoading()

        viewModelScope.launch(dispatcherProvider.io()) {
            try {
                val login = accountDataSource.login(phone, password)
                _loginState.postData(login)
            } catch (e: Exception) {
                _loginState.postError(e)
            }
        }
    }

}