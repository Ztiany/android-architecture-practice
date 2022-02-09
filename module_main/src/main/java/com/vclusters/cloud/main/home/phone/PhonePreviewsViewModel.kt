package com.vclusters.cloud.main.home.phone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.data.Resource
import com.android.base.foundation.data.emitData
import com.android.base.foundation.data.emitError
import com.android.base.foundation.data.emitLoading
import com.android.base.utils.common.ignoreCrash
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
class PhonePreviewsViewModel @Inject constructor(
    private val deviceManager: DeviceManager,
    private val dispatcherProvider: DispatcherProvider,
    private val cloudPhoneRepository: CloudPhoneRepository
) : ViewModel() {

    private val _announcement = MutableLiveData<List<String>>()
    val announcement: LiveData<List<String>>
        get() = _announcement

    private val _devicesState = MutableStateFlow<Resource<List<CloudDevice>>>(Resource.loading())
    val devicesState: Flow<Resource<List<CloudDevice>>>
        get() = _devicesState

    init {
        loadCloudDevices()
        loadHomeAnnouncement()
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

    fun loadHomeAnnouncement() {
        viewModelScope.launch(dispatcherProvider.io()) {
            ignoreCrash {
                _announcement.postValue(cloudPhoneRepository.homeAnnouncements().map {
                    it.title
                })
            }
        }
    }

}