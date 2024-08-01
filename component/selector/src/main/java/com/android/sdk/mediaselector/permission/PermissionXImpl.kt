package com.android.sdk.mediaselector.permission

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import timber.log.Timber

internal class PermissionXImpl : MediaPermissionRequester {

    override fun askForCameraPermission(
        activity: FragmentActivity,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
    ) {

        PermissionX.init(activity)
            .permissions(Manifest.permission.CAMERA)
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    Timber.d("askForCameraPermission, All permissions are granted")
                    onGranted()
                } else {
                    Timber.d("askForCameraPermission, These permissions are denied: $deniedList")
                    onDenied()
                }
            }
    }

    override fun askForReadStoragePermissionWhenUsingBuiltinPicker(activity: FragmentActivity, onGranted: () -> Unit, onDenied: () -> Unit) {
        // Scoped storage is enabled in Android 10 (API level 29) and higher.
        if (!activity.isLegacyExternalStorageEnabled() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Timber.d("Build.VERSION_CODES.Q: askMediaPermissionWhenUsingSAF")
            onGranted()
            return
        }

        PermissionX.init(activity)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    Timber.d("askForReadStoragePermissionWhenUsingBuiltinPicker, All permissions are granted")
                    onGranted()
                } else {
                    Timber.d("askForReadStoragePermissionWhenUsingBuiltinPicker, These permissions are denied: $deniedList")
                    onDenied()
                }
            }
    }

    override fun askForMediaPermissionWhenUsingMediaStoreAPI(
        activity: FragmentActivity,
        onGranted: (granted: List<String>, denied: List<String>) -> Unit,
        onDenied: () -> Unit,
    ) {

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE /*Android 14, API 34*/) {
            Timber.d("Build.VERSION_CODES.UPSIDE_DOWN_CAKE")
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU /*Android 13, API 33*/) {
            Timber.d("Build.VERSION_CODES.TIRAMISU")
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q/*Android 10, API 29*/) {
            Timber.d("Build.VERSION_CODES.Q")
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else /* Old */ {
            Timber.d("Build.VERSION_CODES.OLD")
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        PermissionX.init(activity)
            .permissions(*permissions)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Timber.d("askMediaPermissionWhenUsingMediaAPI, All permissions are granted")
                    onGranted(permissions.toList(), emptyList())
                } else {
                    Timber.d("askMediaPermissionWhenUsingMediaAPI, These permissions are denied: $deniedList")
                    onGranted(grantedList, deniedList)
                }
            }
    }

}