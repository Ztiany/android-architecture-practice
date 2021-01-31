package com.app.base.umeng;

import com.app.base.push.PushMessage;
import com.umeng.message.entity.UMessage;

final class UmengUtils {

    static PushMessage convertToMessage(UMessage uMessage) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setContent(uMessage.custom);
        pushMessage.setText(uMessage.text);
        pushMessage.setMessageId(uMessage.msg_id);
        pushMessage.setExtra(uMessage.extra);
        return pushMessage;
    }

}
