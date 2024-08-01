package com.android.sdk.mediaselector.processor

import com.android.sdk.mediaselector.ComponentStateHandler
import com.android.sdk.mediaselector.MediaItem

interface Processor : ComponentStateHandler {

    fun onAttachToChain(processorChain: ProcessorChain)

    fun start(params: List<MediaItem>)

}