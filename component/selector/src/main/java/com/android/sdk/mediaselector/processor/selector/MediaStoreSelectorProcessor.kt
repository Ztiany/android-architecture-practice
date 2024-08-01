package com.android.sdk.mediaselector.processor.selector

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.getSerializer
import com.android.sdk.mediaselector.processor.BaseProcessor
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import timber.log.Timber

private val selectedLocalMedia = mutableMapOf<LifecycleOwner, MutableSet<LocalMedia>>()

internal class MediaStoreSelectorProcessor(
    private val host: ActFragWrapper,
    private val config: MediaStoreSelectorConfig,
) : BaseProcessor() {

    init {
        host.lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                selectedLocalMedia.remove(owner)
            }
        })
    }

    override fun start(params: List<MediaItem>) {
        /*
        we don't need it, because PictureSelector will handle the Dynamic permissions itself.
        if (host.fragmentActivity.getPermissionState() != MediaPermissionState.None) {
            start()
        } else {
            getPermissionRequester().askForMediaPermissionWhenUsingMediaStoreAPI(
                host.fragmentActivity,
                onGranted = { _, _ -> start() },
                onDenied = { processorChain.onCanceled() }
            )
        }*/
        start()
    }

    private fun start() {
        if (host.activity != null) {
            PictureSelector.create(host.activity)
        } else {
            PictureSelector.create(host.fragment)
        }
            .openGallery(config.mineType)
            .setMaxSelectNum(config.count)
            .setImageEngine(InternalEngine())
            .isWithSelectVideoImage(config.imageAndVideo)
            .isGif(config.includeGif)
            .isDisplayTimeAxis(true)
            .isDisplayCamera(config.showCamera)
            .setSelectMaxFileSize(config.maxFileSize)
            .setSelectMinFileSize(config.minFileSize)
            .setSelectMaxDurationSecond(config.maxDurationSecond)
            .setSelectMinDurationSecond(config.minDurationSecond)
            .isPreloadFirst(true)
            .setSelectedData(findSelected())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    handleResult(result)
                }

                override fun onCancel() {
                    processorChain.onCanceled()
                }
            })
    }

    private fun findSelected(): List<LocalMedia> {
        val totalSelected = selectedLocalMedia[host.lifecycleOwner] ?: return emptyList()
        Timber.d("current selected: ${config.selectedData}")
        Timber.d("total selected: ${totalSelected.map { it.id }}")
        return config.selectedData.mapNotNull { id ->
            totalSelected.find { it.id.toString() == id }
        }
    }

    private fun handleResult(result: ArrayList<LocalMedia>) {
        selectedLocalMedia.getOrPut(host.lifecycleOwner) { mutableSetOf() }.addAll(result)
        result.forEach {
            Timber.d("handleResult: ${getSerializer().serialize(it)}")
        }
        processorChain.onResult(result.map { it.toItem(false) })
    }

}