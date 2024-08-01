package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.crop.CropOptions
import com.android.sdk.mediaselector.processor.crop.CropProcessor
import com.android.sdk.mediaselector.processor.picker.GetContentPicker
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.utils.MineType

class GetImageContent() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var multiple = false

    private var cropOptions: CropOptions? = null

    fun crop(cropOptions: CropOptions = CropOptions()): GetImageContent {
        this.cropOptions = cropOptions
        return this
    }

    fun multiple(): GetImageContent {
        this.multiple = true
        return this
    }

    override fun start(scene: String) {
        builtInSelector?.start(this, scene)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return buildList {
            add(GetContentPicker(host, MineType.IMAGE.value, multiple))
            cropOptions?.let { add(CropProcessor(host, it)) }
        }
    }

    constructor(parcel: Parcel) : this() {
        multiple = parcel.readByte() != 0.toByte()
        cropOptions = ParcelCompat.readParcelable(parcel, CropOptions::class.java.classLoader, CropOptions::class.java)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (multiple) 1 else 0)
        parcel.writeParcelable(cropOptions, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetImageContent> {
        override fun createFromParcel(parcel: Parcel): GetImageContent {
            return GetImageContent(parcel)
        }

        override fun newArray(size: Int): Array<GetImageContent?> {
            return arrayOfNulls(size)
        }
    }

}