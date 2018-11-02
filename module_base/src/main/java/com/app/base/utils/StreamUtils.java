package com.app.base.utils;

import android.support.annotation.WorkerThread;

import com.android.base.utils.common.CloseUtils;

import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSource;
import okio.Okio;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-02 13:59
 */
public class StreamUtils {

    @WorkerThread
    public static String convertToString(InputStream stream) {
        BufferedSource source = null;
        try {
            source = Okio.buffer(Okio.source(stream));
            return source.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(source);
        }
        return "";
    }

}
