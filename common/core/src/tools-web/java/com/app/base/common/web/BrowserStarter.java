package com.app.base.common.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public final class BrowserStarter {

    private final Intent mIntent;
    private final Context mContext;

    private BrowserStarter(Context context) {
        mIntent = new Intent(context, BrowserActivity.class);
        mContext = context;
    }

    static final String URL_KEY = "url_key";//string

    /**
     * for set custom fragment.
     */
    static final String FRAGMENT_KEY = "fragment_class_key";//string

    /**
     * define whether the fragment should show it's header. default is true
     */
    static final String SHOW_HEADER_KEY = "show_header";//boolean

    /**
     * define the {@link JsCallInterceptor}, the defined JsCallInterceptor must has a constructor without arguments.
     */
    static final String JS_CALL_INTERCEPTOR_CLASS_KEY = "js_call_interceptor_class_key";//string with full path class name

    /**
     * pass a bundle to fragment, through {@link androidx.fragment.app.Fragment#getArguments()} to get fragment's arguments, then through {@link android.os.Bundle#getBundle(String)} to get this bundle.
     */
    static final String ARGUMENTS_KEY = "arguments_key";//Bundle

    /**
     * config the web view if can use cache, default is false
     */
    static final String CACHE_ENABLE = "cache_enable";//boolean

    /**
     * custom page title.
     */
    static final String PAGE_TITLE = "web_title";//String

    public static BrowserStarter newStarter(Context context) {
        return new BrowserStarter(context);
    }

    public void start() {
        if (!mIntent.hasExtra(URL_KEY)) {
            throw new IllegalStateException("You need to define the url you want open.");
        }
        mContext.startActivity(mIntent);
    }

    public BrowserStarter withURL(String url) {
        mIntent.putExtra(URL_KEY, url);
        return this;
    }

    public BrowserStarter withFragmentArguments(Bundle bundle) {
        mIntent.putExtra(ARGUMENTS_KEY, bundle);
        return this;
    }

}