package com.android.sdk.mediaselector.processor

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.android.sdk.mediaselector.ComponentStateHandler
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.ResultListener
import timber.log.Timber

internal class ProcessorManager(
    private val lifecycleOwner: LifecycleOwner,
    private val resultListener: ResultListener,
) : ComponentStateHandler {

    private val processors = mutableListOf<Processor>()

    private var currentScene: String = ""

    private var processorProgress = 0

    private val processorChain = object : ProcessorChain {

        override fun onCanceled() {
            resultListener.onCanceled()
        }

        override fun onResult(items: List<MediaItem>) {
            continueProcedure(items)
        }
    }

    fun install(assembledProcessors: List<Processor>) {
        if (assembledProcessors.isEmpty()) {
            throw IllegalStateException("assembledProcessors is empty.")
        }
        Timber.d("install: assembledProcessors.size = ${assembledProcessors.size}")
        processors.clear()
        processors.addAll(assembledProcessors)
        processors.forEach { it.onAttachToChain(processorChain) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult: processors.size = ${processors.size}, requestCode=$requestCode, resultCode=$resultCode, data=$data")
        processors.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.d("onSaveInstanceState: processorProgress = $processorProgress")
        outState.putInt(PROGRESS_KEY, processorProgress)
        outState.putString(CURRENT_SCENE_KEY, currentScene)
        processors.forEach { it.onSaveInstanceState(outState) }
    }

    override fun onRestoreInstanceState(outState: Bundle?) {
        outState?.let {
            processorProgress = it.getInt(PROGRESS_KEY)
            currentScene = it.getString(CURRENT_SCENE_KEY, "")
        }
        Timber.d("onRestoreInstanceState: processorProgress = $processorProgress")
        processors.forEach { it.onRestoreInstanceState(outState) }
    }

    fun start(scene: String) {
        currentScene = scene
        processorProgress = 0
        Timber.d("start: processorProgress = 0")
        continueProcedure(emptyList())
    }

    private fun continueProcedure(params: List<MediaItem>) {
        val processor = processors.getOrNull(processorProgress)
        Timber.d("processorProgress = $processorProgress, continueProcedure: $processor")
        if (processor == null) {
            onAllProcessorCompleted(params)
            return
        }
        processorProgress++
        processor.start(params)
    }

    private fun onAllProcessorCompleted(result: List<MediaItem>) {
        resultListener.onResult(currentScene, result)
    }

    companion object {
        private const val CURRENT_SCENE_KEY = "current_scene_key"

        private const val PROGRESS_KEY = "processor_progress_key"
    }

}