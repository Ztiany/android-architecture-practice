package com.android.sdk.mediaselector

import android.content.Intent
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.sdk.mediaselector.actions.FilePicker
import com.android.sdk.mediaselector.actions.GetContent
import com.android.sdk.mediaselector.actions.GetImageContent
import com.android.sdk.mediaselector.actions.GetVideoContent
import com.android.sdk.mediaselector.actions.ImageAndVideoPicker
import com.android.sdk.mediaselector.actions.ImageCapturer
import com.android.sdk.mediaselector.actions.ImagePicker
import com.android.sdk.mediaselector.actions.SelectImageAction
import com.android.sdk.mediaselector.actions.SelectVideoAction
import com.android.sdk.mediaselector.actions.VideoCapturer
import com.android.sdk.mediaselector.actions.VideoPicker
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.ProcessorManager
import timber.log.Timber

internal class MediaSelectorImpl(
    private val actFragWrapper: ActFragWrapper,
    private val postProcessors: List<Processor>,
    resultListener: ResultListener
) : MediaSelector, ComponentStateHandler {

    private var currentAction: Action? = null

    private val processorManager: ProcessorManager = ProcessorManager(actFragWrapper.lifecycleOwner, resultListener)

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.d("onSaveInstanceState: currentAction = $currentAction")
        currentAction?.let {
            outState.putParcelable(CURRENT_ACTION_KEY, it)
            outState.putSerializable(CURRENT_ACTION_CLASS_KEY, it.javaClass)
        }
        processorManager.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(outState: Bundle?) {
        if (outState != null) {
            val clazz = BundleCompat.getSerializable(outState, CURRENT_ACTION_CLASS_KEY, Class::class.java)
            clazz?.let {
                currentAction = (BundleCompat.getParcelable(outState, CURRENT_ACTION_KEY, it) as? Action)?.apply {
                    processorManager.install(assembleAllProcessors(this))
                }
            }
            Timber.d("onRestoreInstanceState: clazz = $clazz, currentAction = $currentAction")
        }
        processorManager.onRestoreInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        processorManager.onActivityResult(requestCode, resultCode, data)
    }

    fun start(action: Action, scene: String) {
        currentAction = action
        processorManager.install(assembleAllProcessors(action))
        processorManager.start(scene)
    }

    private fun assembleAllProcessors(action: Action) = action.assembleProcessors(actFragWrapper) + postProcessors

    override fun captureImage(): ImageCapturer {
        return ImageCapturer().also {
            it.builtInSelector = this
        }
    }

    override fun captureVideo(): VideoCapturer {
        return VideoCapturer().also {
            it.builtInSelector = this
        }
    }

    override fun getContent(): GetContent {
        return GetContent().also {
            it.builtInSelector = this
        }
    }

    override fun getImageContent(): GetImageContent {
        return GetImageContent().also {
            it.builtInSelector = this
        }
    }

    override fun getVideoContent(): GetVideoContent {
        return GetVideoContent().also {
            it.builtInSelector = this
        }
    }

    override fun pickImage(): ImagePicker {
        return ImagePicker().also {
            it.builtInSelector = this
        }
    }

    override fun pickVideo(): VideoPicker {
        return VideoPicker().also {
            it.builtInSelector = this
        }
    }

    override fun pickImageAndVideo(): ImageAndVideoPicker {
        return ImageAndVideoPicker().also {
            it.builtInSelector = this
        }
    }

    override fun pickFile(): FilePicker {
        return FilePicker().also {
            it.builtInSelector = this
        }
    }

    override fun selectImage(): SelectImageAction {
        return SelectImageAction().also {
            it.builtInSelector = this
        }
    }

    override fun selectVideo(): SelectVideoAction {
        return SelectVideoAction().also {
            it.builtInSelector = this
        }
    }

    companion object {
        private const val CURRENT_ACTION_KEY = "current_action_key"
        private const val CURRENT_ACTION_CLASS_KEY = "current_action_class_key"
    }

}