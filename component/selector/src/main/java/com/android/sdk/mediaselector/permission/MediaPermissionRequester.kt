package com.android.sdk.mediaselector.permission

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import androidx.fragment.app.FragmentActivity

interface MediaPermissionRequester {

    /**
     * This method is used when the app needs to access the camera.
     */
    fun askForCameraPermission(
        activity: FragmentActivity,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
    )

    /**
     * This method is used when the app needs to access the media files with SAF.
     *
     * Because of the introduction of scoped storage in Android 10, wn don't need any permission to access media files with SAF.
     *
     * But for devices running on Android 9 or lower, we need to ask for the [READ_EXTERNAL_STORAGE] permission.
     *
     * This method will be called only when the app is running on Android 9 or lower.
     */
    fun askForReadStoragePermissionWhenUsingBuiltinPicker(activity: FragmentActivity, onGranted: () -> Unit, onDenied: () -> Unit)

    /**
     * This method is used when the app needs to access the media files with MediaStore API.
     */
    fun askForMediaPermissionWhenUsingMediaStoreAPI(
        activity: FragmentActivity,
        onGranted: (granted: List<String>, denied: List<String>) -> Unit,
        onDenied: () -> Unit,
    )

}