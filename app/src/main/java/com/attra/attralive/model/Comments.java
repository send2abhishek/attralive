package com.attra.attralive.model;

public class Comments {
int imageID;
String comments;
String name;

    public Comments(int imageID, String comments, String name) {
        this.imageID = imageID;
        this.comments = comments;
        this.name = name;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
