package com.vclusters.cloud.account.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.data.Resource
import com.app.base.app.DispatcherProvider
import com.app.base.services.usermanager.User
import com.vclusters.cloud.account.data.AccountDataSource
import com.vclusters.cloud.account.data.HistoryUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountDataSource: AccountDataSource,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    var historyUsers = emptyList<HistoryUser>()
        private set

    private val _historyUserEnable = MutableLiveData<Boolean>()
    val historyUserEnable: LiveData<Boolean>
        get() = _historyUserEnable

    private val _loginState = MutableSharedFlow<Resource<User>>(0)
    val loginState: SharedFlow<Resource<User>>
        get() = _loginState

    init {
        accountDataSource.historyUserList()
            .onEach {
                historyUsers = it
                _historyUserEnable.postValue(it.isNotEmpty())
            }
            .launchIn(viewModelScope)
    }

    fun login(phone: String, password: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            _loginState.emit(Resource.loading())
            accountDataSource.login(phone, password)
                .catch {
                    _loginState.emit(Resource.error(it))
                }.collect {
                    saveHistoryUser(phone, password)
                    _loginState.emit(Resource.success(it))
                }
        }
    }

    private fun saveHistoryUser(phone: String, password: String) {
        accountDataSource.saveHistoryUser(HistoryUser(phone, password))
    }

    fun deleteHistoryUser(historyUser: HistoryUser) {
        accountDataSource.deleteHistoryUser(historyUser)
    }

}