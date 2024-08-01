package com.android.sdk.mediaselector

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.base.delegate.activity.ActivityDelegateOwner
import com.android.base.delegate.fragment.FragmentDelegateOwner

/**
 * If your fragment has implemented [FragmentDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany.
 */
fun Fragment.newMediaSelector(resultListener: ResultListener): MediaSelector {
    return MediaSelectorImpl(this, resultListener).also {
        autoCallback(this, it)
    }
}

/**
 * If your activity has implemented [ActivityDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany
 */
fun FragmentActivity.newMediaSelector(resultListener: ResultListener): MediaSelector {
    return MediaSelectorImpl(this, resultListener).also {
        autoCallback(this, it)
    }
}

/**
 * If your activity has implemented [ActivityDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany
 */
fun Fragment.newMediaSelector(
    cancellationHandler: () -> Unit = {},
    resultHandler: (List<MediaItem>) -> Unit,
): MediaSelector {

    return MediaSelectorImpl(this, object : ResultListener {

        override fun onResult(result: List<MediaItem>) {
            resultHandler(result)
        }

        override fun onCanceled() {
            cancellationHandler()
        }

    }).also {
        autoCallback(this, it)
    }
}

/**
 * If your activity has implemented [ActivityDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany
 */
fun FragmentActivity.newMediaSelector(
    cancellationHandler: () -> Unit = {},
    resultHandler: (List<MediaItem>) -> Unit,
): MediaSelector {

    return MediaSelectorImpl(this, object : ResultListener {

        override fun onResult(result: List<MediaItem>) {
            resultHandler(result)
        }

        override fun onCanceled() {
            cancellationHandler()
        }

    }).also {
        autoCallback(this, it)
    }
}