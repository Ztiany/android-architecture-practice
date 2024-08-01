package com.android.sdk.mediaselector

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.base.delegate.activity.ActivityDelegateOwner
import com.android.base.delegate.fragment.FragmentDelegateOwner
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.compress.CompressionConfig
import com.android.sdk.mediaselector.processor.compress.ImageCompressionProcessor
import com.android.sdk.mediaselector.processor.copy.CopyToAppSpecificProcessor

typealias ResultHandlerWithScene = (scene: String, results: List<MediaItem>) -> Unit
typealias ResultHandler = (results: List<MediaItem>) -> Unit


/**
 * If your fragment has implemented [FragmentDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany.
 */
fun Fragment.buildMediaSelector(block: MediaSelectorBuilder.() -> Unit): MediaSelector {
    return MediaSelectorBuilder(ActFragWrapper.create(this)).apply(block).setUp()
}

/**
 * If your activity has implemented [ActivityDelegateOwner], you don't need to call methods in [ComponentStateHandler].
 *
 *@author Ztiany
 */
fun FragmentActivity.buildMediaSelector(block: MediaSelectorBuilder.() -> Unit): MediaSelector {
    return MediaSelectorBuilder(ActFragWrapper.create(this)).apply(block).setUp()
}

class MediaSelectorBuilder internal constructor(private val actFragWrapper: ActFragWrapper) {

    private val postProcessors = mutableListOf<Processor>()

    private var cancellationHandler: (() -> Unit)? = null

    private var resultHandler: ResultHandlerWithScene? = null

    fun withProcessor(vararg processor: Processor) {
        postProcessors.addAll(processor)
    }

    fun onCanceled(cancellationHandler: () -> Unit) {
        this.cancellationHandler = cancellationHandler
    }

    fun onResultsWithScene(resultHandler: ResultHandlerWithScene) {
        this.resultHandler = resultHandler
    }

    fun onResults(resultHandler: ResultHandler) {
        this.resultHandler = { _, results -> resultHandler(results) }
    }

    fun onResult(resultHandler: (MediaItem) -> Unit) {
        this.resultHandler = { _, results -> resultHandler(results.first()) }
    }

    fun onResultWithScene(resultHandler: (String, MediaItem) -> Unit) {
        this.resultHandler = { scene, results -> resultHandler(scene, results.first()) }
    }

    internal fun setUp(): MediaSelector {
        return MediaSelectorImpl(actFragWrapper, postProcessors, object : ResultListener {
            override fun onCanceled() {
                cancellationHandler?.invoke()
            }

            override fun onResult(scene: String, items: List<MediaItem>) {
                resultHandler?.invoke(scene, items)
            }
        }).also { selector ->
            actFragWrapper.activity?.let {
                autoCallback(it, selector)
            } ?: actFragWrapper.fragment?.let {
                autoCallback(it, selector)
            }
        }
    }

    /**
     * 拷贝文件到应用私有目录。
     */
    fun appSpecificFileTransfer(): Processor {
        return CopyToAppSpecificProcessor(actFragWrapper)
    }

    /**
     * 对图片进行压缩处理。
     */
    fun imageCompressor(config: CompressionConfig = CompressionConfig()): Processor {
        return ImageCompressionProcessor(actFragWrapper, config)
    }

}