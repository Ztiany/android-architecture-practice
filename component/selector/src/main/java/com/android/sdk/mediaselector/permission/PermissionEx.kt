package com.android.sdk.mediaselector.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import timber.log.Timber

internal fun FragmentActivity.isLegacyExternalStorageEnabled(): Boolean {
    val packageManager = packageManager
    val packageName = packageName

    val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    return if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
        // For Android Q (API level 29), requestLegacyExternalStorage is always false unless explicitly set in the manifest.
        // For Android Q After (API level 30 or higher), requestLegacyExternalStorage is always useless.
        applicationInfo.metaData?.getBoolean("android:requestLegacyExternalStorage", false) ?: false
    } else {
        // For versions before Android Q, requestLegacyExternalStorage is not applicable
        false
    }.also {
        Timber.d("isLegacyExternalStorageEnabled: $it")
    }
}

internal enum class MediaPermissionState {
    Full,
    Visual,
    None
}

internal fun FragmentActivity.getPermissionState(): MediaPermissionState {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        && (checkMediaPermission(Manifest.permission.READ_MEDIA_IMAGES) || checkMediaPermission(Manifest.permission.READ_MEDIA_VIDEO))
    ) {
        // Full access on Android 13 (API level 33) or higher
        MediaPermissionState.Full
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
        && checkMediaPermission(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
    ) {
        MediaPermissionState.Visual
    } else if (checkMediaPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
        // Full access up to Android 12 (API level 32)
        MediaPermissionState.Full
    } else {
        MediaPermissionState.None
    }
}

private fun FragmentActivity.checkMediaPermission(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED