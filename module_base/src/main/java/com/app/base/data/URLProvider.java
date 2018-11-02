package com.app.base.data;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

class URLProvider {

    private static final Map<String, HttpURL> URL_MAP = new HashMap<>();

    @Inject
    URLProvider() {
    }

    static {
        /*调试环境*/
        URL_MAP.put(DataContext.DEBUG, new HttpURL("debug", 8081));
        /*预发布*/
        URL_MAP.put(DataContext.PRE, new HttpURL("pre", 8081));
        /*正式环境*/
        URL_MAP.put(DataContext.RELEASE, new HttpURL("release", false));
    }

    String baseUrl() {
        DataContext instance = DataContext.getInstance();
        HttpURL httpURL = URL_MAP.get(instance.getEnvTag());
        assert httpURL != null;
        return httpURL.url();
    }

    private static final class HttpURL {

        final String HOST;
        final int PORT;
        final String SCHEME;

        private static final String HTTP = "http://";
        private static final String HTTPS = "https://";

        private static final String API_PATH_SEGMENT = "/api/";

        HttpURL(String HOST, boolean isHttps) {
            this(HOST, isHttps ? 443 : 80, isHttps);
        }

        HttpURL(String HOST, int PORT) {
            this(HOST, PORT, false);
        }

        HttpURL(String HOST, int PORT, boolean isHttps) {
            this.HOST = HOST;
            this.PORT = PORT;
            SCHEME = isHttps ? HTTPS : HTTP;
        }

        String url() {
            return SCHEME + HOST + ":" + PORT + API_PATH_SEGMENT;
        }

        @Override
        public String toString() {
            return url();
        }

    }
}