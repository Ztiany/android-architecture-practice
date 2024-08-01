package com.android.sdk.mediaselector

import android.os.Parcelable
import com.android.sdk.mediaselector.processor.Processor

interface Action : Parcelable {

    fun assembleProcessors(host: ActFragWrapper): List<Processor>

    fun start(scene: String = "default")

}
