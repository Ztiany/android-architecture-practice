package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.picker.GetContentPicker
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.utils.MineType

class GetContent() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var multiple = false

    private var type: String = MineType.ALL.value

    fun multiple(): GetContent {
        this.multiple = true
        return this
    }

    fun type(type: String): GetContent {
        this.type = type
        return this
    }

    fun start() {
        builtInSelector?.start(this)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return listOf(
            GetContentPicker(host, type, multiple)
        )
    }

    constructor(parcel: Parcel) : this() {
        multiple = parcel.readByte() != 0.toByte()
        type = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (multiple) 1 else 0)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetContent> {
        override fun createFromParcel(parcel: Parcel): GetContent {
            return GetContent(parcel)
        }

        override fun newArray(size: Int): Array<GetContent?> {
            return arrayOfNulls(size)
        }
    }

}