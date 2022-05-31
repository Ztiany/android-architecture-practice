package com.android.base.architecture.ui.viewbniding

import android.view.View
import androidx.viewbinding.ViewBinding

/** This class can be used when there is no xml layout to generate a ViewBinding instance. */
abstract class AbsViewBinding<out T : View>(private val rootView: T) : ViewBinding {

    override fun getRoot(): View {
        return rootView
    }

}