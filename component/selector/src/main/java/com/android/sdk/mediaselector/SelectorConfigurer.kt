package com.android.sdk.mediaselector

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.R
import com.android.sdk.mediaselector.imageloader.GlideImageLoader
import com.android.sdk.mediaselector.imageloader.ImageLoader
import com.android.sdk.mediaselector.permission.MediaPermissionRequester
import com.android.sdk.mediaselector.permission.PermissionXImpl
import com.android.sdk.mediaselector.serialization.Serializer
import com.google.gson.Gson
import timber.log.Timber

class SelectorConfigurer private constructor() {

    fun setAuthority(authority: String) {
        mediaAuthority = authority
    }

    fun setPermissionRequester(requester: MediaPermissionRequester) {
        mediaPermissionRequester = requester
    }

    fun setImageLoader(loader: ImageLoader) {
        imageLoader = loader
    }

    fun setCropPrimaryColorAttr(@AttrRes color: Int) {
        cropPrimaryColorAttr = color
    }

    fun setCropTextColorAttr(@AttrRes color: Int) {
        cropTextColorAttr = color
    }

    fun setSerializer(serializer: Serializer) {
        objSerializer = serializer
    }

    companion object {
        fun setUp(config: SelectorConfigurer.() -> Unit) {
            SelectorConfigurer().apply(config)
            Timber.d("SelectorConfigurer setUp!")
        }
    }

}

private var mediaAuthority: String = ""
private var cropPrimaryColorAttr: Int = R.attr.colorPrimary
private var cropTextColorAttr: Int = R.attr.titleTextColor

private var imageLoader: ImageLoader? = null
private var objSerializer: Serializer? = null
private var mediaPermissionRequester: MediaPermissionRequester? = null

internal fun getConfiguredAuthority(context: Context): String {
    if (mediaAuthority.isNotEmpty()) {
        return mediaAuthority
    }
    return context.packageName + ".file.provider"
}

@ColorInt
internal fun Context.getConfiguredCropPrimaryColor(): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(cropPrimaryColorAttr, outValue, true)
    return outValue.data
}

@ColorInt
internal fun Context.getConfiguredCropTextColor(): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(cropTextColorAttr, outValue, true)
    return outValue.data
}

internal fun getPermissionRequester(): MediaPermissionRequester {
    if (mediaPermissionRequester == null) {
        mediaPermissionRequester = PermissionXImpl()
    }
    return mediaPermissionRequester ?: throw AssertionError("impossible!")
}

internal fun getImageLoader(): ImageLoader {
    if (imageLoader == null) {
        imageLoader = GlideImageLoader()
    }
    return imageLoader ?: throw AssertionError("impossible!")
}

internal fun getSerializer(): Serializer {
    if (objSerializer == null) {
        val gson = Gson()
        objSerializer = object : Serializer {
            override fun serialize(any: Any) = gson.toJson(any)
        }
    }
    return objSerializer ?: throw AssertionError("impossible!")
}