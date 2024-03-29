package com.app.base.widget.address;


import androidx.annotation.WorkerThread;

import com.android.base.utils.common.Lang;

import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSource;
import okio.Okio;

class IOUtils {

    private IOUtils() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    @WorkerThread
    static String convertToString(InputStream stream) {
        BufferedSource source = null;
        try {
            source = Okio.buffer(Okio.source(stream));
            return source.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Lang.closeIO(source);
        }
        return "";
    }

}