package com.app.base.app;

public class AppSecurity {

    public static void init() {
        //System.loadLibrary("app-security");
    }

    public static native String getAppToken();

    public static native String getAppId();

}
