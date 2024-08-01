package com.android.sdk.mediaselector.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import timber.log.Timber

fun Uri.setRequireOriginal(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            MediaStore.setRequireOriginal(this)
        } else {
            Timber.w("You don't have the permission ACCESS_MEDIA_LOCATION to access media location.")
        }
    }
}