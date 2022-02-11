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

@HiltViewModel
class PhonePreviewsViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val cloudPhoneRepository: CloudPhoneRepository
) : ViewModel() {

    private val _announcement = MutableLiveData<List<String>>()
    val announcement: LiveData<List<String>>
        get() = _announcement

    init {
        loadHomeAnnouncements()
    }

    fun loadHomeAnnouncements() {
        viewModelScope.launch(dispatcherProvider.io()) {
            ignoreCrash {
                _announcement.postValue(cloudPhoneRepository.homeAnnouncements().map {
                    it.title
                })
            }
        }
    }

}