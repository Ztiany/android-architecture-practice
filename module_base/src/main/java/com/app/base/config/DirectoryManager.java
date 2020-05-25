package com.app.base.config;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.android.base.utils.BaseUtils;
import com.android.base.utils.android.DirectoryUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 14:24
 */
public class DirectoryManager {

    private static final String APP_NAME = "gelei";
    private static final String TEMP_PICTURE = "temp-pictures";
    private static final String TEMP_FILES = "temp-files";
    public static final String PICTURE_FORMAT_JPEG = ".jpeg";
    public static final String PICTURE_FORMAT_PNG = ".png";
    public static final String VIDEO_FORMAT_MP4 = ".mp4";
    public static final String BH_SDCARD_FOLDER_NAME = "YourAppName";

    static final String IMAGE_CACHE_DIR = "app_images_cache";//Glide的图片缓存位置

    public static List<String> getAppClearableCachePathList() {
        ArrayList<String> paths = new ArrayList<>();
        paths.add(getAppExternalStoreCachePath());
        paths.add(getAppExternalStorePath(Environment.DIRECTORY_DOWNLOADS) + "apk");
        return paths;
    }

    /**
     * APK下载路径
     *
     * @param version apk版本
     */
    public static String createAppDownloadPath(String version) {
        return getAppExternalStorePath(Environment.DIRECTORY_DOWNLOADS) + "apk" + File.separator + version + ".apk";
    }

    /**
     * 根据日期生成一个临时的用于存储图片的全路径，格式为 jpeg
     */
    public static String createTempJPEGPicturePath() {
        return createTempPicturePath(PICTURE_FORMAT_JPEG);
    }

    /**
     * 根据日期生成一个临时的用于存储图片的全路径，格式由 format 制定。
     */
    public static String createTempPicturePath(String format) {
        return getAppExternalStoreCachePath() + TEMP_PICTURE + File.separator + tempFileName() + format;
    }

    /**
     * 根据日期生成一个临时的用于存储文件的全路径，格式由 format 制定。
     */
    public static String createTempFilePath(String format) {
        return getAppExternalStoreCachePath() + TEMP_FILES + File.separator + tempFileName() + format;
    }

    /**
     * 根据日期生成一个临时文件名，格式由 format 制定。
     */
    public static String createTempFileName(String format) {
        return tempFileName() + format;
    }

    @NonNull
    public static String createDCIMPictureStorePath(String fileName) {
        File file = new File(getExternalPicturePath() + fileName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            Timber.d("createDCIMPictureStorePath() called mkdirs:%b", mkdirs);
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取App 图片图片缓存位置
     */
    public static File getImageCacheDir() {
        File cacheDirectory = BaseUtils.getAppContext().getExternalCacheDir();
        return new File(cacheDirectory, IMAGE_CACHE_DIR);
    }

    /**
     * 获取 DCIM 存储图片的路径
     */
    private static String getExternalPicturePath() {
        File publicDirectory = DirectoryUtils.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return publicDirectory.toString() + File.separator + APP_NAME + File.separator;
    }

    /**
     * 获取SD 卡上外部存储目录
     */
    public static String getBHSDCardExternalStorePath() {
        String state = Environment.getExternalStorageState();
        String path;
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            path = BaseUtils.getAppContext().getFilesDir().getAbsolutePath() + File.separator + BH_SDCARD_FOLDER_NAME;
        } else {
            path = Environment.getExternalStorageDirectory() + File.separator + BH_SDCARD_FOLDER_NAME;
        }
        return path;
    }

    /**
     * 获取 SD 卡上私有缓存存储目录
     */
    private static String getAppExternalStoreCachePath() {
        String state = Environment.getExternalStorageState();
        String savePath;
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            savePath = BaseUtils.getAppContext().getCacheDir() + File.separator;
        } else {
            savePath = BaseUtils.getAppContext().getExternalCacheDir() + File.separator;
        }
        return savePath;
    }

    /**
     * 获取 SD 卡上私有存储目录
     */
    private static String getAppExternalStorePath(String type) {
        String state = Environment.getExternalStorageState();
        String savePath;
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            savePath = BaseUtils.getAppContext().getFilesDir() + File.separator;
        } else {
            savePath = BaseUtils.getAppContext().getExternalFilesDir(type) + File.separator;
        }
        return savePath;
    }

    @SuppressLint("SimpleDateFormat")
    private static String tempFileName() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + UUID.randomUUID().toString();//统一生成图片的名称格式
    }

}