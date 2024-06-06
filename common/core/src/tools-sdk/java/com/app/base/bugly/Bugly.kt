package com.app.base.bugly;

import static com.blankj.utilcode.util.CloseUtils.closeIO;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.RestrictTo;

import com.app.base.debug.Debug;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-12-02 11:07
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class Bugly {

    private static final String KEY_DEBUG = "?";
    private static final String KEY_RELEASE = "?";

    public static void init(Application application, String channel) {
        // 获取当前包名
        String packageName = application.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(application);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //设置渠道
        strategy.setAppChannel(channel);
        //延迟初始化 10s
        strategy.setAppReportDelay(10000);
        //初始化
        CrashReport.initCrashReport(
                application,
                Debug.isOpenDebug() ? KEY_DEBUG : KEY_RELEASE,//注册时申请的 AppId
                Debug.isOpenDebug(),//第三个参数为SDK调试模式开关
                strategy
        );
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            Timber.e(throwable, "getProcessName");
        } finally {
           closeIO(reader);
        }
        return null;
    }

}