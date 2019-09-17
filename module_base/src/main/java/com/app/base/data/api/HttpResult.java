package com.app.base.data.api;


import com.android.sdk.net.core.Result;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class HttpResult<T> implements Result<T> {

    public HttpResult() {
    }

    HttpResult(int code) {
        this.code = code;
    }

    private int code;
    private String msg;
    private T data;

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }

    @Override
    public boolean isSuccess() {
        return ApiHelper.isSuccess(this);
    }

    public boolean hasData() {
        return data != null;
    }

    @NonNull
    @Override
    public String toString() {
        return "HttpResult{" +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}