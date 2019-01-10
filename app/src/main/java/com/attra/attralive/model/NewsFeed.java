package com.attra.attralive.model;

import android.widget.ImageView;

public class NewsFeed {

    String imageId;
    String newsFeedImage;

    public NewsFeed(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    String postId;
    String userName;

    public NewsFeed(String imageId, String newsFeedImage, String postId, String userName, String title, String feedTime, String feedDescription, String location, String userid, String postID, int noOfLikes, int noOfCommenst) {
        this.imageId = imageId;
        this.newsFeedImage = newsFeedImage;
        this.postId = postId;
        this.userName = userName;
        this.title = title;
        this.feedTime = feedTime;
        this.feedDescription = feedDescription;
        this.location = location;
        this.userid = userid;
        this.postID = postID;
        this.noOfLikes = noOfLikes;
        this.noOfCommenst = noOfCommenst;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;
    String feedTime;
    String feedDescription;
    String location;
    String userid;
    String postID;
    int  noOfLikes,noOfCommenst;



    public String getPostID() { return postID;}

    public void setPostID(String postId){ this.postID = postId; }

    public String getUserid() {return userid;}

    public void setUserid(String  userid){this.userid = userid;}

    public String getImageId() { return imageId;}

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFeedTime() {
        return feedTime;
    }

    public String setFeedTime(String feedTime) {
        return this.feedTime = feedTime;
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String feedDescription) {
        this.feedDescription = feedDescription;
    }

    public int getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(int noOfLikes) {
         this.noOfLikes = noOfLikes;
    }

    public int getNoOfCommenst() {
        return noOfCommenst;
    }

    public void setNoOfCommenst(int noOfCommenst) {
        this.noOfCommenst = noOfCommenst;
    }


    public NewsFeed(String  userId, String postId, String image, String newsFeedImage, String userName,
                    String location, String feedTime, String feedDescription, int noOfLikes, int noOfCommenst) {
       this.userid = userId;
        this.postID = postId;
        this.imageId = image;
        this.newsFeedImage = newsFeedImage;
        this.userName = userName;
        this.location = location;
        this.feedTime = feedTime;
        this.feedDescription = feedDescription;
        this.noOfLikes = noOfLikes;
        this.noOfCommenst = noOfCommenst;
    }






}
