package com.app.base.dialog.toast;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.android.base.utils.android.views.ResourceEx;
import com.google.android.material.snackbar.Snackbar;


public class ToastKit {

    public static void showMessage(Context context, @StringRes int msgId) {
        showMessage(context, context.getText(msgId));
    }

    public static void showMessage(Context context, CharSequence message) {
        if (isLongMessage(message)) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showMessage(View anchor, CharSequence message) {
        if (isLongMessage(message)) {
            Snackbar.make(anchor, message, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showMessage(View anchor, CharSequence message, CharSequence actionText, View.OnClickListener onClickListener) {
        if (isLongMessage(message)) {
            Snackbar.make(anchor, message, Snackbar.LENGTH_LONG).setAction(actionText, onClickListener).show();
        } else {
            Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT).setAction(actionText, onClickListener).show();
        }
    }

    private static boolean isLongMessage(CharSequence message) {
        return message.length() >= 20;
    }

}