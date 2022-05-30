package com.android.sdk.net.core.provider;

import com.android.sdk.net.core.exception.ApiErrorException;

/**
 * @author Ztiany
 */
public interface ErrorMessage {

    /**
     * 网络错误提示消息。
     */
    CharSequence netErrorMessage(Throwable exception);

    /**
     * 服务器返回的数据格式异常消息。
     */
    CharSequence serverDataErrorMessage(Throwable exception);


    /**
     * 服务器返回的数据是为 null 异常消息。
     */
    CharSequence serverReturningNullDataErrorMessage(Throwable exception);

    /**
     * 服务器错误，比如 500-600 响应码。
     */
    CharSequence serverErrorMessage(Throwable exception);

    /**
     * 客户端请求错误，比如 400-499 响应码
     */
    CharSequence clientRequestErrorMessage(Throwable exception);

    /**
     * API 调用错误
     */
    CharSequence apiErrorMessage(ApiErrorException exception);

    /**
     * 未知错误
     */
    CharSequence unknownErrorMessage(Throwable exception);

}
