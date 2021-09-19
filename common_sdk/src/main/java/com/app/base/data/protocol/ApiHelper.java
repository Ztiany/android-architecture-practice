package com.app.base.data.protocol;


import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.result.Result;

import androidx.annotation.NonNull;

public class ApiHelper {

    /*Json解析错误*/
    private static final int DATA_ERROR = -8088;

    /*返回成功*/
    private static final int CODE_SUCCESS = 1;

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

    public static Exception buildAuthenticationExpiredException() {
        return new ApiErrorException(KEY_INVALID, "登录过期");
    }

    @SuppressWarnings({"rawtypes"})
    public static boolean isDataError(Object data) {
        if (data instanceof Result) {
            return ((Result) data).getCode() == DATA_ERROR;
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