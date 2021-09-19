package com.app.base.common.web;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-01-21 20:54
 */
class WebUtils {

    static String buildJavascript(String method, String[] params) {
        StringBuilder sb = new StringBuilder("javascript:");
        sb.append(method);
        sb.append("(");

        if (params != null) {
            int length = params.length;
            for (int i = 0; i < length; i++) {
                sb.append(params[i]);
                if (i != length - 1) {
                    sb.append(",");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    static boolean isValidateTitle(String title) {
        return !"undefined".equalsIgnoreCase(title);
    }

    @NotNull
    public static String removePath(@Nullable String baseWebUrl) {
        try {
            URL url = new URL(baseWebUrl);
            return url.getProtocol() + "://" + url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

}