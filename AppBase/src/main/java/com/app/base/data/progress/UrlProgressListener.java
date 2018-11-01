package com.app.base.data.progress;

public interface UrlProgressListener {

    void onProgress(String url, long contentLength, long currentBytes, float percent, boolean isFinish);

    void onError(String url, Exception e);
}
