package me.ztiany.wan.account.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.state.setData
import com.android.base.foundation.state.setError
import com.android.base.foundation.state.setLoading
import com.android.base.foundation.state.setSuccess
import com.android.base.foundation.state.StateD
import com.android.base.foundation.livedata.SingleLiveData
import com.app.base.config.AppSettings
import com.app.common.api.usermanager.User
import me.ztiany.wan.account.data.AccountDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SmsLoginViewModel @Inject constructor(
    private val accountDataSource: AccountDataSource,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _smsCodeState = SingleLiveData<StateD<Unit>>()
    val smsCodeState: LiveData<StateD<Unit>>
        get() = _smsCodeState

    private val _loginState = MutableLiveData<StateD<User>>()
    val loginState: LiveData<StateD<User>>
        get() = _loginState

    val agreeWithUserProtocol: Boolean
        get() = appSettings.agreeWithUserProtocol

    fun sendSmsCode(phone: String) {
        _smsCodeState.setLoading()

        viewModelScope.launch {
            try {
                accountDataSource.sendLoginCode(phone)
                _smsCodeState.setSuccess()
            } catch (e: Exception) {
                _smsCodeState.setError(e)
            }
        }
    }

    fun smsLogin(phone: String, smsCode: String) {
        _loginState.setLoading()

        viewModelScope.launch {
            try {
                val user = accountDataSource.smsLogin(phone, smsCode)
                _loginState.setData(user)
                appSettings.agreeWithUserProtocol = true
            } catch (e: Exception) {
                _loginState.setError(e)
            }
        }
    }

}