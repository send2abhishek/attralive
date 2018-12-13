package com.attra.attralive.model;

public class MyTaskList {
    int imageId;

    public MyTaskList(int imageId, String noTitle, String noSubTitle, String noTime) {
        this.imageId = imageId;
        this.noTitle = noTitle;
        this.noSubTitle = noSubTitle;
        this.noTime = noTime;
    }

    String noTitle,noSubTitle,noTime;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getNoTitle() {
        return noTitle;
    }

    public void setNoTitle(String noTitle) {
        this.noTitle = noTitle;
    }

    public String getNoSubTitle() {
        return noSubTitle;
    }

    public void setNoSubTitle(String noSubTitle) {
        this.noSubTitle = noSubTitle;
    }

    public String getNoTime() {
        return noTime;
    }

    public void setNoTime(String noTime) {
        this.noTime = noTime;
    }


}
