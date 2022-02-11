package com.vclusters.cloud.main.home.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.data.Resource
import com.android.base.foundation.data.emitData
import com.android.base.foundation.data.emitError
import com.android.base.foundation.data.emitLoading
import com.app.base.app.DispatcherProvider
import com.app.base.services.devicemanager.CloudDevice
import com.app.base.services.devicemanager.DeviceManager
import com.vclusters.cloud.main.home.phone.data.CloudPhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhoneViewModel @Inject constructor(
    private val deviceManager: DeviceManager,
    private val dispatcherProvider: DispatcherProvider,
    private val cloudPhoneRepository: CloudPhoneRepository
) : ViewModel() {

    private val _devicesState = MutableStateFlow<Resource<List<CloudDevice>>>(Resource.loading())
    val devicesState: Flow<Resource<List<CloudDevice>>>
        get() = _devicesState

    init {
        loadCloudDevices()
    }

    fun loadCloudDevices() {
        viewModelScope.launch(dispatcherProvider.io()) {
            _devicesState.emitLoading()
            try {
                _devicesState.emitData(deviceManager.cloudDevices())
            } catch (e: Exception) {
                Timber.e(e)
                _devicesState.emitError(e)
            }
        }
    }

    fun rebootCloudDevice(userCardId: String) {

    }

    fun resetCloudDevice(userCardId: String) {

    }

}