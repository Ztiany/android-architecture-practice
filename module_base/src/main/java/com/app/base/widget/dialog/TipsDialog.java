package com.app.base.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.base.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;


/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-05-02 14:37
 */
class TipsDialog extends AppCompatDialog {

    private TextView mMessageTv;

    TipsDialog(Context context) {
        super(context, R.style.ThemeDialogTips);
        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.windowAnimations = R.style.StyleAnimFadeIn;
        setView();
    }

    private void setView() {
        setContentView(R.layout.dialog_tips);
        mMessageTv = findViewById(R.id.dialogTipsTvMessage);
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

    public void setIcon(@DrawableRes int icon) {
        if (icon != 0) {
            mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
        }
    }

    public void setIconType(@TipsType int type) {
        switch (type) {
            case TipsDialogBuilder.TYPE_SUCCESS: {
                mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_success_tips, 0, 0);

            }
            case TipsDialogBuilder.TYPE_FAILURE: {
                mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_failure_tips, 0, 0);
            }
            case TipsDialogBuilder.TYPE_WARNING: {
                mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_warning_tips, 0, 0);
            }
        }
    }

}