package com.attra.attralive.model;

public class AllAnswers {

    public AllAnswers(int votescount, int watchcount, int likescount, int dislikecount, int rating,
                      String answer, String usercreatedans, String anscreatedate, String userlocation, String ansId) {
        this.votescount = votescount;
        this.watchcount = watchcount;
        this.likescount = likescount;
        this.dislikecount = dislikecount;
        this.rating = rating;
        this.answer = answer;
        this.usercreatedans = usercreatedans;
        this.anscreatedate = anscreatedate;
        this.userlocation = userlocation;
        this.ansId = ansId;
    }

    int votescount,watchcount,likescount,dislikecount,rating;
    String answer;
    String usercreatedans;
    String anscreatedate;
    String userlocation;
    String ansId;

    public String getAnsId() {
        return ansId;
    }

    public void setAnsId(String ansId) {
        this.ansId = ansId;
    }



    public int getVotescount() {
        return votescount;
    }

    public void setVotescount(int votescount) {
        this.votescount = votescount;
    }

    public int getWatchcount() {
        return watchcount;
    }

    public void setWatchcount(int watchcount) {
        this.watchcount = watchcount;
    }

    public int getLikescount() {
        return likescount;
    }

    public void setLikescount(int likescount) {
        this.likescount = likescount;
    }

    public int getDislikecount() {
        return dislikecount;
    }

    public void setDislikecount(int dislikecount) {
        this.dislikecount = dislikecount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUsercreatedans() {
        return usercreatedans;
    }

    public void setUsercreatedans(String usercreatedans) {
        this.usercreatedans = usercreatedans;
    }

    public String getAnscreatedate() {
        return anscreatedate;
    }

    public void setAnscreatedate(String anscreatedate) {
        this.anscreatedate = anscreatedate;
    }

    public String getUserlocation() {
        return userlocation;
    }

    public void setUserlocation(String userlocation) {
        this.userlocation = userlocation;
    }
}
