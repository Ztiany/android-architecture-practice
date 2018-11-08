package com.app.base.data;


import com.android.sdk.net.errorhandler.ErrorHandler;
import com.app.base.widget.dialog.TipsManager;

public class AppErrorHandler implements ErrorHandler {

    @Override
    public CharSequence createMessage(Throwable throwable) {
        return ErrorHandler.createDefaultErrorMessage(throwable);
    }

    @Override
    public void handleError(Throwable throwable) {
        TipsManager.showMessage(createMessage(throwable));
    }

}