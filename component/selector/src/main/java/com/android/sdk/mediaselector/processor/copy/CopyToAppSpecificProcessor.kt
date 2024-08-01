package com.android.sdk.mediaselector.processor.copy

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.processor.BaseProcessor
import com.android.sdk.mediaselector.utils.createInternalPath
import com.android.sdk.mediaselector.utils.getPostfix
import com.android.sdk.mediaselector.utils.isFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Copy the media items in the process to a app-specific directory if it is represented by a content URI.
 */
internal class CopyToAppSpecificProcessor(
    private val host: ActFragWrapper,
) : BaseProcessor() {

    override fun start(params: List<MediaItem>) {
        host.lifecycleOwner.lifecycle.coroutineScope.launch {
            val copiedItems = mutableListOf<MediaItem>()

            for (item: MediaItem in params) {
                if (item.uri.isFile()) {
                    copiedItems.add(item)
                    continue
                }

                val copied = copyToInternal(host.context, item)
                if (copied == null) {
                    processorChain.onCanceled()
                    return@launch
                }
                copiedItems.add(copied)
            }

            processorChain.onResult(copiedItems)
        }
    }

    private suspend fun copyToInternal(context: Context, item: MediaItem): MediaItem? {
        val postfix = item.getPostfix(context) ?: "jpeg"
        val target = context.createInternalPath(".${postfix}")
        return try {
            doCopy(target, context, item)
            val savedFile = File(target)
            val failed = !savedFile.exists()
            Timber.d("result = ${!failed}: copy ${item.uri} to $target")
            if (failed) {
                null
            } else item.copy(
                uri = Uri.fromFile(savedFile),
                path = target,
            )
        } catch (e: IOException) {
            Timber.e(e, "failed to copy ${item.uri} to $target")
            null
        }
    }

    private suspend fun doCopy(target: String, context: Context, item: MediaItem) {
        withContext(Dispatchers.IO) {
            val out = FileOutputStream(target)
            context.contentResolver.openInputStream(item.uri)?.use { inputStream ->
                inputStream.copyTo(out)
            }
            runCatching { out.close() }
        }
    }

}