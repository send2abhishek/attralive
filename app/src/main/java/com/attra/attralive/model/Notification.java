package com.attra.attralive.model;

import java.io.Serializable;

public class Notification implements Serializable {
    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }



    /* private int id;
        private String title;
        private String shortdesc;


        public Notification(int id, String title, String shortdesc) {
            this.id = id;
            this.title = title;
            this.shortdesc = shortdesc;

        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getShortdesc() {

            return shortdesc;
        }*/
   private String postType;
    private String postId;
    private String ownerId;

    private String action;
    private String     userId;

    public Notification(String postType, String postId, String ownerId, String action, String userId,
                        String userName, String time, String postMessage, String userImage,  String readStatus) {
        this.postType = postType;
        this.postId = postId;
        this.ownerId = ownerId;

        this.action = action;
        this.userId = userId;
        this.userName = userName;
        this.time = time;
        this.postMessage = postMessage;
        this.userImage = userImage;

        this.readStatus = readStatus;
    }

    private String userName;

    private String   time;
    private String postMessage;
    private String userImage;
    private String readStatus;

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }



}
