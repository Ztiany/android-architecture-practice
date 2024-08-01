package com.android.sdk.mediaselector.processor.compress

import android.net.Uri
import androidx.lifecycle.coroutineScope
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.processor.BaseProcessor
import com.android.sdk.mediaselector.utils.createInternalPath
import com.android.sdk.mediaselector.utils.getPostfix
import com.android.sdk.mediaselector.utils.supportImageCompression
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File
import java.io.FileOutputStream

internal class ImageCompressionProcessor(
    private val host: ActFragWrapper,
    private val config: CompressionConfig,
) : BaseProcessor() {

    override fun start(params: List<MediaItem>) {
        host.lifecycleOwner.lifecycle.coroutineScope.launch {
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

    private suspend fun doCompression(item: MediaItem): File {
        // Luban can't compress the image from the content provider directly.
        val path = item.path.takeIf { !PictureMimeType.isContent(it) } ?: withContext(Dispatchers.IO) {
        val postfix = item.getPostfix(host.context) ?: "jpeg"
            val target = host.context.createInternalPath(".${postfix}")
            val out = FileOutputStream(target)
            host.context.contentResolver.openInputStream(item.uri)?.use { inputStream ->
                inputStream.copyTo(out)
            }
            runCatching { out.close() }
            target
        }

        return compress(path)
    }

    private suspend fun compress(path: String) = suspendCancellableCoroutine {
        Luban.with(host.context)
            .load(path)
            .ignoreBy(config.ignoreBy)
            .setCompressListener(object : OnNewCompressListener {
                override fun onStart() {}
                override fun onSuccess(source: String, compressFile: File) {
                    Timber.d("image compression for $source succeeded. result: $compressFile")
                    it.resumeWith(Result.success(compressFile))
                }

                override fun onError(source: String, e: Throwable?) {
                    val exception = e ?: Exception("image compression failed.")
                    Timber.e(exception, "image compression for $source failed.")
                    it.resumeWith(Result.failure(exception))
                }
            })
            .launch()
    }

}