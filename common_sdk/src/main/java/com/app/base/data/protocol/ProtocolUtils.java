package com.app.base.data.protocol;

import androidx.annotation.RestrictTo;

import com.blankj.utilcode.util.AppUtils;
import com.android.common.api.usermanager.User;
import com.android.common.api.usermanager.UserManager;
import com.android.sdk.net.core.https.HttpsUtils;
import com.app.base.app.AndroidPlatform;
import com.app.base.debug.Debug;

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
    public static void trustAllCertificationChecked(OkHttpClient.Builder builder) {
        if (Debug.trustHttpsCertification()) {
            HttpsUtils.SSLParams sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null);
            builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager)
                    .hostnameVerifier((hostname, session) -> true);
        }
    }

    static void processHeader(UserManager userManager, AndroidPlatform androidPlatform, Request.Builder newBuilder) {
        newBuilder.header(ApiParameter.HEADER_OS_KEY, ApiParameter.HEADER_OS_VALUE);
        newBuilder.header(ApiParameter.HEADER_VERSION_NAME_KEY, AppUtils.getAppVersionName());
        newBuilder.header(ApiParameter.HEADER_VERSION_CODE_KEY, String.valueOf(AppUtils.getAppVersionCode()));
        newBuilder.header(ApiParameter.HEADER_BRAND_KEY, android.os.Build.BRAND);
        newBuilder.header(ApiParameter.HEADER_DEVICE_ID_KEY, androidPlatform.getDeviceId());
        User user = userManager.getUser();
        if (user != User.Companion.getNOT_LOGIN()) {
            newBuilder.header(ApiParameter.HEADER_TOKEN_KEY, user.getToken());
        }
    }

    static RequestBody createJsonBody(String content) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content);
    }

}