package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContracts
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.picker.SAFPicker
import com.android.sdk.mediaselector.processor.picker.VisualMediaPicker
import com.android.sdk.mediaselector.utils.MineType

class VideoPicker() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var count: Int = 1
    private var type = ""

    fun count(count: Int): VideoPicker {
        this.count = count
        return this
    }

    fun restrictTypeTo(type: String): VideoPicker {
        if (!type.startsWith("video/")) {
            throw IllegalArgumentException("Type must be a video mime type")
        }
        this.type = type
        return this
    }

    override fun start(scene: String) {
        builtInSelector?.start(this, scene)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return buildList {
            if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(host.context)) {
                val visualType = if (type.isEmpty()) {
                    ActivityResultContracts.PickVisualMedia.VideoOnly
                } else ActivityResultContracts.PickVisualMedia.SingleMimeType(type)
                add(VisualMediaPicker(host, visualType, count))
            } else {
                add(SAFPicker(host, listOf(type.takeIf { it.isNotEmpty() } ?: MineType.VIDEO.value), count > 1))
            }
        }
    }

    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        type = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoPicker> {
        override fun createFromParcel(parcel: Parcel): VideoPicker {
            return VideoPicker(parcel)
        }

        override fun newArray(size: Int): Array<VideoPicker?> {
            return arrayOfNulls(size)
        }
    }

}