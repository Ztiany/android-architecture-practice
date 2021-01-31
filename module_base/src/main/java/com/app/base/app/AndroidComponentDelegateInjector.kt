package com.app.base.app

import android.app.Activity
import androidx.fragment.app.Fragment
import com.android.base.app.DelegateInjector
import com.android.base.foundation.activity.ActivityDelegateOwner
import com.android.base.foundation.fragment.FragmentDelegate
import com.android.base.foundation.fragment.FragmentDelegateOwner
import com.android.base.utils.android.SoftKeyboardUtils
import com.android.base.utils.common.ignoreCrash
import timber.log.Timber

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-11-27 10:48
 */
class AndroidComponentDelegateInjector : DelegateInjector {

    override fun injectFragmentDelegate(fragment: FragmentDelegateOwner) {
        Timber.d("inject Fragment Delegate for %s", fragment.toString())
        fragment.addDelegate(AutoHideSoftDelegate())
    }

    override fun injectActivityDelegate(activity: ActivityDelegateOwner) {

    }

}

private class AutoHideSoftDelegate : FragmentDelegate<Fragment> {

    private var activity: Activity? = null

    override fun onAttachToFragment(fragment: Fragment) {
        activity = fragment.activity
    }

    override fun onPause() {
        ignoreCrash {
            activity?.let {
                SoftKeyboardUtils.hideSoftInput(activity)
            }
        }
    }

}