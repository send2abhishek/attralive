package com.attra.attralive.model;

public class BlogListModel {
    int imageId;
    Float rating;
    String title,subTitle,publishTime,blogId;

    public BlogListModel(int imageId, Float rating, String title, String subTitle, String publishTime, String blogId) {
        this.imageId = imageId;
        this.rating = rating;
        this.title = title;
        this.subTitle = subTitle;
        this.publishTime = publishTime;
        this.blogId = blogId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }
}
