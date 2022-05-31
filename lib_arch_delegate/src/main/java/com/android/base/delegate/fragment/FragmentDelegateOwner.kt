package com.android.base.delegate.fragment

import androidx.annotation.UiThread

@UiThread
interface FragmentDelegateOwner {

    fun addDelegate(fragmentDelegate: FragmentDelegate<*>)

    fun removeDelegate(fragmentDelegate: FragmentDelegate<*>): Boolean

    fun findDelegate(predicate: (FragmentDelegate<*>)->Boolean): FragmentDelegate<*>?

}