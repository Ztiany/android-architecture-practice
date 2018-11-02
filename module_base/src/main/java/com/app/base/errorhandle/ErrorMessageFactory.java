package com.app.base.errorhandle;


import android.text.TextUtils;

import com.android.base.utils.android.ResourceUtils;
import com.app.base.R;
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
            string = ResourceUtils.getString(R.string.error_net_error);
        }
        //2：服务器错误处理
        else if (exception instanceof ServerErrorException) {

            int errorType = ((ServerErrorException) exception).getErrorType();
            if (errorType == ServerErrorException.SERVER_DATA_ERROR) {
                string = ResourceUtils.getString(R.string.error_service_data_error);
            } else if (errorType == ServerErrorException.UNKNOW_ERROR) {
                string = ResourceUtils.getString(R.string.error_service_data_error);
            }
        }
        //3：响应码非200处理
        else if (exception instanceof HttpException) {
            int code = ((HttpException) exception).code();
            if (code >= 500/*http 500 表示服务器错误*/) {
                string = ResourceUtils.getString(R.string.error_service_data_error);
            } else if (code >= 400/*http 400 表示客户端请求出错*/) {
                string = ResourceUtils.getString(R.string.error_request_error);
            }
        } else {
            //4：api 错误处理
            if (exception instanceof ApiErrorException) {
                string = exception.getMessage();
                if (TextUtils.isEmpty(string)) {
                    string = ResourceUtils.getString(R.string.error_api_code_mask_tips, ((ApiErrorException) exception).getCode());
                }
            } else {
                throw new RuntimeException(exception);
            }
        }

        if (isEmpty(string)) {
            string = ResourceUtils.getString(R.string.error_unknow);
        }

        return string;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }

}
