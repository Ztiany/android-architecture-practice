package com.app.base.data.protocol;

import com.google.gson.annotations.SerializedName;

public class ErrorResult {

    @SerializedName("msg")
    private String msg;

    @SerializedName("status")
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

}
