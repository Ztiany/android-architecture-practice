package com.android.sdk.mediaselector.processor.picker

import android.app.Activity
import android.content.Intent
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
 *  refer to:
 *
 * - [Open files using the Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider)
 * - [Access documents and other files from shared storage](https://developer.android.com/training/data-storage/shared/documents-files)
 * - [Document: ACTION_OPEN_DOCUMENT](https://developer.android.com/reference/kotlin/android/content/Intent#action_open_document)
 * - [ACTION_OPEN_DOCUMENT with Storage Access Framework returns duplicate results](https://stackoverflow.com/questions/39804530/action-open-document-with-storage-access-framework-returns-duplicate-results)
 * - [What is the real difference between ACTION_GET_CONTENT and ACTION_OPEN_DOCUMENT?](https://stackoverflow.com/questions/36182134/what-is-the-real-difference-between-action-get-content-and-action-open-document)
 *
 * @see [androidx.activity.result.contract.ActivityResultContracts.OpenDocument]
 * @see [androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments]
 */
internal class SAFPicker(
    private val host: ActFragWrapper,
    private val types: List<String>,
    private val multiple: Boolean,
) : BaseProcessor() {

    override fun start(params: List<MediaItem>) {
        getPermissionRequester().askForReadStoragePermissionWhenUsingBuiltinPicker(
            host.fragmentActivity,
            onGranted = { openSAF() },
            onDenied = { processorChain.onCanceled() }
        )
    }

    private fun openSAF() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
            if (types.size == 1) {
                type = types.first()
            } else {
                type =  MineType.ALL.value
                putExtra(Intent.EXTRA_MIME_TYPES, types.toTypedArray())
            }
        }

        return try {
            host.startActivityForResult(intent, REQUEST_SAF)
        } catch (e: Exception) {
            Timber.d(e, "openSAF")
            processorChain.onCanceled()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_SAF) {
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
            val type = if (types.size == 1) types.first() else MineType.ALL.value
            processorChain.onResult(result.toList().map {
                val realPath = it.getAbsolutePath(host.context)
                host.context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

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
        private const val REQUEST_SAF = 10904
    }

}