package com.vclusters.cloud.main.home.phone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.utils.common.ignoreCrash
import com.app.base.app.DispatcherProvider
import com.vclusters.cloud.main.home.phone.data.CloudPhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 */
@HiltViewModel
class PhoneRootViewModule @Inject constructor(
    private val cloudPhoneRepository: CloudPhoneRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _messageCount = MutableLiveData<Int>()
    val messageCount: LiveData<Int>
        get() = _messageCount

    fun queryMessageCount() {
        viewModelScope.launch(dispatcherProvider.io()) {
            ignoreCrash {
                _messageCount.postValue(cloudPhoneRepository.messageCount())
            }
        }
    }

}