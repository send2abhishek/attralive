package com.attra.attralive.model;

import android.widget.ImageView;

public class NewsFeed {

    String imageId,newsFeedImage;
    String userName,title,feedTime,feedDescription,noOfLikes,noOfCommenst;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getNewsFeedImage() {
        return newsFeedImage;
    }

    public void setNewsFeedImage(String newsFeedImage) {
        this.newsFeedImage = newsFeedImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(String feedTime) {
        this.feedTime = feedTime;
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String feedDescription) {
        this.feedDescription = feedDescription;
    }

    public String getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(String noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public String getNoOfCommenst() {
        return noOfCommenst;
    }

    public void setNoOfCommenst(String noOfCommenst) {
        this.noOfCommenst = noOfCommenst;
    }


//    public NewsFeed(int imageId, int newsFeedImage, String userName, String title, String feedTime, String feedDescription, String noOfLikes, String noOfCommenst) {
//        this.imageId = imageId;
//        this.newsFeedImage = newsFeedImage;
//        this.userName = userName;
//        this.title = title;
//        this.feedTime = feedTime;
//        this.feedDescription = feedDescription;
//        this.noOfLikes = noOfLikes;
//        this.noOfCommenst = noOfCommenst;
//    }

    public NewsFeed(String image, String newsFeedImage, String userName, String title, String feedTime, String feedDescription, String noOfLikes, String noOfCommenst) {
        this.imageId = image;
        this.newsFeedImage = newsFeedImage;
        this.userName = userName;
        this.title = title;
        this.feedTime = feedTime;
        this.feedDescription = feedDescription;
        this.noOfLikes = noOfLikes;
        this.noOfCommenst = noOfCommenst;
    }






}
