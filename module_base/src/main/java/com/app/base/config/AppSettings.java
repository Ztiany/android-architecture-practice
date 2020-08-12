package com.app.base.config;

import com.app.base.AppContext;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-30 15:08
 */
public class AppSettings {

    public static final int DEFAULT_PAGE_START = 1;
    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final String TRANSITION_ANIMATION_PHOTOS = "transition_animation_photos";

    public static final long MINIMUM_DIALOG_SHOWING_TIME = 600L;

    public static boolean supportStatusBarLightMode = false;

    public static String appFileProviderAuthorities() {
        return AppContext.get().getPackageName() + ".file.provider";
    }

}
