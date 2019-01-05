package com.attra.attralive.model;

public class NewsFeedNew {
    String userid,description,filepath;

    public NewsFeedNew(String userid, String description, String filepath) {
        this.userid = userid;
        this.description = description;
        this.filepath = filepath;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
