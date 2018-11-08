package com.android.sdk.net.errorhandler;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:02
 */
public interface ErrorHandler {

    /**
     * 根据异常，生成一个合理的错误提示
     */
    CharSequence createMessage(Throwable throwable);

    /**
     * 直接处理异常，比如根据{@link #createMessage(Throwable)} 方法生生的消息弹出一个 toast。
     */
    void handleError(Throwable throwable);

    static CharSequence createDefaultErrorMessage(Throwable throwable) {
        return ErrorMessageFactory.createMessage(throwable);
    }

}
