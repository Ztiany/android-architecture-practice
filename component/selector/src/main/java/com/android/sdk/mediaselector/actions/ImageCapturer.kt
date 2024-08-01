package com.android.sdk.mediaselector.actions

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import com.android.sdk.mediaselector.ActFragWrapper
import com.android.sdk.mediaselector.Action
import com.android.sdk.mediaselector.MediaSelectorImpl
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.capture.CaptureProcessor
import com.android.sdk.mediaselector.processor.crop.CropOptions
import com.android.sdk.mediaselector.processor.crop.CropProcessor
import com.android.sdk.mediaselector.utils.createInternalPath

class ImageCapturer() : Action {

    internal var builtInSelector: MediaSelectorImpl? = null

    private var cropOptions: CropOptions? = null

    private var savePath: String = ""

    fun crop(cropOptions: CropOptions = CropOptions()): ImageCapturer {
        this.cropOptions = cropOptions
        return this
    }

    fun saveTo(savePath: String): ImageCapturer {
        if (savePath.isEmpty()) {
            throw IllegalArgumentException("savePath cannot be empty")
        }
        this.savePath = savePath
        return this
    }

    override fun start(scene: String) {
        builtInSelector?.start(this, scene)
    }

    override fun assembleProcessors(host: ActFragWrapper): List<Processor> {
        if (savePath.isEmpty()) {
            savePath = host.context.createInternalPath(".jpeg")
        }

        return buildList {
            add(CaptureProcessor(host, CaptureProcessor.IMAGE, savePath))
            cropOptions?.let { add(CropProcessor(host, it)) }
        }
    }

    constructor(parcel: Parcel) : this() {
        cropOptions = ParcelCompat.readParcelable(parcel, CropOptions::class.java.classLoader, CropOptions::class.java)
        savePath = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cropOptions, flags)
        parcel.writeString(savePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageCapturer> {
        override fun createFromParcel(parcel: Parcel): ImageCapturer {
            return ImageCapturer(parcel)
        }

        override fun newArray(size: Int): Array<ImageCapturer?> {
            return arrayOfNulls(size)
        }
    }

}