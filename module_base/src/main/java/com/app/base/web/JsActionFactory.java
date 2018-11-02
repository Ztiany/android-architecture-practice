package com.app.base.web;

import android.support.annotation.Nullable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-01-24 14:45
 */
class JsActionFactory {

    private static final String SAVE_IMAGE_METHOD = "saveImage";

    static Runnable createAction(BaseWebFragment fragment, String method, @Nullable String[] args) {
        if (SAVE_IMAGE_METHOD.equals(method)) {
            return new SaveImageAction(fragment, args);
        }
        return null;
    }
}
