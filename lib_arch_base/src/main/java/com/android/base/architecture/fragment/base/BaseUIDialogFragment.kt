package com.android.base.architecture.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.android.base.AndroidSword
import com.android.base.architecture.fragment.tools.ReusableView
import com.android.base.architecture.fragment.tools.dismissDialog
import com.android.base.architecture.ui.loading.LoadingViewHost
import com.android.base.architecture.ui.viewbniding.inflateBindingWithParameterizedType

/**
 *@author Ztiany
 * @see BaseUIFragment
 */
abstract class BaseUIDialogFragment<VB : ViewBinding> : BaseDialogFragment(), LoadingViewHost {

    private var recentShowingDialogTime: Long = 0

    private var loadingViewHost: LoadingViewHost? = null

    private val reuseView by lazy { ReusableView() }

    private var _vb: VB? = null
    protected val vb: VB
        get() = checkNotNull(_vb) {
            "access this after onCreateView() is called."
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = {
            _vb = provideViewBinding(inflater, savedInstanceState) ?: inflateBindingWithParameterizedType(layoutInflater, container, false)
            vb.root
        }
        return reuseView.createView(factory)
    }

    protected open fun provideViewBinding(inflater: LayoutInflater, savedInstanceState: Bundle?): VB? = null

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (reuseView.isNotTheSameView(view)) {
            internalOnViewPrepared(view, savedInstanceState)
            onViewPrepared(view, savedInstanceState)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    internal open fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {}

    /**
     * View is prepared, If [androidx.fragment.app.Fragment.onCreateView] reuse the layout, it will be called once
     *
     * @param view view of fragment
     */
    protected open fun onViewPrepared(view: View, savedInstanceState: Bundle?) {}

    protected fun setReuseView(reuseTheView: Boolean) {
        reuseView.reuseTheView = reuseTheView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (reuseView.destroyView()) {
            _vb = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
    }

    protected open fun onCreateLoadingView(): LoadingViewHost? {
        return null
    }

    private fun loadingView(): LoadingViewHost {
        val loadingViewImpl = loadingViewHost
        return if (loadingViewImpl != null) {
            loadingViewImpl
        } else {
            loadingViewHost = onCreateLoadingView() ?: AndroidSword.sLoadingViewHostFactory?.invoke(requireContext())
            loadingViewHost ?: throw NullPointerException("you need to config LoadingViewFactory in Sword or implement onCreateLoadingView.")
        }
    }

    override fun showLoadingDialog() {
        recentShowingDialogTime = System.currentTimeMillis()
        loadingView().showLoadingDialog(true)
    }

    override fun showLoadingDialog(cancelable: Boolean) {
        recentShowingDialogTime = System.currentTimeMillis()
        loadingView().showLoadingDialog(cancelable)
    }

    override fun showLoadingDialog(message: CharSequence, cancelable: Boolean) {
        recentShowingDialogTime = System.currentTimeMillis()
        loadingView().showLoadingDialog(message, cancelable)
    }

    override fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean) {
        recentShowingDialogTime = System.currentTimeMillis()
        loadingView().showLoadingDialog(messageId, cancelable)
    }

    override fun dismissLoadingDialog() {
        loadingViewHost?.dismissLoadingDialog()
    }

    override fun dismissLoadingDialog(minimumMills: Long, onDismiss: (() -> Unit)?) {
        dismissDialog(recentShowingDialogTime, minimumMills, onDismiss)
    }

    override fun isLoadingDialogShowing(): Boolean {
        return loadingViewHost != null && loadingView().isLoadingDialogShowing()
    }

    override fun showMessage(message: CharSequence) {
        loadingView().showMessage(message)
    }

    override fun showMessage(@StringRes messageId: Int) {
        loadingView().showMessage(messageId)
    }

}