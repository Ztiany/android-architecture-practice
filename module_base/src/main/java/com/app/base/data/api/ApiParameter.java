package com.app.base.data.api;

import java.io.File;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public final class ApiParameter {

    static final String HEADER_TOKEN_NAME = "Token";

    static final String HEADER_PLATFORM_NAME = "Platform";
    static final String HEADER_PLATFORM_VALUE = "android";

    static final String HEADER_VERSION_NAME = "ver";

    public static final String HEADER_JSON = "Content-Type: application/json; charset=utf-8";

    /**
     * 生成表单请求体。
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