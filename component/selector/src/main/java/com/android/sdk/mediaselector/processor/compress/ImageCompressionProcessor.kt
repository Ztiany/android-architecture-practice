package com.android.sdk.mediaselector.processor.compress

import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.processor.BaseProcessor
import com.android.sdk.mediaselector.utils.createInternalPath
import com.android.sdk.mediaselector.utils.supportImageCompression
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File

internal class ImageCompressionProcessor(
    private val lifecycleOwner: LifecycleOwner,
    private val host: ActFragWrapper,
    private val config: CompressionConfig,
) : BaseProcessor() {

    override fun start(params: List<MediaItem>) {
        lifecycleOwner.lifecycle.coroutineScope.launch {
            startCompression(params)
        }
    }

    private suspend fun startCompression(params: List<MediaItem>) {
        val result = params.map {
            it.takeIf { it.supportImageCompression() }?.let { item ->
                try {
                    doCompression(item)
                } catch (e: Exception) {
                    null
                }
            }.takeIf { file ->
                file != null
            }?.let { file ->
                it.copy(uri = Uri.fromFile(file), path = file.absolutePath, size = file.length())
            } ?: it
        }

        processorChain.onResult(result)
    }

    private suspend fun doCompression(item: MediaItem) = suspendCancellableCoroutine {
        Luban.with(host.context)
            .load(item.uri)
            .ignoreBy(config.ignoreBy)
            .setRenameListener { filePath: String ->
                val indexOf = filePath.lastIndexOf(".")
                val postfix = if (indexOf != -1) filePath.substring(indexOf) else ".jpg"
                host.context.createInternalPath(postfix)
            }
            .setCompressListener(object : OnNewCompressListener {
                override fun onStart() {}
                override fun onSuccess(source: String, compressFile: File) {
                    Timber.d("image compression for $source succeeded. result: $compressFile")
                    it.resumeWith(Result.success(compressFile))
                }

                override fun onError(source: String, e: Throwable) {
                    Timber.e(e, "image compression for $source failed.")
                    it.resumeWith(Result.failure(e))
                }
            })
            .launch()
    }

}