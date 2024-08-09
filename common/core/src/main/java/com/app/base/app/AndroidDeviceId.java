package com.app.base.app;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

class AndroidDeviceId {

    private static String sFakeDeviceId = "";

    private static String DEVICE_ID_KEY = "";
    private static String DEVICE_ID_FILE = "";

    private static boolean sWroteToFile = false;

    private final static AtomicBoolean ATOMIC_BOOLEAN = new AtomicBoolean(false);

    /**
     * 获取手机的设备 ID。
     *
     * @return DeviceId
     */
    public static String get(Context context) {
        String deviceId = "";

        if (TextUtils.isEmpty(deviceId)) {
            if (DEVICE_ID_KEY.isEmpty()) {
                DEVICE_ID_KEY = context.getPackageName().replace(".", "_") + "_device_id";
                DEVICE_ID_FILE = "." + DEVICE_ID_KEY;
            }
            deviceId = makeAndSaveFakeId(context);
        }

        Timber.d("finial device id: %s", deviceId);

        return deviceId;
    }

    private static String makeAndSaveFakeId(Context context) {
        while (TextUtils.isEmpty(sFakeDeviceId)) {

            if (ATOMIC_BOOLEAN.compareAndSet(false, true)) {

                String uuid = getUuidFromSystemSettings(context);
                if (TextUtils.isEmpty(uuid)) {
                    uuid = getUuidFromExternalStorage(context);
                }

                if (TextUtils.isEmpty(uuid)) {
                    uuid = getUuidFromSharedPreferences(context);
                }

                if (TextUtils.isEmpty(uuid)) {
                    uuid = makeFakeId(context);
                    Timber.d("makeFakeId: %s", uuid);
                }

                saveUuidToSharedPreferences(context, uuid);
                saveUuidToSystemSettings(context, uuid);
                saveUuidToExternalStorage(context, uuid);

                sFakeDeviceId = uuid;
            }
        }

        if (!sWroteToFile) {
            saveUuidToExternalStorage(context, sFakeDeviceId);
        }
        return sFakeDeviceId;
    }

    private static String getUuidFromSystemSettings(Context context) {
        String uuid = Settings.System.getString(context.getContentResolver(), DEVICE_ID_KEY);
        Timber.d("Get uuid from system settings: %s", uuid);
        return uuid;
    }

