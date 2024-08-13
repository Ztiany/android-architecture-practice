package me.ztiany.wan.main.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.ztiany.wan.main.home.data.HomeRepository
import javax.inject.Inject

/**
 * @author Ztiany
 */
@HiltViewModel
class FeedViewModule @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            homeRepository.loadRecommends()
        }
    }

}