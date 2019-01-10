package com.attra.attralive.model;

import android.widget.ImageView;

public class NewsFeed {

    int  noOfLikes,noOfCommenst;
    String imageId, userid,postID;
            String newsFeedImage;
    String userName,location,feedTime,feedDescription;

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