    private static void saveUuidToSystemSettings(Context context, String uuid) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(context)) {
            try {
                Settings.System.putString(context.getContentResolver(), DEVICE_ID_KEY, uuid);
                Timber.d("Save uuid to system settings: %s", uuid);
            } catch (Exception e) {
                Timber.e(e, "saveUuidToSystemSettings");
            }
        } else {
            Timber.d("android.permission.WRITE_SETTINGS not granted");
        }
    }

    private static String getUuidFromExternalStorage(Context context) {
        String uuid = "";
        File file = getGuidFile(context);
        if (file != null && file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                uuid = reader.readLine();
            } catch (Exception e) {
                Timber.d(e, "getUuidFromExternalStorage");
            }
        }
        Timber.d("Get uuid from external storage: %s", uuid);
        return uuid;
    }

    /* TODO：兼容 scope storage. */
    private static void saveUuidToExternalStorage(Context context, String uuid) {
        File file = getGuidFile(context);
        if (file == null) {
            Timber.d("UUID file in external storage is null");
            return;
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(uuid);
            writer.flush();
            sWroteToFile = true;
            Timber.d("Save uuid to external storage: %s", uuid);
        } catch (Exception e) {
            Timber.d(e, "saveUuidToExternalStorage");
        }
    }

    private static File getGuidFile(Context context) {
        boolean hasStoragePermission;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            hasStoragePermission = true;
        } else if (Build.VERSION.SDK_INT >= 30) {
            hasStoragePermission = false;
        } else {
            hasStoragePermission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        if (hasStoragePermission && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return new File(Environment.getExternalStorageDirectory(), "Android/" + DEVICE_ID_FILE);
        }
        return null;
    }

    private static void saveUuidToSharedPreferences(Context context, String uuid) {
        SharedPreferences preferences = context.getSharedPreferences(DEVICE_ID_KEY, Context.MODE_PRIVATE);
        preferences.edit().putString("uuid", uuid).apply();
        Timber.d("Save uuid to shared preferences: %s", uuid);
    }

    private static String getUuidFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(DEVICE_ID_KEY, Context.MODE_PRIVATE);
        String uuid = preferences.getString("uuid", "");
        Timber.d("Get uuid from shared preferences: %s", uuid);
        return uuid;
    }

    /**
     * 获取一个唯一设备标识。
     */
    private static String makeFakeId(Context context) {

        StringBuilder sbDeviceId = new StringBuilder();

        //International Mobile Equipment Identity.
        String imei = getIMEI(context);

        //ANDROID ID
        String androidID = getAndroidId(context);

        //Serial  8.0
        String serial = getSerial();

        //Device UUID
        String id = getDeviceUUID().replace("-", "");

        //追加 IMEI
        if (imei != null && imei.length() > 0) {
            sbDeviceId.append(imei);
            sbDeviceId.append("|");
        }

        //追加 ANDROID ID
        if (androidID != null && androidID.length() > 0) {
            sbDeviceId.append(androidID);
            sbDeviceId.append("|");
        }

        //追加 SERIAL
        if (serial != null && serial.length() > 0) {
            sbDeviceId.append(serial);
            sbDeviceId.append("|");
        }

        //追加硬件 UUID
        if (id != null && id.length() > 0) {
            sbDeviceId.append(id);
        }

        //一系列的字符串硬件标识有，生成 SHA1，统一 DeviceId 长度
        if (sbDeviceId.length() > 0) {
            try {
                byte[] hash = getHashByString(sbDeviceId.toString());
                String sha1 = bytesToHex(hash);
                if (sha1 != null && sha1.length() > 0) {
                    //返回最终的 DeviceId
                    return sha1;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return generateId();
    }

    private static String generateId() {
        return bytesToHex(UUID.randomUUID().toString().getBytes());
    }

    /**
     * 转 16 进制字符串。
     *
     * @param data 数据。
     * @return 16 进制字符串。
     */
    private static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        String sTemp;
        for (byte datum : data) {
            sTemp = (Integer.toHexString(datum & 0xFF));
            if (sTemp.length() == 1) {
                sb.append("0");
            }
            sb.append(sTemp);
        }
        return sb.toString().toUpperCase(java.util.Locale.CHINA);
    }

    /**
     * 取 SHA1。
     *
     * @param data 数据。
     * @return 对应的 hash 值。
     */
    private static byte[] getHashByString(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            return messageDigest.digest();
        } catch (Exception e) {
            return "".getBytes();
        }
    }

    /**
     * 用硬件信息拼接一个唯一标识：获得硬件 uuid（根据硬件相关属性，生成 uuid），改操作无需权限。
     */
    private static String getDeviceUUID() {
        String serial = getSerial();

        String dev = "100001" +
                Build.BOARD +//电路板
                Build.BRAND +//牌子
                Build.DEVICE +//工业设计名
                Build.HARDWARE +//硬件名
                Build.MANUFACTURER +//产品厂商
                Build.ID +
                Build.MODEL +
                Build.PRODUCT +
                serial;

        Timber.d("BOARD = %s, BRAND = %s,  DEVICE = %s, HARDWARE = %s, MANUFACTURER = %s, ID = %s, MODEL = %s, PRODUCT = %s, Serial = %s", Build.BOARD, Build.BRAND, Build.DEVICE, Build.HARDWARE, Build.MANUFACTURER, Build.ID, Build.MODEL, Build.PRODUCT, serial);

        return new UUID(dev.hashCode(), serial.hashCode()).toString();
    }

    /**
     * ANDROID_ID 是设备的系统首次启动时随机生成的一串字符，由 16 个 16 进制数（64 位）组成，理论上可以保证唯一性的。ANDROID_ID 的获取门槛是最低的，不需要任何权限。
     * <p>
     * 存在的问题：
     *     <ol>
     *         <li>无法保证稳定性，root、刷机或恢复出厂设置都会导致设备的 ANDROID_ID 发生改变。</li>
     *         <li>某些厂商定制系统的 Bug 会导致不同的设备可能会产生相同的 ANDROID_ID，而且某些设备获取到的 ANDROID_ID 为 null。</li>
     *     </ol>
     * </p>
     */
    private static String getAndroidId(Context context) {
        String androidId;
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ignored) {
            androidId = "";
        }
        //https://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
        if ("9774d56d682e549c".equals(androidId) || TextUtils.isEmpty(androidId)) {
            androidId = UUID.randomUUID().toString();
            Timber.e("getAndroidId failed, make a fake one: %s", androidId);
        }
        Timber.e("the final android id: %s", androidId);
        return androidId;
    }

    /**
     * 设备序列号是手机生产厂商提供的，如果拼接上厂商名称（Build.MANUFACTURER）基本上可以保证唯一性。
     * <p>
     *     <ol>
     *         <li>在 Android 8.0以下版本，可以通过 android.os.Build.SERIAL 获取到设备序列号</li>
     *         <li>在 Android 8.0 及以上版本被废弃了，通过 Build.SERIAL 在 Android 8.0 及以上设备获取到设备的序列号始终为 “unknown”，取而代之的是使用 android.os.Build.getSerial() 方法。</li>
     *     </ol>
     *     Build.getSerial() 方法在 Android 6.0 及以上版本是需要动态申请 READ_PHONE_STATE 权限的，并且该方法在 Android 10 上同样无法获取到设备序列号。具体情况如下：
     *     <ol>
     *         <li>Android 8.0 以下：无需申请权限，可以通过 Build.SERIAL 获取到设备序列号</li>
     *         <li>Android 8.0-Android 10：需要申请 READ_PHONE_STATE 权限，可以通过 Build.getSerial() 获取到设备序列号，如果没有权限直接获取，会抛出 java.lang.SecurityException异常</li>
     *         <li>
     *             Android 10 及以上：分为以下两种情况：
     *             <ol>
     *                 <li>targetSdkVersion < 29：没有申请权限的情况，调用 Build.getSerial() 方法时抛出 java.lang.SecurityException 异常；申请了权限，通过 Build.getSerial() 方法获取到的设备序列号为 “unknown”</li>
     *                 <li>targetSdkVersion >= 29：无论是否申请了权限，调用 Build.getSerial() 方法时都会直接抛出 java.lang.SecurityException 异常</li>
     *             </ol>
     *         </li>
     *     </ol>
     *
     * </p>
     */
    private static String getSerial() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Build.getSerial();
            } else {
                return Build.SERIAL;
            }
        } catch (Exception ignored) {
            Timber.e("getSerial failed.");
        }
        return "";
    }

    /**
     * IMEI(International Mobile Equipment Identity) 是国际移动设备识别码的缩写，由 15-17 位数字组成，与手机是一一对应的关系，该码是全球唯一的，并且永远不会改变。
     * <p>
     * 获取方法：
     *     <ol>
     *         <li>在Android 8.0（API Level 26）以下，可以通过 TelephonyManager 的 getDeviceId() 方法获取到设备的 IMEI 码（其实这个说法不准确，该方法是会根据手机设备的制式（GSM 或 CDMA）返回相应的设备码（IMEI、MEID 和 ESN））</li>
     *         <li>getDeviceId()  方法在 Android 8.0 及之后的版本已经被废弃了，取而代之的是 getImei() 方法。</li>
     *         <li>无论是 getDeviceId() 方法还是 getImei() 方法都可以传入一个参数 slotIndex。</li>
     *     </ol>
     *     限制：
     *     <ol>
     *         <li>Android 6.0 以下：无需申请权限，可以通过 getDeviceId() 方法获取到 IMEI 码。</li>
     *         <li>Android 6.0-Android 8.0：需要申请 READ_PHONE_STATE 权限，可以通过 getDeviceId() 方法获取到 IMEI 码，如果没有权限直接获取，会抛出 java.lang.SecurityException 异常</li>
     *         <li>Android 8.0-Android 10：需要申请 READ_PHONE_STATE 权限，可以通过 getImei() 方法获取到 IMEI 码，如果没有权限直接获取，会抛出 java.lang.SecurityException 异常</li>
     *         <li>
     *             Android 10 及以上：分为以下两种情况：
     *             <ol>
     *                 <li>targetSdkVersion < 29：没有申请权限的情况，通过 getImei() 方法获取 IMEI 码时抛出 java.lang.SecurityException 异常；申请了权限，通过 getImei() 方法获取到 IMEI 码为 null</li>
     *                 <li>targetSdkVersion >= 29：无论是否申请了权限，通过 getImei() 方法获取 IMEI 码时都会直接抛出 java.lang.SecurityException 异常</li>
     *             </ol>
     *         </li>
     *     </ol>
     * </p>
     */
    private static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm.getImei();
            } else {
                return tm.getDeviceId();
            }
        } catch (Exception ignored) {
            Timber.e("getIMEI failed.");
        }
        return "";
    }

}