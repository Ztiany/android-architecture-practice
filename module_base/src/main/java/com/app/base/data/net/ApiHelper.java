package com.app.base.data.net;


import android.support.annotation.NonNull;

import com.app.base.data.net.exception.ApiErrorException;

/**
 * @author Ztiany
 * Date : 2018-08-13
 */
public class ApiHelper {

    /*Json解析错误*/
    private static final int DATA_ERROR = -8088;
    /*返回成功*/
    private static final int CODE_SUCCESS = 200;
    /*Key失效*/
    private static final int KEY_INVALID = 99;

    public static boolean isSuccess(@NonNull HttpResult<?> httpResult) {
        return httpResult.getCode() == CODE_SUCCESS;
    }

    /**
     * @param httpResult 响应结果
     * @return 当前 httpResult 表示服务器返回的数据格式(比如 json )错误时返回 true
     */
    public static boolean isDataFormatError(@NonNull HttpResult httpResult) {
        return httpResult.getCode() == DATA_ERROR;
    }

    public static boolean isKeyInvalid(int code) {
        return code == KEY_INVALID;
    }

    public static HttpResult createErrorResult() {
        return new HttpResult(DATA_ERROR);
    }

    public static boolean isAuthenticationExpired(Throwable throwable) {
        return throwable instanceof ApiErrorException && ApiHelper.isKeyInvalid(((ApiErrorException) throwable).getCode());
    }
}
