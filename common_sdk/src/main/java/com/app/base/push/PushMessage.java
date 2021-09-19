package com.app.base.push;


import androidx.annotation.NonNull;

import java.util.Map;

public class PushMessage {

    private String messageId;

    private String title;
    private String text;
    private String content;
    private Map<String, String> extra;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return "PushMessage{" +
                "messageId=" + messageId +
                ", content='" + content + '\'' +
                ", extra='" + extra + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}