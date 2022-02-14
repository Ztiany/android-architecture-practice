package com.vclusters.cloud.main.home.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.utils.common.ignoreCrash
import com.vclusters.cloud.main.home.phone.data.CloudPhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhonePreviewsViewModel @Inject constructor(
    private val cloudPhoneRepository: CloudPhoneRepository
) : ViewModel() {

    private val _announcement = MutableStateFlow<List<String>>(emptyList())
    val announcement = _announcement.asStateFlow()

    init {
        loadHomeAnnouncements()
    }

    fun loadHomeAnnouncements() {
        viewModelScope.launch {
            ignoreCrash {
                _announcement.emit(cloudPhoneRepository.homeAnnouncements().map {
                    it.content
                })
            }
        }
    }

}