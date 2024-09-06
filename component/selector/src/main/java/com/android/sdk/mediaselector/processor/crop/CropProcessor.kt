package com.android.sdk.mediaselector.processor.crop

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.getConfiguredCropPrimaryColor
import com.android.sdk.mediaselector.getConfiguredCropTextColor
import com.android.sdk.mediaselector.processor.BaseProcessor
import com.android.sdk.mediaselector.utils.createInternalPath
import com.android.sdk.mediaselector.utils.getAbsolutePath
import com.android.sdk.mediaselector.utils.getPostfix
import com.android.sdk.mediaselector.utils.supportedCropping
import com.yalantis.ucrop.UCrop
import timber.log.Timber
import java.io.File

internal class CropProcessor(
    private val host: ActFragWrapper,
    private val cropOptions: CropOptions,
) : BaseProcessor() {

    private var originContent: List<MediaItem> = emptyList()

    private var processedContent: MutableList<MediaItem> = mutableListOf()

    private var progress = 0

    override fun start(params: List<MediaItem>) {
        originContent = params
        processedContent = mutableListOf()
        progress = 0
        continueCropWork()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_CROP) {
            return
        }
        if (resultCode != Activity.RESULT_OK || data == null) {
            processorChain.onCanceled()
            return
        }

        processCropResult(data)
    }

    private fun processCropResult(data: Intent) {
        val uCropResult = getUCropResult(data)
        Timber.d("processCropResult() called with: data = [$uCropResult]")
        if (uCropResult == null) {
            processorChain.onCanceled()
            return
        }

        val absolutePath = uCropResult.getAbsolutePath(host.context)
        Timber.d("processCropResult() called with: absolutePath = [$absolutePath]")
        if (absolutePath.isNullOrEmpty()) {
            processorChain.onCanceled()
            return
        }

        val origin = originContent[progress - 1]
        val croppedFile = File(absolutePath)
        val uri = Uri.fromFile(croppedFile)
        val croppedItem = origin.copy(
            uri = uri,
            path = absolutePath,
            size = croppedFile.length(),
            width = UCrop.getOutputImageWidth(data),
            height = UCrop.getOutputImageHeight(data),
        )
        processedContent.add(croppedItem)
        continueCropWork()
    }

    private fun continueCropWork() {
        Timber.d("progress: $progress, size: ${originContent.size}")
        if (progress >= originContent.size) {
            processorChain.onResult(processedContent)
            return
        }

        val item = originContent[progress]
        if (item.supportedCropping(host.context)) {
            Timber.d("to crop: ${item.uri}")
            progress++
            toUCrop(item)
        } else {
            Timber.d("skip crop: ${item.uri}")
            processedContent.add(item)
            progress++
            continueCropWork()
        }
    }

    private fun toUCrop(item: MediaItem) {
        val postfix = item.getPostfix(host.context) ?: ".jpeg"
        val targetPath: String = host.context.createInternalPath(postfix)
        val targetUri = Uri.Builder()
            .scheme("file")
            .appendPath(targetPath)
            .build()

        // 参数
        val primaryColor = host.context.getConfiguredCropPrimaryColor()
        val textColor = host.context.getConfiguredCropTextColor()
        val crop = UCrop.Options().apply {
            // crop
            setCompressionFormat(compressFormat(postfix))
            withMaxResultSize(cropOptions.outputX, cropOptions.aspectY)
            withAspectRatio(cropOptions.aspectX.toFloat(), cropOptions.aspectY.toFloat())
            // color
            setToolbarColor(primaryColor)
            setStatusBarColor(primaryColor)
            setToolbarWidgetColor(textColor)
        }

        // 开始裁减
        Timber.d("toUCrop: item = ${item}, targetUri = $targetUri")
        if (host.fragment != null) {
            UCrop.of<Uri>(item.uri, targetUri)
                .withOptions(crop)
                .start(host.context, host.fragment, REQUEST_CROP)
        } else if (host.activity != null) {
            UCrop.of<Uri>(item.uri, targetUri)
                .withOptions(crop)
                .start(host.activity, REQUEST_CROP)
        }
    }

    private fun compressFormat(postfix: String): Bitmap.CompressFormat {
        return when (postfix) {
            ".jpeg", ".jpg" -> Bitmap.CompressFormat.JPEG
            ".png" -> Bitmap.CompressFormat.PNG
            ".webp" -> Bitmap.CompressFormat.WEBP
            else -> Bitmap.CompressFormat.JPEG
        }
    }

    private fun getUCropResult(data: Intent): Uri? {
        val throwable = UCrop.getError(data)
        if (throwable != null) {
            Timber.e(throwable, "getUCropResult")
            return null
        }
        return UCrop.getOutput(data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(ORIGIN_ITEM_KEY, ArrayList(originContent))
        outState.putParcelableArrayList(PROCESSED_ITEM_KEY, ArrayList(processedContent))
        outState.putInt(PROGRESS_KEY, progress)
        Timber.d("onSaveInstanceState: progress=$progress")
    }

    override fun onRestoreInstanceState(outState: Bundle?) {
        outState?.let {
            originContent = BundleCompat.getParcelableArrayList(outState, ORIGIN_ITEM_KEY, MediaItem::class.java) ?: emptyList()
            processedContent = BundleCompat.getParcelableArrayList(outState, PROCESSED_ITEM_KEY, MediaItem::class.java) ?: mutableListOf()
            progress = outState.getInt(PROGRESS_KEY)
            Timber.d("onRestoreInstanceState: progress=$progress")
        }
    }

    companion object {
        private const val REQUEST_CROP = 10902

        private const val ORIGIN_ITEM_KEY = "crop_origin_item_key"
        private const val PROCESSED_ITEM_KEY = "crop_processed_item_key"
        private const val PROGRESS_KEY = "crop_progress_key"
    }

}