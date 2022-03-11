package com.app.base.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;

import com.app.base.R;


/**
 * @author Ztiany
 * Date : 2017-05-02 14:37
 */
public class LoadingDialog extends AppCompatDialog {

    private TextView mMessageTv;

    public LoadingDialog(Context context) {
        super(context);
        setView();
    }

    private void setView() {
        setContentView(R.layout.dialog_loading);
        mMessageTv = findViewById(R.id.dialog_loading_tv_title);
    }

    public void setMessage(CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            mMessageTv.setText(message);
        }
    }

    public void setMessage(@StringRes int messageId) {
        if (messageId != 0) {
            mMessageTv.setText(messageId);
        }
    }

}
