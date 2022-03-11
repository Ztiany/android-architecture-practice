package com.app.base.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.app.base.R;


/**
 * @author Ztiany
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

    public void setTipsType(@TipsType int type) {
        switch (type) {
            case TipsDialogBuilder.TYPE_SUCCESS: {
                mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tips_success, 0, 0);
                mMessageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }
            break;
            case TipsDialogBuilder.TYPE_FAILURE: {
                mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tips_failed, 0, 0);
                mMessageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.app_tips_warn));
            }
            break;
            case TipsDialogBuilder.TYPE_WARNING: {
                mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tips_failed, 0, 0);
                mMessageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.app_tips_warn));
            }
            break;
        }
    }

}