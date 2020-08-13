package com.app.base.data.api;

import android.Manifest;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.android.base.utils.security.AESUtils;
import com.app.base.AppContext;
import com.app.base.app.AppSecurity;
import com.app.base.config.AppFileSystemManager;
import com.blankj.utilcode.util.FileIOUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import androidx.core.content.ContextCompat;
import timber.log.Timber;

/**
 * todo: mmap + AndroidQ
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-11-23 10:44
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class DeviceId {

    private static final String BH_DEVICE_FILE_NAME = "bh_credit_token";

    private static String deviceId = "";

    /**
     * 参考：https://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2853253#2853253
     * 参考：https://developer.android.com/training/articles/user-data-ids
     * 参考：备选方案：安卓 oaid
     * <p>
     * 该方法必须在获取到 sd 卡权限后才能调用。
     *
     * @return device id
     */
    public static String getDeviceId() {
        if (!TextUtils.isEmpty(deviceId)) {
            write2Files(deviceId);
            return deviceId;
        }

        synchronized (DeviceId.class) {
            if (!TextUtils.isEmpty(deviceId)) {
                return deviceId;
            }
            String device = readFromFile();
            if (TextUtils.isEmpty(device)) {
                deviceId = UUID.randomUUID().toString();
                write2Files(deviceId);
            } else {
                deviceId = device;
            }
        }

        return deviceId;
    }

    private static void write2Files(String deviceId) {
        String encryptedDeviceId = encryptData(deviceId);

        AppContext.schedulerProvider().io().scheduleDirect(() -> {

            //internal
            try {
                File file = new File(AppContext.get().getExternalFilesDir(null), BH_DEVICE_FILE_NAME);
                String old = FileIOUtils.readFile2String(file);
                if (TextUtils.isEmpty(old) || !old.equals(encryptedDeviceId)) {
                    Timber.d("do saveDeviceToFile inner");
                    FileIOUtils.writeFileFromString(file, encryptedDeviceId);
                } else {
                    Timber.d("try saveDeviceToFile inner, same, give up");
                }
            } catch (Exception e) {
                Timber.e(e, "saveDeviceToFile inner");
            }

            //sdcard
            try {
                if (hasSDCardPermission()) {
                    File file = new File(AppFileSystemManager.getAppSDCardExternalStorePath(), BH_DEVICE_FILE_NAME);
                    String old = FileIOUtils.readFile2String(file);
                    if (TextUtils.isEmpty(old) || !old.equals(encryptedDeviceId)) {
                        Timber.d("do saveDeviceToFile sdcard");
                        FileIOUtils.writeFileFromString(file, encryptedDeviceId);
                    } else {
                        Timber.d("try saveDeviceToFile sdcard, same, give up");
                    }
                } else {
                    Timber.d("try saveDeviceToFile sdcard, no permission");
                }
            } catch (Exception e) {
                Timber.e(e, "saveDeviceToFile sdcard");
            }
        });
    }

    private static String readFromFile() {
        String device = "";
        File file;

        //outer file
        boolean hasSDCardPermission = hasSDCardPermission();

        Timber.d("read from sdcard hasSDCardPermission = %s", String.valueOf(hasSDCardPermission));

        if (hasSDCardPermission) {
            Timber.d("read from sdcard");
            file = new File(AppFileSystemManager.getAppSDCardExternalStorePath(), BH_DEVICE_FILE_NAME);
            device = FileIOUtils.readFile2String(file);
            if (!TextUtils.isEmpty(device)) {
                try {
                    device = decryptData(device);
                } catch (Exception e) {
                    file.delete();
                    device = "";
                }
            }
        }

        if (!TextUtils.isEmpty(device)) {
            return device;
        }

        //inner file
        Timber.d("read from inner");
        file = new File(AppContext.get().getExternalFilesDir(null), BH_DEVICE_FILE_NAME);
        device = FileIOUtils.readFile2String(file);
        if (!TextUtils.isEmpty(device)) {
            try {
                return decryptData(device);
            } catch (Exception e) {
                file.delete();
            }
        }

        return null;
    }

    private static String decryptData(String device) {
        Timber.d("encrypted device id = %s", device);
        byte[] bytes = AESUtils.decryptDataFromBase64(device, AESUtils.AES, AppSecurity.getAESKey());
        String decryptedDevice = new String(bytes, StandardCharsets.UTF_8);
        Timber.d("origin device id = %s", decryptedDevice);
        return decryptedDevice;
    }

    private static String encryptData(String device) {
        Timber.d("origin device id = %s", device);
        String encryptedDevice = AESUtils.encryptDataToBase64(device, AESUtils.AES, AppSecurity.getAESKey());
        Timber.d("encrypted device id = %s", encryptedDevice);
        return encryptedDevice;
    }

    private static boolean hasSDCardPermission() {
        return ContextCompat.checkSelfPermission(AppContext.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

}