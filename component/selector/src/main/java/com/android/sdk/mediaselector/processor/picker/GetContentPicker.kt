package com.android.sdk.mediaselector.processor.picker

import android.app.Activity
import android.content.Intent
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.Source
import com.android.sdk.mediaselector.getPermissionRequester
import com.android.sdk.mediaselector.processor.BaseProcessor
import com.android.sdk.mediaselector.utils.getAbsolutePath
import com.android.sdk.mediaselector.utils.getClipDataUris
import com.android.sdk.mediaselector.utils.getSingleDataUri
import com.android.sdk.mediaselector.utils.tryFillPickedMediaInfo
import timber.log.Timber

/**
 * refers to: [What is the real difference between ACTION_GET_CONTENT and ACTION_OPEN_DOCUMENT?](https://stackoverflow.com/questions/36182134/what-is-the-real-difference-between-action-get-content-and-action-open-document)
 *
 * @see [androidx.activity.result.contract.ActivityResultContracts.GetContent]
 * @see [androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents]
 */
internal class GetContentPicker(
    private val host: ActFragWrapper,
    private val type: String,
    private val multiple: Boolean,
) : BaseProcessor() {

    override fun start(params: List<MediaItem>) {
        getPermissionRequester().askForReadStoragePermissionWhenUsingBuiltinPicker(
            host.fragmentActivity,
            onGranted = { openContentSelector() },
            onDenied = { processorChain.onCanceled() }
        )
    }

    private fun openContentSelector() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
        intent.type = type
        try {
            host.startActivityForResult(intent, REQUEST_PICKER)
        } catch (e: Exception) {
            Timber.e(e, "openContentSelector")
            processorChain.onCanceled()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_PICKER) {
            return
        }

        if (resultCode != Activity.RESULT_OK || data == null) {
            Timber.d("onActivityResult: canceled")
            processorChain.onCanceled()
            return
        }

        val result = if (multiple) data.getClipDataUris() else data.getSingleDataUri()
        Timber.d("onActivityResult: result=$result")
        if (result.isEmpty()) {
            processorChain.onCanceled()
        } else {

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
        private const val REQUEST_PICKER = 10903
    }

}