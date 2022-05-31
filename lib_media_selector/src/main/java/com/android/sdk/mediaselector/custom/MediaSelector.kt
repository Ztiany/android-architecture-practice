package com.android.sdk.mediaselector.custom

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.base.delegate.activity.ActivityDelegateOwner
import com.android.base.delegate.fragment.FragmentDelegateOwner
import com.android.sdk.mediaselector.common.ActivityStateHandler
import com.android.sdk.mediaselector.common.MediaSelectorConfiguration
import com.android.sdk.mediaselector.common.ResultListener
import com.android.sdk.mediaselector.common.autoCallback
import timber.log.Timber

/**
 * If your activity has implemented [ActivityDelegateOwner], you don't call methods in [ActivityStateHandler].
 * And the same if your fragment has implemented [FragmentDelegateOwner].
 *
 *@author Ztiany
 */
interface MediaSelector : ActivityStateHandler {

    fun takeMedia(): Instructor

}

/**
 * If your activity has implemented [ActivityDelegateOwner], you don't call methods in [ActivityStateHandler].
 *
 *@author Ztiany
 */
fun newMediaSelector(activity: AppCompatActivity, resultListener: ResultListener): MediaSelector {
    return if (Build.VERSION.SDK_INT < 29 || MediaSelectorConfiguration.isForceUseLegacyApi()) {
        Timber.d("newSystemMediaSelector LegacySystemMediaSelector")
        val mediaSelector = LegacyMediaSelector(activity, resultListener)
        autoCallback(activity, mediaSelector)
        mediaSelector
    } else {
        Timber.d("newSystemMediaSelector AndroidPSystemMediaSelector")
        val mediaSelector = AndroidQMediaSelector(activity, resultListener)
        autoCallback(activity, mediaSelector)
        mediaSelector
    }
}

/**
 * If your fragment has implemented [FragmentDelegateOwner], you don't call methods in [ActivityStateHandler].
 *
 *@author Ztiany
 */
fun newMediaSelector(fragment: Fragment, resultListener: ResultListener): MediaSelector {
    return if (Build.VERSION.SDK_INT < 29 || MediaSelectorConfiguration.isForceUseLegacyApi()) {
        Timber.d("newSystemMediaSelector LegacySystemMediaSelector")
        val mediaSelector = LegacyMediaSelector(fragment, resultListener)
        autoCallback(fragment, mediaSelector)
        mediaSelector
    } else {
        Timber.d("newSystemMediaSelectorAndroidPSystemMediaSelector")
        val mediaSelector = AndroidQMediaSelector(fragment, resultListener)
        autoCallback(fragment, mediaSelector)
        mediaSelector
    }
}
