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
    private var mineType = ""
    private var useSAF = false
    private var takePersistentUriPermission = false

    fun count(count: Int): VideoPicker {
        this.count = count
        return this
    }

    fun restrictTypeTo(mineType: String): VideoPicker {
        if (!mineType.startsWith("video/")) {
            throw IllegalArgumentException("Type must be a video mime type")
        }
        this.mineType = mineType
        return this
    }

    fun useSAF(useSAF: Boolean): VideoPicker {
        this.useSAF = useSAF
        return this
    }

    fun takePersistentUriPermission(takePersistentUriPermission: Boolean): VideoPicker {
        this.takePersistentUriPermission = takePersistentUriPermission
        return this
    }

    override fun start(scene: String) {
        builtInSelector?.start(this, scene)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        return buildList {
            if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(host.context) && !useSAF && !takePersistentUriPermission) {
                val visualType = if (mineType.isEmpty()) {
                    ActivityResultContracts.PickVisualMedia.VideoOnly
                } else ActivityResultContracts.PickVisualMedia.SingleMimeType(mineType)
                add(VisualMediaPicker(host, visualType, count))
            } else {
                add(SAFPicker(host, listOf(mineType.takeIf { it.isNotEmpty() } ?: MineType.VIDEO.value), takePersistentUriPermission, count > 1))
            }
        }
    }

    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        mineType = parcel.readString() ?: ""
        useSAF = parcel.readByte() != 0.toByte()
        takePersistentUriPermission = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeString(mineType)
        parcel.writeByte(if (useSAF) 1 else 0)
        parcel.writeByte(if (takePersistentUriPermission) 1 else 0)
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