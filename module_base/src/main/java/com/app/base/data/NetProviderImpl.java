package com.app.base.data;

import android.support.annotation.NonNull;

import com.android.base.utils.android.ResourceUtils;
import com.android.sdk.net.exception.ApiErrorException;
import com.android.sdk.net.provider.ApiHandler;
import com.android.sdk.net.provider.ErrorDataAdapter;
import com.android.sdk.net.provider.ErrorMessage;
import com.android.sdk.net.provider.HttpConfig;
import com.android.sdk.net.provider.NetProvider;
import com.app.base.BuildConfig;
import com.app.base.R;
import com.app.base.data.api.ApiHelper;
import com.blankj.utilcode.util.NetworkUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:16
 */
public class NetProviderImpl implements NetProvider {

    private ApiHandler mApiHandler = result -> {
        //todo
    };

    private HttpConfig mHttpConfig = new HttpConfig() {

        private static final int CONNECTION_TIME_OUT = 10;
        private static final int IO_TIME_OUT = 20;

        @Override
        public void configHttp(OkHttpClient.Builder builder) {
            configDebug(builder)
                    .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(IO_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(IO_TIME_OUT, TimeUnit.SECONDS)
                    .hostnameVerifier((hostname, session) -> {
                        Timber.d("hostnameVerifier called with: hostname 、session = [" + hostname + "、" + session.getProtocol() + "]");
                        return true;
                    });
        }

        OkHttpClient.Builder configDebug(OkHttpClient.Builder builder) {
            if (BuildConfig.openDebug) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(
                        message -> Timber.tag("===OkHttp===").i(message)
                );
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor)
                        .addNetworkInterceptor(new StethoInterceptor());
            }
            return builder;
        }

        @Override
        public boolean configRetrofit(Retrofit.Builder builder) {
            return false;
        }

        @Override
        public String baseUrl() {
            return URLProvider.baseUrl();
        }
    };


    private ErrorMessage mErrorMessage = new ErrorMessage() {
        @Override
        public CharSequence netErrorMessage(Throwable exception) {
            return ResourceUtils.getString(R.string.error_net_error);
        }

        @Override
        public CharSequence serverDataErrorMessage(Throwable exception) {
            return ResourceUtils.getString(R.string.error_service_data_error);
        }

        @Override
        public CharSequence serverErrorMessage(Throwable exception) {
            return ResourceUtils.getString(R.string.error_service_error);
        }

        @Override
        public CharSequence clientRequestErrorMessage(Throwable exception) {
            return ResourceUtils.getString(R.string.error_request_error);
        }

        @Override
        public CharSequence apiErrorMessage(ApiErrorException exception) {
            return ResourceUtils.getString(R.string.error_api_code_mask_tips, exception.getCode());
        }

        @Override
        public CharSequence unknowErrorMessage(Throwable exception) {
            return ResourceUtils.getString(R.string.error_unknow);
        }

    };

    private ErrorDataAdapter mErrorDataAdapter = new ErrorDataAdapter() {
        @Override
        public Object createErrorDataStub(Type type, Annotation[] annotations, Retrofit retrofit, ResponseBody value) {
            return ApiHelper.newErrorDataStub();
        }

        @Override
        public boolean isErrorDataStub(Object object) {
            return ApiHelper.isDataError(object);
        }
    };

    @Override
    public boolean isConnected() {
        return NetworkUtils.isConnected();
    }

    @NonNull
    @Override
    public ApiHandler aipHandler() {
        return mApiHandler;
    }

    @NonNull
    @Override
    public HttpConfig httpConfig() {
        return mHttpConfig;
    }

    @NonNull
    @Override
    public ErrorMessage errorMessage() {
        return mErrorMessage;
    }

    @NonNull
    @Override
    public ErrorDataAdapter errorDataAdapter() {
        return mErrorDataAdapter;
    }

}
