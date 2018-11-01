package com.app.base.widget.dialog;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;


public class TipsManager {

    public static void showMessage(CharSequence message) {
        ToastUtils.showShort(message);
    }

    public static void showMessage(View anchor, CharSequence message) {
        Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showMessage(View anchor, CharSequence message, CharSequence actionText, View.OnClickListener onClickListener) {
        Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT)
                .setAction(actionText, onClickListener)
                .show();
    }

}
