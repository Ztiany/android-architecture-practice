package com.android.sdk.mediaselector.common

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

internal class ActFragWrapper private constructor(
    val fragment: Fragment?,
    val activity: Activity?
) {

    val context: Context
        get() = activity ?: (fragment?.requireContext() ?: throw IllegalStateException("never happen."))

    fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode, options)
        } else fragment?.startActivityForResult(intent, requestCode, options)
    }

    fun startService(intent: Intent) {
        if (activity != null) {
            activity.startService(intent)
        } else fragment?.activity?.startService(intent)
    }

    fun stopService(clazz: Class<out Service?>?) {
        if (activity != null) {
            activity.stopService(Intent(activity, clazz))
        } else fragment?.activity?.let {
            it.stopService(Intent(it, clazz))
        }
    }

    companion object {

        fun create(activity: Activity): ActFragWrapper {
            return ActFragWrapper(activity = activity, fragment = null)
        }

        fun create(fragment: Fragment?): ActFragWrapper {
            return ActFragWrapper(fragment = fragment, activity = null)
        }

    }

}