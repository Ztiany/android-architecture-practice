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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhoneViewModel @Inject constructor(
    private val deviceManager: DeviceManager,
    private val dispatcherProvider: DispatcherProvider,
    private val cloudPhoneRepository: CloudPhoneRepository
) : ViewModel() {

    private val rebootingPoneCardIdList = mutableListOf<Int>()

    private val _devicesState = MutableStateFlow<Resource<List<CloudDevice>>>(Resource.loading())
    val devicesState: Flow<Resource<List<CloudDevice>>> = _devicesState.asStateFlow()

    private val _rebootDeviceState = MutableSharedFlow<Resource<Int>>(1)
    val rebootDeviceState: Flow<Resource<Int>> = _rebootDeviceState.asSharedFlow()

    private val _resetDeviceState = MutableSharedFlow<Resource<Int>>(1)
    val resetDeviceState: Flow<Resource<Int>> = _resetDeviceState.asSharedFlow()

    private val _rebootCountDown = MutableSharedFlow<Int>()
    val rebootCountDown = _rebootCountDown.asSharedFlow()

    private val _resetCountDown = MutableSharedFlow<Int>()
    val resetCountDown = _resetCountDown.asSharedFlow()

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

    fun rebootCloudDevice(cardId: Int) {
        viewModelScope.launch {
            _rebootDeviceState.emitLoading()
            try {
                cloudPhoneRepository.rebootCloudDevice(cardId)
                _rebootDeviceState.emitData(cardId)
                startCountDown(_rebootCountDown, cardId)
            } catch (e: Exception) {
                _rebootDeviceState.emitError(e)
            }
        }
    }

    fun resetCloudDevice(cardId: Int) {
        viewModelScope.launch {
            _rebootDeviceState.emitLoading()
            try {
                cloudPhoneRepository.resetCloudDevice(cardId)
                _rebootDeviceState.emitData(cardId)
                startCountDown(_resetCountDown, cardId)
            } catch (e: Exception) {
                _rebootDeviceState.emitError(e)
            }
        }
    }

    private fun startCountDown(flow: MutableSharedFlow<Int>, cardId: Int) {
        rebootingPoneCardIdList.add(cardId)
        viewModelScope.launch {
            delay(15 * 1000)
            rebootingPoneCardIdList.remove(cardId)
            flow.emit(cardId)
        }
    }

    fun isDeviceRebooting(cardId: Int): Boolean {
        return rebootingPoneCardIdList.contains(cardId)
    }

}