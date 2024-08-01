package com.app.base.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.android.base.delegate.activity.ActivityDelegateOwner
import com.android.base.delegate.fragment.FragmentDelegateOwner
import com.android.base.utils.android.SoftKeyboardUtils
import com.android.base.utils.android.adaption.ActivityLifecycleCallbacksAdapter
import com.android.base.utils.common.ignoreCrash
import com.app.base.utils.setStatusBarLightMode
import com.app.common.api.protocol.CustomizeSystemBar

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-11-27 10:48
 */
class ComponentProcessor : ActivityLifecycleCallbacksAdapter {

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPreCreated(activity, savedInstanceState)
        handleAutoInstallActivityDelegate(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //common ui bar
        configActivityBar(activity)
        //inject
        if (activity is FragmentActivity) {
            injectFragmentLifecycle(activity)
        }
    }

    private fun configActivityBar(activity: Activity) {
        if (activity is CustomizeSystemBar) {
            return
        }
        activity.setStatusBarLightMode()
    }

    private fun injectFragmentLifecycle(activity: FragmentActivity) {
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentAttached(
                fm: FragmentManager,
                fragment: Fragment,
                context: Context
            ) {
                handleAutoInstallFragmentDelegate(fragment)
            }

            override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentViewDestroyed(fm, f)
                ignoreCrash {
                    f.activity?.let {
                        SoftKeyboardUtils.hideSoftInput(activity)
                    }
                }
            }

        }, true)
    }

    private fun handleAutoInstallFragmentDelegate(fragment: Fragment) {
        if (fragment is FragmentDelegateOwner) {
            //nothing to do
        }
    }

    private fun handleAutoInstallActivityDelegate(activity: Activity?) {
        if (activity is ActivityDelegateOwner) {
            //nothing to do
        }
    }

}