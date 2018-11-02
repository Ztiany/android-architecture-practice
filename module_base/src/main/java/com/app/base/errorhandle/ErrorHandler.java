package com.app.base.errorhandle;

/**
 * 异常处理器
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-11-30 17:38
 */
public interface ErrorHandler {

    /**
     * 根据异常，生成一个合理的错误提示
     */
    String createMessage(Throwable throwable);

    /**
     * 直接处理异常，根据{@link #createMessage(Throwable)} 方法生生的消息弹出一个 toast
     */
    void handleError(Throwable throwable);

}
