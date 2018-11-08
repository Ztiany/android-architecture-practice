package com.android.sdk.net.provider;

import android.support.annotation.NonNull;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 11:05
 */
public interface NetProvider {

    boolean isConnected();

    @NonNull
    ApiHandler aipHandler();

    @NonNull
    HttpConfig httpConfig();

    @NonNull
    ErrorMessage errorMessage();

    @NonNull
    ErrorDataAdapter errorDataAdapter();

}
