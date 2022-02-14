package com.vclusters.cloud.main.home.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.data.Resource
import com.android.base.foundation.data.emitData
import com.android.base.foundation.data.emitError
import com.android.base.foundation.data.emitLoading
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
        viewModelScope.launch() {
            _devicesState.emitLoading()
            try {
                _devicesState.emitData(deviceManager.cloudDevices())
            } catch (e: Exception) {
                Timber.e(e)
                _devicesState.emitError(e)
            }
        }
    }

    fun rebootCloudDevice(deviceId: Int) {
        viewModelScope.launch {
            _rebootDeviceState.emitLoading()
            try {
                cloudPhoneRepository.rebootCloudDevice(deviceId)
                _rebootDeviceState.emitData(deviceId)
                startCountDown(_rebootCountDown, deviceId)
            } catch (e: Exception) {
                _rebootDeviceState.emitError(e)
            }
        }
    }

    fun resetCloudDevice(deviceId: Int) {
        viewModelScope.launch {
            _resetDeviceState.emitLoading()
            try {
                cloudPhoneRepository.resetCloudDevice(deviceId)
                _resetDeviceState.emitData(deviceId)
                startCountDown(_resetCountDown, deviceId)
            } catch (e: Exception) {
                _resetDeviceState.emitError(e)
            }
        }
    }

    private fun startCountDown(flow: MutableSharedFlow<Int>, deviceId: Int) {
        rebootingPoneCardIdList.add(deviceId)
        viewModelScope.launch {
            delay(15 * 1000)
            rebootingPoneCardIdList.remove(deviceId)
            flow.emit(deviceId)
        }
    }

    fun isDeviceRebooting(cardId: Int): Boolean {
        return rebootingPoneCardIdList.contains(cardId)
    }

}