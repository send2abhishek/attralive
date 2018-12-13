package com.attra.attralive.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReplyBlogComments implements Parcelable {
    public ReplyBlogComments(String name, String reply, int imageId) {
        this.name = name;
        this.reply = reply;
        this.imageId = imageId;
    }

    String name;
    String reply;
    int imageId;

    protected ReplyBlogComments(Parcel in) {
        name = in.readString();
        reply = in.readString();
        imageId = in.readInt();
    }

    public static final Creator<ReplyBlogComments> CREATOR = new Creator<ReplyBlogComments>() {
        @Override
        public ReplyBlogComments createFromParcel(Parcel in) {
            return new ReplyBlogComments(in);
        }

        @Override
        public ReplyBlogComments[] newArray(int size) {
            return new ReplyBlogComments[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(reply);
        dest.writeInt(imageId);
    }
}
