package com.attra.attralive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sirisha.kalluri on 1/29/2018.
 */

public class QuestionAnswer implements Parcelable {
    int questionId;
    String questionName;
    String answer;

    public QuestionAnswer() {
        
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuestionAnswer(Parcel in) {
        questionId = in.readInt();
        questionName = in.readString();
        answer = in.readString();
    }

    public static final Creator<QuestionAnswer> CREATOR = new Creator<QuestionAnswer>() {
        @Override
        public QuestionAnswer createFromParcel(Parcel in) {
            return new QuestionAnswer(in);
        }

        @Override
        public QuestionAnswer[] newArray(int size) {
            return new QuestionAnswer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(questionId);
        parcel.writeString(questionName);
        parcel.writeString(answer);
    }
}
