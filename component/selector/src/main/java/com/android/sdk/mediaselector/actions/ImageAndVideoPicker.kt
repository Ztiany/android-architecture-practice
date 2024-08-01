package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.ParcelCompat
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.crop.CropOptions
import com.android.sdk.mediaselector.processor.crop.CropProcessor
import com.android.sdk.mediaselector.processor.picker.SAFPicker
import com.android.sdk.mediaselector.processor.picker.VisualMediaPicker
import com.android.sdk.mediaselector.utils.MineType

class ImageAndVideoPicker() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var count: Int = 1

    private var cropOptions: CropOptions? = null

    private var useSAF = false
    private var takePersistentUriPermission = false

    fun count(count: Int): ImageAndVideoPicker {
        this.count = count
        return this
    }

    fun crop(cropOptions: CropOptions = CropOptions()): ImageAndVideoPicker {
        this.cropOptions = cropOptions
        return this
    }

    override fun start(scene: String) {
        builtInSelector?.start(this, scene)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return buildList {
            if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(host.context) && !useSAF && !takePersistentUriPermission) {
                add(VisualMediaPicker(host, ActivityResultContracts.PickVisualMedia.ImageAndVideo, count))
            } else {
                add(SAFPicker(host, listOf(MineType.IMAGE.value, MineType.VIDEO.value), takePersistentUriPermission, count > 1))
            }
            cropOptions?.let { add(CropProcessor(host, it)) }
        }
    }

    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        cropOptions = ParcelCompat.readParcelable(parcel, CropOptions::class.java.classLoader, CropOptions::class.java)
        useSAF = parcel.readByte() != 0.toByte()
        takePersistentUriPermission = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeParcelable(cropOptions, flags)
        useSAF = parcel.readByte() != 0.toByte()
        takePersistentUriPermission = parcel.readByte() != 0.toByte()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageAndVideoPicker> {
        override fun createFromParcel(parcel: Parcel): ImageAndVideoPicker {
            return ImageAndVideoPicker(parcel)
        }

        override fun newArray(size: Int): Array<ImageAndVideoPicker?> {
            return arrayOfNulls(size)
        }
    }

}