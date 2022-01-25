package com.vclusters.cloud.main.home.phone

import androidx.lifecycle.ViewModel
import com.app.base.services.devicemanager.DeviceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhonePreviewsViewModel @Inject constructor(
    private val deviceManager: DeviceManager
) : ViewModel() {



}