package com.app.base.data.utils;

import com.android.base.utils.common.Files;
import com.android.base.utils.common.Lang;
import com.android.sdk.net.core.exception.NetworkErrorException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-01-24 16:03
 */
public class OKHttpDownloader {

    /**
     * @param url      下载地址
     * @param saveFile 保存的路径
     * @return Observable，实际类型参数为保存后的路径
     */
    public static Observable<String> download(String url, File saveFile) {
        return Observable.just(url)
                .subscribeOn(Schedulers.io())
                .map(s -> downLoadImage(url, saveFile))
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static String downLoadImage(String url, File saveFile) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        ResponseBody body;

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                body = response.body();
            } else {
                throw new NetworkErrorException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetworkErrorException();
        }

        if (body == null) {
            throw new NetworkErrorException();
        }

        Files.makeParentPath(saveFile);

        InputStream is = null;
        FileOutputStream fos = null;
        try {
            byte[] buf = new byte[1024 * 2];
            int len;
            is = body.byteStream();
            fos = new FileOutputStream(saveFile);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Lang.closeIOQuietly(is, fos);
        }
        return saveFile.getAbsolutePath();
    }

}