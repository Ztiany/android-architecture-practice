package com.gwchina.sdk.social.share;

import android.os.Parcel;
import android.os.Parcelable;

public class Share implements Parcelable {

    private String content;
    private String image;
    private String title;
    private String url;

    @SuppressWarnings("all")
    public Share() {
    }

    @SuppressWarnings("all")
    protected Share(Parcel in) {
        content = in.readString();
        image = in.readString();
        title = in.readString();
        url = in.readString();
    }

    public static final Creator<Share> CREATOR = new Creator<Share>() {
        @Override
        public Share createFromParcel(Parcel in) {
            return new Share(in);
        }

        @Override
        public Share[] newArray(int size) {
            return new Share[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(url);
    }

    String getContent() {
        return content;
    }

    public Share setContent(String content) {
        this.content = content;
        return this;
    }

    String getImage() {
        return image;
    }

    public Share setImage(String image) {
        this.image = image;
        return this;
    }

    String getTitle() {
        return title;
    }

    public Share setTitle(String title) {
        this.title = title;
        return this;
    }

    String getUrl() {
        return url;
    }

    public Share setUrl(String url) {
        this.url = url;
        return this;
    }

}