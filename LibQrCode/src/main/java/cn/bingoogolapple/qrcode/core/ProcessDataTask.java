package cn.bingoogolapple.qrcode.core;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import io.fotoapparat.parameter.Size;

class ProcessDataTask extends AsyncTask<Void, Void, String> {

    @SuppressWarnings("all")
    private final int mRotation;
    private final Size mSize;
    private byte[] mData;
    private Delegate mDelegate;

    ProcessDataTask(byte[] data, Size size, int rotation, Delegate delegate) {
        mData = data;
        mSize = size;
        mRotation = rotation;
        mDelegate = delegate;
    }

    @SuppressLint("ObsoleteSdkInt")
    ProcessDataTask perform() {
        if (Build.VERSION.SDK_INT >= 11) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            execute();
        }
        return this;
    }

    void cancelTask() {
        if (getStatus() != Status.FINISHED) {
            cancel(true);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mDelegate = null;
    }

    //https://stackoverflow.com/questions/16252791/zxing-camera-in-portrait-mode-on-android
    @Override
    protected String doInBackground(Void... params) {
        int width = mSize.width;
        int height = mSize.height;
        byte[] rotatedData = new byte[mData.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = mData[x + y * width];
            }
        }
        int tmp = width;
        width = height;
        height = tmp;
        try {
            if (mDelegate == null) {
                return null;
            }
            return mDelegate.processData(rotatedData, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface Delegate {
        String processData(byte[] data, int width, int height);
    }
}
