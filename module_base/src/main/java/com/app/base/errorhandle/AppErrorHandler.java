package com.app.base.errorhandle;


import com.app.base.widget.dialog.TipsManager;

public class AppErrorHandler implements ErrorHandler {

    @Override
    public String createMessage(Throwable throwable) {
        return ErrorMessageFactory.createMessage(throwable);
    }

    @Override
    public void handleError(Throwable throwable) {
        TipsManager.showMessage(createMessage(throwable));
    }

}