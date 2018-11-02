package com.app.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;

import com.app.base.R;

public class DialogManager {

    private DialogManager() {
        throw new UnsupportedOperationException();
    }

    public static Dialog createLoadingDialog(Context context) {
        return createLoadingDialog(context, false, false);
    }

    public static Dialog createLoadingDialog(Context context, boolean autoShow) {
        return createLoadingDialog(context, autoShow, false);
    }

    public static Dialog createLoadingDialog(Context context, boolean autoShow, boolean horizontal) {
        LoadingDialog loadingDialog = new LoadingDialog(context, horizontal);
        loadingDialog.setMessage(R.string.dialog_loading);
        if (autoShow) {
            loadingDialog.show();
        }
        return loadingDialog;
    }

}