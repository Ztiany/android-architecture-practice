package com.app.base.data.api;


import com.android.sdk.net.exception.ApiErrorException;

import androidx.annotation.NonNull;

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

    public static boolean isKeyInvalid(int code) {
        return code == KEY_INVALID;
    }

    public static boolean isAuthenticationExpired(Throwable throwable) {
        return throwable instanceof ApiErrorException && ApiHelper.isKeyInvalid(((ApiErrorException) throwable).getCode());
    }

    public static boolean isDataError(Object data) {
        if (data instanceof HttpResult) {
            return ((HttpResult) data).getCode() == DATA_ERROR;
        }
        return false;
    }

    public static Object newErrorDataStub() {
        return new HttpResult<>(DATA_ERROR);
    }

    public static boolean isLoginExpired(int code) {
        return false;
    }

}