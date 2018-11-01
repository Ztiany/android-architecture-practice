package com.app.base.data;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

class URLProvider {

    private static final Map<String, HttpURL> mHttpURLMap = new HashMap<>();

    @Inject
    URLProvider() {
    }

    static {
        /*调试环境*/
//        mHttpURLMap.put(DataContext.buildEnvTag(DataContext.ENVIRONMENT_CN, DataContext.BUILD_DEBUG), new HttpURL("", 8081));
//        mHttpURLMap.put(DataContext.buildEnvTag(DataContext.ENVIRONMENT_EN, DataContext.BUILD_DEBUG), new HttpURL("120.77.170.52", 8082));
        /*预发布*/
//        mHttpURLMap.put(DataContext.buildEnvTag(DataContext.ENVIRONMENT_EN, DataContext.BUILD_PRE_RELEASE), new HttpURL("", 8081));
//        mHttpURLMap.put(DataContext.buildEnvTag(DataContext.ENVIRONMENT_CN, DataContext.BUILD_PRE_RELEASE), new HttpURL("112.74.112.220", 8081));
        /*正式环境*/
//        mHttpURLMap.put(DataContext.buildEnvTag(DataContext.ENVIRONMENT_EN, DataContext.BUILD_RELEASE), new HttpURL("", false));
//        mHttpURLMap.put(DataContext.buildEnvTag(DataContext.ENVIRONMENT_CN, DataContext.BUILD_RELEASE), new HttpURL("", false));
    }

    String baseUrl() {
//        DataContext instance = DataContext.getInstance();
//        return mHttpURLMap.get(instance.getEnvTag()).url();
        return "http://www.fake.com/api/";
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