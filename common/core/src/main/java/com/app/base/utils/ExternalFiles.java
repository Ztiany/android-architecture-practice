package com.app.base.utils;

import static com.app.base.config.AppPrivateDirectories.createTempFileName;

import android.os.Environment;

import androidx.annotation.NonNull;

import com.android.base.utils.BaseUtils;

import java.io.File;

import timber.log.Timber;

/**
 * TODO：Scope Storage
 */
public class ExternalFiles {

    private static final String APP_NAME = "APP_NAME";

    public static final String SDCARD_FOLDER_NAME = "APP_NAME";

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

}
