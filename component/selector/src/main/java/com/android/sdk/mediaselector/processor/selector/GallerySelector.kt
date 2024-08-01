package com.android.sdk.mediaselector.processor.selector

import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.getSerializer
import com.android.sdk.mediaselector.processor.BaseProcessor
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import timber.log.Timber

/**
 * Maybe we will need this in the future.
 */
internal class GalleryPicker(
    private val host: ActFragWrapper,
    private val config: GalleryPickerConfig,
) : BaseProcessor() {

    override fun start(params: List<MediaItem>) {
        if (host.activity != null) {
            PictureSelector.create(host.activity)
        } else {
            PictureSelector.create(host.fragment)
        }
            .openSystemGallery(config.mineType)
            .setSelectMaxFileSize(config.maxFileSize)
            .setSelectMinFileSize(config.minFileSize)
            .setSelectMaxDurationSecond(config.maxDurationSecond)
            .setSelectMinDurationSecond(config.minDurationSecond)
            .forSystemResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    handleResult(result)
                }

                override fun onCancel() {
                    processorChain.onCanceled()
                }
            })
    }

    private fun handleResult(result: ArrayList<LocalMedia>) {
        result.forEach {
            Timber.d("handleResult: ${getSerializer().serialize(it)}")
        }
        processorChain.onResult(result.map { it.toItem(true) })
    }

}