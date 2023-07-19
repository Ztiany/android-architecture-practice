package com.app.base.data.protocol;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ErrorResult {

    @SerializedName("msg")
    private String msg;

    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @NonNull
    @Override
    public String toString() {
        return "ErrorResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }

}
