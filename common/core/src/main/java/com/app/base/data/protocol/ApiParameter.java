package com.app.base.data.protocol;

import java.io.File;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public final class ApiParameter {

    static final String HEADER_APPID_KEY = "AppId";
    static final String HEADER_APPKEY_KEY = "appKey";
    static final String HEADER_CHANNEL_KEY = "channelCode";

    public static final String HEADER_APPKEY_VALUE = "958308972492750848";

    static final String HEADER_CLIENT_KEY = "client";
    static final String HEADER_CLIENT_VALUE = "1";

    /*登录 token*/
    static final String HEADER_TOKEN_KEY = "Authorization";
    /*版本名*/
    static final String HEADER_VERSION_NAME_KEY = "versionName";
    /*版本号*/
    static final String HEADER_VERSION_CODE_KEY = "versionCode";
    /*设备唯一标识*/
    static final String HEADER_DEVICE_ID_KEY = "devicesId";
    /*手机品牌*/
    static final String HEADER_BRAND_KEY = "brand";
    /*平台标识*/
    static final String HEADER_OS_KEY = "os";
    /*平台标识【固定为 Android】*/
    static final String HEADER_OS_VALUE = "android";

    public static final String HEADER_JSON = "Content-Type: application/json; charset=utf-8";

    /**
     * 生成表单请求体。如何上传 URI？参考：
     *<ul>
     *     <li><a href="https://github.com/square/okhttp/issues/3585">okhttp/issues/3585</a></li>
     *     <li><a href="https://github.com/liangjingkanji/Net/issues/189">net/issues/189</a></li>
     *</ul>
     */
    public static Map<String, RequestBody> buildMultiPartRequestBody(Map<String, String> fieldParts, Map<String, File> fileParts) {

        Map<String, RequestBody> params = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : fieldParts.entrySet()) {
            params.put(
                    entry.getKey(),
                    RequestBody.create(MediaType.parse("text/plain"), entry.getValue()));
        }

        for (Map.Entry<String, File> entry : fileParts.entrySet()) {
            params.put(
                    URLEncoder.encode(entry.getKey()) + "\"; filename=\"" + URLEncoder.encode(entry.getValue().getName()),
                    RequestBody.create(null, entry.getValue()));
        }

        return params;
    }

}