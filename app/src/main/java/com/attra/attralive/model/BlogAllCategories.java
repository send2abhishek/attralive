package com.attra.attralive.model;

public class BlogAllCategories {
    int imageId;
    String categories;

    public BlogAllCategories(int imageId, String categories) {
        this.imageId = imageId;
        this.categories = categories;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
