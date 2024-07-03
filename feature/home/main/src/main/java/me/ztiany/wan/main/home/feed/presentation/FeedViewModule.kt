package me.ztiany.wan.main.home.feed.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ztiany.wan.main.home.feed.data.FeedRepository
import javax.inject.Inject

/**
 * @author Ztiany
 */
@HiltViewModel
class FeedViewModule @Inject constructor(
    private val feedRepository: FeedRepository,
) : ViewModel() {

}