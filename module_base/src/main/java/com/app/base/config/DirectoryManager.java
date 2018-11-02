package com.app.base.config;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.android.base.utils.BaseUtils;
import com.android.base.utils.android.DirectoryUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 14:24
 */
public class DirectoryManager {


    private static final String APP_NAME = "GW-Parent";
    private static final String TEMP_PICTURE = "temp-pictures";
    private static final String PICTURE_FORMAT_JPEG = ".jpeg";
    private static final String VIDEO_FORMAT_MP4 = ".mp4";

    static final String IMAGE_CACHE_DIR = "app_images_cache";//Glide的图片缓存位置

    public static String createTempPicturePath() {
        return getAppExternalStorePath() + TEMP_PICTURE + File.separator + formatDate(new Date()) + PICTURE_FORMAT_JPEG;
    }

    @NonNull
    public static String createDCIMPictureStorePath(String fileName) {
        File file = new File(getExternalPicturePath() + fileName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            Timber.d("createDCIMPictureStorePath() called mkdirs:" + mkdirs);
        }
        return file.getAbsolutePath();
    }

    @NonNull
    private static File getDCIMPath(String format) {
        File file = new File(getExternalPicturePath() + formatDate(new Date()) + format);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            Timber.d("createDCIMPictureStorePath() called mkdirs:" + mkdirs);
        }
        return file;
    }

    /**
     * 获取App图片图片缓存位置
     */
    public static File getImageCacheDir() {
        File cacheDirectory = BaseUtils.getAppContext().getExternalCacheDir();
        return new File(cacheDirectory, IMAGE_CACHE_DIR);
    }

    /**
     * 获取外部存储的图片位置
     */
    private static String getExternalPicturePath() {
        File publicDirectory = DirectoryUtils.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return publicDirectory.toString() + File.separator + APP_NAME + File.separator;
    }

    /**
     * 获取SD卡上外部存储
     */
    @SuppressWarnings("unused")
    private static String getSDCardExternalStorePath() {
        String state = Environment.getExternalStorageState();
        String savePath;
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            savePath = BaseUtils.getAppContext().getFilesDir().getAbsolutePath() + File.separator;
        } else {
            savePath = Environment.getExternalStorageDirectory() + File.separator;
        }
        return savePath;
    }

    /**
     * 获取SD卡上私有存储目录
     */
    private static String getAppExternalStorePath() {
        String state = Environment.getExternalStorageState();
        String savePath;
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            savePath = BaseUtils.getAppContext().getCacheDir() + File.separator;
        } else {
            savePath = BaseUtils.getAppContext().getExternalCacheDir() + File.separator;
        }
        return savePath;
    }


    @SuppressLint("SimpleDateFormat")
    private static String formatDate(Date date) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date);//统一生成图片的名称格式
    }

}
