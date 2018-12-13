package com.attra.attralive.model;

public class QuestionComments {
    String comments;
    String user;
    String location;

    public QuestionComments(String comments, String user, String location) {
        this.comments = comments;
        this.user = user;
        this.location = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
