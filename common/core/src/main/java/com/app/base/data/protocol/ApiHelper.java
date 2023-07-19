package com.app.base.data.protocol;


import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ApiErrorException;

public class ApiHelper {

    /**
     * 返回成功
     */
    private static final int CODE_SUCCESS = 0;

    /**
     * 账号已在其他设备登录
     */
    public static final int SSO = 30003;

    /**
     * Token 过期
     */
    public static final int TOKEN_EXPIRED = 407;

    /**
     * 鉴权失败
     */
    public static final int TOKEN_INVALIDATE = 401;

    public static boolean isSuccess(@NonNull HttpResult<?> httpResult) {
        return httpResult.getCode() == CODE_SUCCESS;
    }

    public static boolean isAuthenticationExpired(Throwable throwable) {
        if (!(throwable instanceof ApiErrorException)) return false;
        int code = ((ApiErrorException) throwable).getCode();
        return isAuthenticationExpired(code);
    }

    public static boolean isAuthenticationExpired(int code) {
        return code == SSO || code == TOKEN_EXPIRED || code == TOKEN_INVALIDATE;
    }

    public static Exception buildAuthenticationExpiredException(int code, String hostFlag) {
        if (code == SSO) {
            return new ApiErrorException(code, "账号已在其他设备登录", hostFlag);
        } else if (code == TOKEN_INVALIDATE) {
            return new ApiErrorException(code, "鉴权失败", hostFlag);
        }
        return new ApiErrorException(TOKEN_EXPIRED, "登录已过期", hostFlag);
    }

}