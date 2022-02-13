package com.vclusters.cloud.main.home.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.utils.common.ignoreCrash
import com.app.base.app.DispatcherProvider
import com.vclusters.cloud.main.home.phone.data.CloudPhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhonePreviewsViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val cloudPhoneRepository: CloudPhoneRepository
) : ViewModel() {

    private val _announcement = MutableStateFlow<List<String>>(emptyList())
    val announcement = _announcement.asStateFlow()

    init {
        loadHomeAnnouncements()
    }

    fun loadHomeAnnouncements() {
        viewModelScope.launch(dispatcherProvider.io()) {
            ignoreCrash {
                _announcement.emit(cloudPhoneRepository.homeAnnouncements().map {
                    it.title
                })
            }
        }
    }

}