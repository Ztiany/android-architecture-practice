package com.app.base.common.web;

public final class BrowserPath {

    public static final String PATH = "/app_browser/browser";

    public static final String URL_KEY = "url_key";//string

    /**
     * for set custom fragment.
     */
    public static final String FRAGMENT_KEY = "fragment_class_key";//string

    /**
     * define whether the fragment should show it's header. default is true
     */
    public static final String SHOW_HEADER_KEY = "show_header";//boolean

    /**
     * define the {@link JsCallInterceptor}, the defined JsCallInterceptor must has a constructor without arguments.
     */
    public static final String JS_CALL_INTERCEPTOR_CLASS_KEY = "js_call_interceptor_class_key";//string with full path class name

    /**
     * pass a bundle to fragment, through {@link androidx.fragment.app.Fragment#getArguments()} to get fragment's arguments, then through {@link android.os.Bundle#getBundle(String)} to get this bundle.
     */
    public static final String ARGUMENTS_KEY = "arguments_key";//Bundle

    /**
     * config the web view if can use cache, default is false
     */
    public static final String CACHE_ENABLE = "cache_enable";//boolean

    /**
     * custom page title.
     */
    public static final String PAGE_TITLE = "web_title";//String
}