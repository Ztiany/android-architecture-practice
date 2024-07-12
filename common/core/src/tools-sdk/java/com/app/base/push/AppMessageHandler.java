package com.app.base.push;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import androidx.annotation.NonNull;
import timber.log.Timber;


final class AppMessageHandler implements MessageHandler {

    private boolean mEnable = true;

    @Override
    public void onDirectMessageArrived(@NonNull @NotNull PushMessage pushMessage) {
        Timber.d("onDirectMessageArrived() called with: pushMessage = [" + pushMessage + "]");
        try {
            processDirectMessage(pushMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleOnNotificationMessageClicked(@NotNull PushMessage pushMessage) {
        Timber.d("handleNotificationClickMessage() called with: pushMessage = [" + pushMessage + "], enable = [" + mEnable + "]");
        if (!mEnable) {
            return;
        }
        processNotificationMessageClicked(pushMessage.getExtra());
    }

    @Override
    public void handleOnLaunchApp(@NonNull PushMessage pushMessage) {
        Timber.d("handleOnLaunchApp() called with: pushMessage = [" + pushMessage + "], enable = [" + mEnable + "]");
        if (!mEnable) {
            return;
        }
        Timber.d("extra = %s", pushMessage.getExtra());
        processNotificationMessageClicked(pushMessage.getExtra());
    }

    @Override
    public void onNotificationMessageArrived(@NotNull PushMessage pushMessage) {
        Timber.d("onNotificationMessageArrived() called with: pushMessage = [" + pushMessage + "]");
    }

    void setEnable(boolean enable) {
        mEnable = enable;
    }

    /**
     * 点击顶部消息，处理跳转。
     */
    private void processNotificationMessageClicked(Map<String, String> extra) {
    }

    /**
     * 透传，分发事件。
     */
    private void processDirectMessage(PushMessage pushMessage) {

    }

}