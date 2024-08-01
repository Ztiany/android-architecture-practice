package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.crop.CropOptions
import com.android.sdk.mediaselector.processor.crop.CropProcessor
import com.android.sdk.mediaselector.processor.selector.MediaStoreSelectorConfig
import com.android.sdk.mediaselector.processor.selector.MediaStoreSelectorProcessor
import com.luck.picture.lib.config.SelectMimeType

class SelectImageAction() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var count = 1
    private var showCamera = false
    private var includeGif: Boolean = false
    private var maxFileSize: Long = 0
    private var minFileSize: Long = 0
    private var selectedData: List<String> = emptyList()
    private var cropOptions: CropOptions? = null

    fun crop(cropOptions: CropOptions = CropOptions()): SelectImageAction {
        this.cropOptions = cropOptions
        return this
    }

    fun count(count: Int): SelectImageAction {
        this.count = count
        return this
    }

    fun includeGif(): SelectImageAction {
        this.includeGif = true
        return this
    }

    fun enableCamera(): SelectImageAction {
        this.showCamera = true
        return this
    }

    /**
     * [min] and [max] are in kb.
     */
    fun sizeRange(min: Long = 0, max: Long = 0): SelectImageAction {
        this.minFileSize = min
        this.maxFileSize = max
        return this
    }

    fun selectedData(selectedData: List<String>): SelectImageAction {
        this.selectedData = selectedData
        return this
    }

    override fun start(scene: String) {
        builtInSelector?.start(this, scene)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return buildList {
            add(MediaStoreSelectorProcessor(host, createConfig()))
            cropOptions?.let { add(CropProcessor(host, it)) }
        }
    }

    private fun createConfig(): MediaStoreSelectorConfig {
        return MediaStoreSelectorConfig(
            mineType = SelectMimeType.ofImage(),
            count = count,
            showCamera = showCamera,
            includeGif = includeGif,
            maxFileSize = maxFileSize,
            minFileSize = minFileSize,
            selectedData = selectedData
        )
    }

    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        showCamera = parcel.readByte() != 0.toByte()
        includeGif = parcel.readByte() != 0.toByte()
        maxFileSize = parcel.readLong()
        minFileSize = parcel.readLong()
        selectedData = parcel.createStringArrayList() ?: emptyList()
        cropOptions = ParcelCompat.readParcelable(parcel, CropOptions::class.java.classLoader, CropOptions::class.java)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeByte(if (showCamera) 1 else 0)
        parcel.writeByte(if (includeGif) 1 else 0)
        parcel.writeLong(maxFileSize)
        parcel.writeLong(minFileSize)
        parcel.writeStringList(selectedData)
        parcel.writeParcelable(cropOptions, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectImageAction> {
        override fun createFromParcel(parcel: Parcel): SelectImageAction {
            return SelectImageAction(parcel)
        }

        override fun newArray(size: Int): Array<SelectImageAction?> {
            return arrayOfNulls(size)
        }
    }

}