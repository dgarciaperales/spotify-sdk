package com.example.spotify_sdk;


public class CommentData {
    private String comment;
    private int id;
    private String userEmail;
    public CommentData(String comment, int id, String userEmail) {
        this.comment = comment;
        this.id = id;
        this.userEmail = userEmail;
    }
    public String getComment() {
        return comment;
    }
    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
