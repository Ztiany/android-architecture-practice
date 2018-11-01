package com.app.base.errorhandle;

/**
 * 异常处理器
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-11-30 17:38
 */
public interface ErrorHandler {

    String createMessage(Throwable throwable);

    void handleError(Throwable throwable);

}
