package com.android.sdk.mediaselector

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.base.delegate.activity.ActivityDelegateOwner
import com.android.base.delegate.fragment.FragmentDelegateOwner
import com.android.sdk.mediaselector.processor.Processor

/**
 * If your fragment has implemented [FragmentDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany.
 */
fun Fragment.newMediaSelector(vararg processors: Processor, resultListener: ResultListener): MediaSelector {
    return MediaSelectorImpl(this, processors.toList(), resultListener).also {
        autoCallback(this, it)
    }
}

/**
 * If your activity has implemented [ActivityDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany
 */
fun FragmentActivity.newMediaSelector(vararg processors: Processor, resultListener: ResultListener): MediaSelector {
    return MediaSelectorImpl(this, processors.toList(), resultListener).also {
        autoCallback(this, it)
    }
}

typealias ResultHandler = (scene: String, results: List<MediaItem>) -> Unit

/**
 * If your activity has implemented [ActivityDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany
 */
fun Fragment.newMediaSelector(
    vararg processors: Processor,
    cancellationHandler: () -> Unit = {},
    resultHandler: ResultHandler,
): MediaSelector {

    return MediaSelectorImpl(this, processors.toList(), object : ResultListener {

        override fun onResult(scene: String, result: List<MediaItem>) {
            resultHandler(scene, result)
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
    vararg processors: Processor,
    cancellationHandler: () -> Unit = {},
    resultHandler: ResultHandler,
): MediaSelector {

    return MediaSelectorImpl(this, processors.toList(), object : ResultListener {

        override fun onResult(scene: String, result: List<MediaItem>) {
            resultHandler(scene, result)
        }

        override fun onCanceled() {
            cancellationHandler()
        }

    }).also {
        autoCallback(this, it)
    }
}