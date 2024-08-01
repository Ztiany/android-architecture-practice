package com.android.sdk.mediaselector

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.base.delegate.activity.ActivityDelegate
import com.android.base.delegate.activity.ActivityDelegateOwner
import com.android.base.delegate.fragment.FragmentDelegate
import com.android.base.delegate.fragment.FragmentDelegateOwner

internal fun autoCallback(activity: Activity, stateHandler: ComponentStateHandler) {
    if (!hasActivityDelegateOwner()) {
        return
    }
    if (activity is ActivityDelegateOwner) {
        (activity as ActivityDelegateOwner).addDelegate(object : ActivityDelegate<AppCompatActivity> {
            override fun onSaveInstanceState(savedInstanceState: Bundle) {
                stateHandler.onSaveInstanceState(savedInstanceState)
            }

            override fun onRestoreInstanceState(savedInstanceState: Bundle) {
                stateHandler.onRestoreInstanceState(savedInstanceState)
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                stateHandler.onActivityResult(requestCode, resultCode, data)
            }
        })
    }
}

internal fun autoCallback(fragment: Fragment, stateHandler: ComponentStateHandler) {
    if (!hasFragmentDelegateOwner()) {
        return
    }
    if (fragment is FragmentDelegateOwner) {
        (fragment as FragmentDelegateOwner).addDelegate(object : FragmentDelegate<Fragment> {
            override fun onActivityCreated(savedInstanceState: Bundle?) {
                stateHandler.onRestoreInstanceState(savedInstanceState)
            }

            override fun onSaveInstanceState(savedInstanceState: Bundle) {
                stateHandler.onSaveInstanceState(savedInstanceState)
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                stateHandler.onActivityResult(requestCode, resultCode, data)
            }
        })
    }
}

private var hasActivityDelegateOwner = false
private var hasFragmentDelegateOwner = false

private fun hasActivityDelegateOwner(): Boolean {
    if (hasActivityDelegateOwner) {
        return true
    }
    return try {
        Class.forName("com.android.base.delegate.activity.ActivityDelegateOwner")
        hasActivityDelegateOwner = true
        true
    } catch (e: Exception) {
        false
    }
}

private fun hasFragmentDelegateOwner(): Boolean {
    if (hasFragmentDelegateOwner) {
        return true
    }
    return try {
        Class.forName("com.android.base.delegate.fragment.FragmentDelegateOwner")
        hasFragmentDelegateOwner = true
        true
    } catch (e: Exception) {
        false
    }
}
