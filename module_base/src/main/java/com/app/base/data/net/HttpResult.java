package com.app.base.data.net;

/**
 * 业务数据模型，所有网络数据必须按照此格式返回
 *
 * @author Ztiany
 * Date : 2018-08-13
 */
@SuppressWarnings("unused")
public class HttpResult<T> {

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

    public String getMsg() {
        return msg;
    }

    public boolean hasData() {
        return data != null;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
