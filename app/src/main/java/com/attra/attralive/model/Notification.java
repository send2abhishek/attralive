package com.attra.attralive.model;

public class Notification {
    private int id;
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
    }

}
