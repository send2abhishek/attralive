package com.attra.attralive.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReplyQuestionComment implements Parcelable {
    public ReplyQuestionComment(String name, String reply, String replyTime) {
        this.name = name;
        this.reply = reply;
        this.time = replyTime;
    }

    String name;
    String reply;
    String time;

    protected ReplyQuestionComment(Parcel in) {
        name = in.readString();
        reply = in.readString();
        time = in.readString();
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

    public String getImageId() {
        return time;
    }

    public void setImageId(String imageId) {
        this.time = imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(reply);
        dest.writeString(time);
    }
}
