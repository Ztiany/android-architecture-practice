package com.android.sdk.mediaselector.processor

import android.content.Intent
import android.os.Bundle

abstract class BaseProcessor : Processor {

    /**
     * The processor chain is used to pass the result of the current processor to the next processor.
     */
    protected lateinit var processorChain: ProcessorChain

    final override fun onAttachToChain(processorChain: ProcessorChain) {
        this.processorChain = processorChain
    }

    override fun onSaveInstanceState(outState: Bundle) = Unit

    override fun onRestoreInstanceState(outState: Bundle?) = Unit

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = Unit

}