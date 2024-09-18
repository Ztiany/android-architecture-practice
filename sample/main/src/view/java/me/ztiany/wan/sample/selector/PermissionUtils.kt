package me.ztiany.wan.sample.selector

import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import timber.log.Timber

internal fun AppCompatActivity.askMediaLocationPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q/*Android 10, API 29*/) {
        PermissionX.init(this)
            .permissions(ACCESS_MEDIA_LOCATION)
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    Timber.d("askMediaLocationPermission, All permissions are granted")
                    onGranted()
                } else {
                    onDenied()
                    Timber.d("askMediaLocationPermission, These permissions are denied: $deniedList")
                }
            }
    }
}