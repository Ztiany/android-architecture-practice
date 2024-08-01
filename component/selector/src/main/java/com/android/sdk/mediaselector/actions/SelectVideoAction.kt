package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.selector.MediaStoreSelectorConfig
import com.android.sdk.mediaselector.processor.selector.MediaStoreSelectorProcessor
import com.luck.picture.lib.config.SelectMimeType

class SelectVideoAction() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var count = 1
    private var showCamera = false
    private var maxFileSize: Long = 0
    private var minFileSize: Long = 0
    private var maxDurationSecond: Int = 0
    private var minDurationSecond: Int = 0
    private var selectedData: List<String> = emptyList()

    fun count(count: Int): SelectVideoAction {
        this.count = count
        return this
    }

    fun showCamera(): SelectVideoAction {
        this.showCamera = true
        return this
    }

    /**
     * [min] and [max] are in kb.
     */
    fun sizeRange(min: Long = 0, max: Long = 0): SelectVideoAction {
        this.minFileSize = min
        this.maxFileSize = max
        return this
    }

    /**
     * [min] and [max] are in second.
     */
    fun durationRange(min: Int = 0, max: Int = 0): SelectVideoAction {
        this.minDurationSecond = min
        this.maxDurationSecond = max
        return this
    }

    fun selectedData(selectedData: List<String>): SelectVideoAction {
        this.selectedData = selectedData
        return this
    }

    fun start() {
        builtInSelector?.start(this)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return buildList {
            add(MediaStoreSelectorProcessor(host, createConfig()))
        }
    }

    private fun createConfig(): MediaStoreSelectorConfig {
        return MediaStoreSelectorConfig(
            mineType = SelectMimeType.ofVideo(),
            count = count,
            showCamera = showCamera,
            maxFileSize = maxFileSize,
            minFileSize = minFileSize,
            maxDurationSecond = maxDurationSecond,
            minDurationSecond = minDurationSecond,
            selectedData = selectedData
        )
    }

    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        showCamera = parcel.readByte() != 0.toByte()
        maxFileSize = parcel.readLong()
        minFileSize = parcel.readLong()
        maxDurationSecond = parcel.readInt()
        minDurationSecond = parcel.readInt()
        selectedData = parcel.createStringArrayList() ?: emptyList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeByte(if (showCamera) 1 else 0)
        parcel.writeLong(maxFileSize)
        parcel.writeLong(minFileSize)
        parcel.writeInt(maxDurationSecond)
        parcel.writeInt(minDurationSecond)
        parcel.writeStringList(selectedData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectVideoAction> {
        override fun createFromParcel(parcel: Parcel): SelectVideoAction {
            return SelectVideoAction(parcel)
        }

        override fun newArray(size: Int): Array<SelectVideoAction?> {
            return arrayOfNulls(size)
        }
    }

}