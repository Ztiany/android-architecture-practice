package com.android.sdk.mediaselector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

class ActFragWrapper private constructor(
    val fragment: Fragment?,
    val activity: FragmentActivity?,
) {

    val context: Context
        get() = activity ?: (fragment?.requireContext() ?: throw IllegalStateException("never happen."))

    val fragmentActivity: FragmentActivity
        get() = activity ?: fragment?.requireActivity() ?: throw IllegalStateException("never happen.")

    val scope: CoroutineScope
        get() = activity?.lifecycleScope ?: fragment?.lifecycleScope ?: throw IllegalStateException("never happen.")

    val lifecycleOwner: LifecycleOwner
        get() = activity ?: fragment ?: throw IllegalStateException("never happen.")

    fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle? = null) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode, options)
        } else fragment?.startActivityForResult(intent, requestCode, options)
    }

    companion object {

        fun create(activity: FragmentActivity): ActFragWrapper {
            return ActFragWrapper(activity = activity, fragment = null)
        }

        fun create(fragment: Fragment?): ActFragWrapper {
            return ActFragWrapper(fragment = fragment, activity = null)
        }

    }

}