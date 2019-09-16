package com.app.base.app

import android.app.Dialog
import com.android.sdk.net.errorhandler.ErrorMessageFactory
import java.lang.ref.WeakReference

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:02
 */
interface ErrorHandler {

    /** 根据异常，生成一个合理的错误提示 */
    fun createMessage(throwable: Throwable): CharSequence

    /** 直接处理异常，比如根据 [createMessage] 方法生成的消息弹出一个 toast。 */
    fun handleError(throwable: Throwable)

    /** 直接处理异常，自定义消息处理*/
    fun handleError(throwable: Throwable, processor: ((CharSequence) -> Unit))

    /**处理全局异常，此方法仅由数据层调用，用于统一处理全局异常*/
    fun handleGlobalError(code: Int)

}

internal class AppErrorHandler : ErrorHandler {

    private var showingDialog: WeakReference<Dialog>? = null

    override fun createMessage(throwable: Throwable): CharSequence {
        return ErrorMessageFactory.createMessage(throwable)
    }

    override fun handleError(throwable: Throwable) {

    }

    override fun handleError(throwable: Throwable, processor: (CharSequence) -> Unit) {

    }

    override fun handleGlobalError(code: Int) {

    }

    private fun showReLoginDialog(code: Int): Boolean {

        return true
    }

}