package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.picker.SAFPicker
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.utils.MineType

class FilePicker() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var multiple = false

    private var types = listOf(MineType.ALL.value)

    fun multiple(): FilePicker {
        this.multiple = true
        return this
    }

    fun type(vararg types: String): FilePicker {
        this.types = types.toList()
        return this
    }

    fun start() {
        builtInSelector?.start(this)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return buildList {
            add(SAFPicker(host, types, multiple))
        }
    }

    constructor(parcel: Parcel) : this() {
        multiple = parcel.readByte() != 0.toByte()
        types = parcel.createStringArrayList() ?: emptyList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (multiple) 1 else 0)
        parcel.writeStringList(types)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilePicker> {
        override fun createFromParcel(parcel: Parcel): FilePicker {
            return FilePicker(parcel)
        }

        override fun newArray(size: Int): Array<FilePicker?> {
            return arrayOfNulls(size)
        }
    }

}