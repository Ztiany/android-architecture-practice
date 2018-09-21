package com.app.base.data.net.errorhandler;


import com.app.base.data.net.ApiHelper;
import com.app.base.data.net.exception.ApiErrorException;

public class AppErrorHandler {

    public static String createMessage(Throwable throwable) {
        return ErrorMessageFactory.createMessage(throwable);
    }

    public static void handleError(Throwable throwable) {
        //show(createMessage(throwable));
    }

    public static boolean isAuthenticationExpired(Throwable throwable) {
        return throwable instanceof ApiErrorException && ApiHelper.isKeyInvalid(((ApiErrorException) throwable).getCode());
    }

}