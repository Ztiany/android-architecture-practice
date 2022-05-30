package com.android.base.foundation.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Don't spread a Bus everywhere, restrict a Bus within a module.
 *
 *@author Ztiany
 */
@Deprecated("LiveData is not designed for events, use SharedFlow instead.")
abstract class LiveBus {

    private val eventMap = HashMap<String, SingleLiveData<*>>()

    @Synchronized
    fun <T> getSender(eventName: String): MutableLiveData<T> {
        var liveData = eventMap[eventName]
        if (null == liveData) {
            liveData = SingleLiveData<T>()
            eventMap[eventName] = liveData
        }
        return liveData as MutableLiveData<T>
    }

    @Synchronized
    fun <T> getReceiver(eventName: String): LiveData<T> {
        var liveData = eventMap[eventName]
        if (null == liveData) {
            liveData = SingleLiveData<T>()
            eventMap[eventName] = liveData
        }
        return liveData as LiveData<T>
    }

    @Synchronized
    fun <T> destroy(eventName: String): Boolean {
        return null != eventMap.remove(eventName)
    }

}

inline fun <reified T> LiveBus.getClassReceiver(): LiveData<T> {
    return getReceiver(T::class.java.name)
}

inline fun <reified T> LiveBus.getClassSender(): MutableLiveData<T> {
    return getSender(T::class.java.name)
}