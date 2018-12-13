package com.attra.attralive.model;

public class RepliesForAnswerComments {
    String userName, commentsReplies, timeStamp;

    public RepliesForAnswerComments( String userName,String comments, String timeStamp) {
        this.commentsReplies = comments;
        this.userName = userName;
        this.timeStamp = timeStamp;
    }

    public String getComments() {
        return commentsReplies;
    }

    public void setComments(String comments) {
        this.commentsReplies = comments;
    }

    public String getUser() {
        return userName;
    }

    public void setUser(String user) {
        this.userName = user;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}


