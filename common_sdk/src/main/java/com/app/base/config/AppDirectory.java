package com.app.base.config;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.android.base.utils.BaseUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import timber.log.Timber;

/**
 * todo 适配 AndroidQ ScopedStorage.
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 14:24
 */
public class AppDirectory {

    private static final String APP_NAME = "APP_NAME";
    public static final String SDCARD_FOLDER_NAME = "APP_NAME";

    private static final String TEMP_PICTURE = "temp-pictures";
    private static final String TEMP_FILES = "temp-files";

    /**
     * ImageLoader 的图片缓存位置
     */
    static final String IMAGE_CACHE_DIR = "app_images_cache";

    public static final String PICTURE_FORMAT_JPEG = ".jpeg";
    public static final String PICTURE_FORMAT_PNG = ".png";
    public static final String VIDEO_FORMAT_MP4 = ".mp4";

    ///////////////////////////////////////////////////////////////////////////
    // sd card private
    ///////////////////////////////////////////////////////////////////////////

    /**
     * APK 下载路径
     *
     * @param version apk 版本
     */
    public static String createAppDownloadPath(String version) {
        String path = getAppExternalPrivatePath(Environment.DIRECTORY_DOWNLOADS) + "apk" + File.separator + version + ".apk";
        makeParentPath(new File(path), "createAppDownloadPath");
        return path;
    }

    /**
     * 根据日期生成一个临时的用于存储文件的全路径，格式由 format 制定。
     */
    public static String createTempFilePath(String format) {
        String path = getAppExternalPrivateCachePath() + TEMP_FILES + File.separator + createTempFileName(format);
        makeParentPath(new File(path), "createTempFilePath() called mkdirs: ");
        return path;
    }

    /**
     * 根据日期生成一个临时的用于存储图片的全路径，格式由 format 制定。
     */
    public static String createTempPicturePath(String format) {
        String path = getAppExternalPrivateCachePath() + TEMP_PICTURE + File.separator + createTempFileName(format);
        makeParentPath(new File(path), "createTempPicturePath() called mkdirs: ");
        return path;
    }

    /**
     * 获取 App 图片图片缓存位置
     */
    public static File getImageCacheDir() {
        File cacheDirectory = BaseUtils.getAppContext().getExternalCacheDir();
        return new File(cacheDirectory, IMAGE_CACHE_DIR);
    }

    /**
     * 获取 SD 卡上私有存储目录
     *
     * @return like /storage/emulated/0/Android/data/包名/PICTURE/...
     */
    private static String getAppExternalPrivatePath(String type) {
        String state = Environment.getExternalStorageState();
        String savePath;
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            savePath = BaseUtils.getAppContext().getFilesDir() + File.separator;
        } else {
            savePath = BaseUtils.getAppContext().getExternalFilesDir(type) + File.separator;
        }
        return savePath;
    }

    /**
     * 获取 SD 卡上私有缓存存储目录
     *
     * @return like /storage/emulated/0/Android/data/包名/cache/
     */
    private static String getAppExternalPrivateCachePath() {
        String state = Environment.getExternalStorageState();
        String savePath;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            savePath = BaseUtils.getAppContext().getExternalCacheDir() + File.separator;
        } else {
            savePath = BaseUtils.getAppContext().getCacheDir() + File.separator;
        }
        return savePath;
    }

    ///////////////////////////////////////////////////////////////////////////
    // sd card public
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取 DCIM 存储图片的路径。
     *
     * @return /storage/sdcard0/DCIM/APP_NAME/xxx.png
     */
    @NonNull
    public static String createDCIMPicturePath(String format) {
        String path = getSDCardPublicDirectoryPath(Environment.DIRECTORY_DCIM).toString() + File.separator + APP_NAME + File.separator;
        File file = new File(path + createTempFileName(format));
        makeParentPath(file, "createDCIMPictureStorePath() called mkdirs: ");
        return file.getAbsolutePath();
    }

    /**
     * 获取公共的外部存储目录。
     *
     * @param type {@link Environment#DIRECTORY_DOWNLOADS}, {@link Environment#DIRECTORY_DCIM}, ect
     * @return DIRECTORY_DCIM = /storage/sdcard0/DCIM , DIRECTORY_DOWNLOADS =  /storage/sdcard0/Download ...ect
     */
    public static File getSDCardPublicDirectoryPath(@NonNull String type) {
        String state = Environment.getExternalStorageState();
        File dir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            dir = Environment.getExternalStoragePublicDirectory(type);
        } else {
            dir = new File(BaseUtils.getAppContext().getFilesDir(), type);
        }
        makePath(dir, "getExternalStoragePublicDirectory");
        return dir;
    }

    /**
     * 获取 APP 在 SD 卡上的外部存储目录。
     */
    public static String getSDCardPublicAppStoragePath() {
        String path = getSDCardRootPath() + File.separator + SDCARD_FOLDER_NAME;
        makePath(new File(path), "getAppSDCardExternalStorePath");
        return path;
    }

    /**
     * 获取 SD 卡根目录
     *
     * @return /storage/emulated/0/
     */
    public static File getSDCardRootPath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory();
        } else {
            return BaseUtils.getAppContext().getFilesDir();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Tools
    ///////////////////////////////////////////////////////////////////////////

    private static void makeParentPath(File file, String tag) {
        try {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                boolean mkdirs = parentFile.mkdirs();
                Timber.d("%s : %s", tag, mkdirs);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private static void makePath(File dir, String tag) {
        try {
            if (dir != null && !dir.exists()) {
                boolean mkdirs = dir.mkdirs();
                Timber.d("%s : %s", tag, mkdirs);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private static final ThreadLocal<SimpleDateFormat> SDF_HOLDER = new ThreadLocal<SimpleDateFormat>() {

        @SuppressLint("ConstantLocale")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        }

    };

    /**
     * 根据日期生成一个临时文件名，格式由 format 制定。
     */
    private static String createTempFileName(String format) {
        String tempFileName = Objects.requireNonNull(SDF_HOLDER.get()).format(new Date()) + "_" + UUID.randomUUID().toString();
        return tempFileName + format;
    }

}