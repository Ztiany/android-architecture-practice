package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.picker.GetContentPicker
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.utils.MineType

class GetVideoContent() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var multiple = false

    fun multiple(): GetVideoContent {
        this.multiple = true
        return this
    }

    fun start() {
        builtInSelector?.start(this)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return buildList {
            add(GetContentPicker(host, MineType.VIDEO.value, multiple))
        }
    }

    constructor(parcel: Parcel) : this() {
        multiple = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (multiple) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetVideoContent> {
        override fun createFromParcel(parcel: Parcel): GetVideoContent {
            return GetVideoContent(parcel)
        }

        override fun newArray(size: Int): Array<GetVideoContent?> {
            return arrayOfNulls(size)
        }
    }

}