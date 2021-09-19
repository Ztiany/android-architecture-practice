package com.app.base.data.protocol;

import androidx.annotation.RestrictTo;

import com.android.sdk.net.core.https.HttpsUtils;
import com.app.base.services.usermanager.AppDataSource;
import com.app.base.debug.Debug;
import com.blankj.utilcode.util.AppUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ProtocolUtils {

    /**
     * trust all.
     *
     * @see <a href='https://stackoverflow.com/questions/51563859/javax-net-ssl-sslhandshakeexception-java-lang-illegalargumentexception-invalid'>javax-net-ssl-sslhandshakeexception-java-lang-illegalargumentexception-invalid</a>
     */
    public static void trustAllCertificationChecked(AppDataSource appDataSource, OkHttpClient.Builder builder) {
        if (Debug.trustHttpsCertification()) {
            HttpsUtils.SSLParams sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null);
            builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager)
                    .hostnameVerifier((hostname, session) -> true);
        }
    }

    static void processHeader(AppDataSource appDataSource, Request.Builder newBuilder) {
        //头部：X-Platform
        newBuilder.header(ApiParameter.HEADER_PLATFORM_NAME, ApiParameter.HEADER_PLATFORM_VALUE);
        /*头部：版本号*/
        newBuilder.header(ApiParameter.HEADER_VERSION_NAME, AppUtils.getAppVersionName());
        //头部：Token
        if (appDataSource.userLogined()) {
            newBuilder.header(ApiParameter.HEADER_TOKEN_NAME, "get token from appDataSource");
        }
    }

    static RequestBody createJsonBody(String content) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content);
    }

}