package com.attra.attralive.model;

public class ForumQuestionListModel {

   String id;
   String  user;
   String topic;
   int watch_count;

    public ForumQuestionListModel(String id, String user, String topic, int watch_count) {
        this.id = id;
        this.user = user;
        this.topic = topic;
        this.watch_count = watch_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getWatch_count() {
        return watch_count;
    }

    public void setWatch_count(int watch_count) {
        this.watch_count = watch_count;
    }
}
