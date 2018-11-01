package com.app.base.errorhandle;


import com.app.base.data.net.exception.ApiErrorException;
import com.app.base.data.net.exception.NetworkErrorException;
import com.app.base.data.net.exception.ServerErrorException;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.HttpException;
import timber.log.Timber;


/**
 * @author Ztiany
 */
final class ErrorMessageFactory {

    private ErrorMessageFactory() {
        throw new UnsupportedOperationException();
    }

    static String createMessage(Throwable exception) {
        Timber.d("createMessage with：" + exception);

        String string = null;
        //SocketTimeoutException android NetworkErrorException extends IOException
        //1：网络连接错误处理
        if (exception instanceof ConnectException ||
                exception instanceof IOException
                || exception instanceof NetworkErrorException) {
            string = "网络连接不可用，请稍后再试！";
        }
        //2：服务器错误处理
        else if (exception instanceof ServerErrorException) {

            int errorType = ((ServerErrorException) exception).getErrorType();
            if (errorType == ServerErrorException.SERVER_DATA_ERROR) {
                string = "服务器数据异常";
            } else if (errorType == ServerErrorException.UNKNOW_ERROR) {
                string = "服务器错误！";
            }
        }
        //3：响应码非200处理
        else if (exception instanceof HttpException) {
            int code = ((HttpException) exception).code();
            if (code >= 500/*http 500 表示服务器错误*/) {
                string = "服务器错误！";
            } else if (code >= 400/*http 400 表示客户端请求出错*/) {
                string = "请求失败！";
            }
        } else {
            //4：api 错误处理
            if (exception instanceof ApiErrorException) {
                string = exception.getMessage();
            } else {
                throw new RuntimeException(exception);
            }
        }

        if (isEmpty(string)) {
            string = "未知错误";
        }

        return string;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }

}
