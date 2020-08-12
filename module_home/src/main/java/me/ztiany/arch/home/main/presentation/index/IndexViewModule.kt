package me.ztiany.arch.home.main.presentation.index

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.app.mvvm.RxViewModel
import me.ztiany.arch.home.main.data.MainRepository

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-05 14:23
 */
class IndexViewModule @ViewModelInject constructor(
        private val mainRepository: MainRepository
) : RxViewModel() {

    private val _demo = MutableLiveData<String>()
    val demo: LiveData<String>
        get() = _demo

    init {
        _demo.postValue("Hello, Welcome!")
    }

}
