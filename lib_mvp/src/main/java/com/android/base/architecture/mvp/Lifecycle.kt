package com.android.base.architecture.mvp


/**
 * @author Ztiany
 */
interface Lifecycle {

    /**
     * start the Lifecycle , initialize something, will be called only once
     */
    fun onStart()

    /**
     * will be called when view is ready.
     */
    fun onPostStart()

    fun onResume()

    fun onPause()

    /**
     * destroy the Lifecycle and release resource, will be called only once
     */
    fun onDestroy()

}

