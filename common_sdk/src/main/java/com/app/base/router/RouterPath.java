package com.app.base.router;

import com.app.base.common.web.JsCallInterceptor;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-02 14:04
 */
public class RouterPath {

    public static final String PAGE_KEY = "page_key";//int

    public static final class Main {
        public static final String PATH = "/app_main/main";

        public static final String ACTION_KEY = "action_key";

        public static final int ACTION_RE_LOGIN = -100;// 登录过期，重新登录
    }

    public static final class Account {
        public static final String PATH = "/app_base/account";
    }

    public static final class Browser {

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

}
