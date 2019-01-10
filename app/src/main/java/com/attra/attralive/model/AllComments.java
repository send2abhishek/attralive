package com.attra.attralive.model;

public class AllComments {
    String commentdby,commentmsg,userlocation,userimagepath,commenttime;

    public AllComments(String commentdby, String commentmsg, String userlocation, String userimagepath, String commenttime) {
        this.commentdby = commentdby;
        this.commentmsg = commentmsg;
        this.userlocation = userlocation;
        this.userimagepath = userimagepath;
        this.commenttime = commenttime;
    }

    public String getCommentdby() {
        return commentdby;
    }

    public void setCommentdby(String commentdby) {
        this.commentdby = commentdby;
    }

    public String getCommentmsg() {
        return commentmsg;
    }

    public void setCommentmsg(String commentmsg) {
        this.commentmsg = commentmsg;
    }

    public String getUserlocation() {
        return userlocation;
    }

    public void setUserlocation(String userlocation) {
        this.userlocation = userlocation;
    }

    public String getUserimagepath() {
        return userimagepath;
    }

    public void setUserimagepath(String userimagepath) {
        this.userimagepath = userimagepath;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }
}
