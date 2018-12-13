package com.attra.attralive.model;

public class SearchResult1 {
    String topic;
    int watchcount,answercount;

    public SearchResult1(String topic, int watchcount, int answercount) {
        this.topic = topic;
        this.watchcount = watchcount;
        this.answercount = answercount;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getWatchcount() {
        return watchcount;
    }

    public void setWatchcount(int watchcount) {
        this.watchcount = watchcount;
    }

    public int getAnswercount() {
        return answercount;
    }

    public void setAnswercount(int answercount) {
        this.answercount = answercount;
    }
}
