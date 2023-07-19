package com.app.base.push;

import androidx.annotation.NonNull;

/**
 * @author Ztiany
 * Date : 2017-03-09 11:31
 */
public interface MessageHandler {

    /**
     * 处理透传消息
     *
     * @param pushMessage 消息
     */
    void onDirectMessageArrived(@NonNull PushMessage pushMessage);

    /**
     * 自定义消息通知栏消息被点击
     */
    void handleOnNotificationMessageClicked(@NonNull PushMessage pushMessage);

    /**
     * 点击消息打开了 APP
     */
    void handleOnLaunchApp(@NonNull PushMessage pushMessage);

    /**
     * 通知栏消息到达
     */
    void onNotificationMessageArrived(@NonNull PushMessage pushMessage);

}
