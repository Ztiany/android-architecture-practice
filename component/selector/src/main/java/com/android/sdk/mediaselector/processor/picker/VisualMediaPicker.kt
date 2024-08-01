package com.android.sdk.mediaselector.processor.picker

import android.app.Activity
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.Source
import com.android.sdk.mediaselector.getPermissionRequester
import com.android.sdk.mediaselector.processor.BaseProcessor
import com.android.sdk.mediaselector.utils.MineType
import com.android.sdk.mediaselector.utils.getAbsolutePath
import com.android.sdk.mediaselector.utils.getClipDataUris
import com.android.sdk.mediaselector.utils.getSingleDataUri
import com.android.sdk.mediaselector.utils.tryFillPickedMediaInfo
import timber.log.Timber

/**
 * refers to [Photo Picker](https://developer.android.com/training/data-storage/shared/photopicker).
 *
 * @see [androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia]
 * @see [androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia]
 */
internal class VisualMediaPicker(
    private val host: ActFragWrapper,
    private val type: ActivityResultContracts.PickVisualMedia.VisualMediaType,
    private val count: Int,
) : BaseProcessor() {

    override fun start(params: List<MediaItem>) {
        getPermissionRequester().askForReadStoragePermissionWhenUsingBuiltinPicker(
            host.fragmentActivity,
            onGranted = { openPhotoPicker() },
            onDenied = { processorChain.onCanceled() }
        )
    }

    private fun openPhotoPicker() {
        val intent = if (count == 1) {
            ActivityResultContracts.PickVisualMedia().createIntent(
                host.context,
                PickVisualMediaRequest.Builder().setMediaType(type).build()
            )
        } else {
            ActivityResultContracts.PickMultipleVisualMedia(count).createIntent(
                host.context,
                PickVisualMediaRequest.Builder().setMediaType(type).build()
            )
        }

        try {
            host.startActivityForResult(intent, REQUEST_VISUAL_PICKER)
        } catch (e: Exception) {
            processorChain.onCanceled()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_VISUAL_PICKER) {
            return
        }

        if (resultCode != Activity.RESULT_OK || data == null) {
            Timber.d("onActivityResult: canceled")
            processorChain.onCanceled()
            return
        }

        val result = if (count > 1) data.getClipDataUris() else data.getSingleDataUri()
        Timber.d("onActivityResult: result=$result")
        if (result.isEmpty()) {
            processorChain.onCanceled()
        } else {
            val type = when (this.type) {
                ActivityResultContracts.PickVisualMedia.ImageOnly -> MineType.IMAGE.value
                ActivityResultContracts.PickVisualMedia.VideoOnly -> MineType.VIDEO.value
                else -> MineType.ALL.value
            }
            processorChain.onResult(result.toList().map {
                val realPath = it.getAbsolutePath(host.context)
                MediaItem(
                    id = it.toString(),
                    source = Source.Selector,
                    mineType = type,

                    rawUri = it,
                    rawPath = realPath ?: "",

                    uri = it,
                    path = realPath ?: "",
                ).tryFillPickedMediaInfo(host.context)
            })
        }
    }

    companion object {
        private const val REQUEST_VISUAL_PICKER = 10905
    }

}