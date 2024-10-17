package com.app.common.api.errorhandler

import com.app.base.data.protocol.isGlobalApiError

interface ErrorHandler {

    /** 根据异常，生成一个合理的错误提示。 */
    fun generateMessage(throwable: Throwable): CharSequence

    /**
     * 直接处理异常，比如根据 [generateMessage] 方法生成的消息弹出一个 Toast。
     * 注意：对于 [isGlobalApiError] 返回 true 的异常，此方法会转发给 [handleGlobalError] 处理。
     */
    fun handleError(throwable: Throwable)

    /** 处理全局异常，此方法仅由数据层调用，用于统一处理全局异常，比如登录过期。 */
    fun handleGlobalError(throwable: Throwable)

}
