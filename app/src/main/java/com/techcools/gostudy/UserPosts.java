package com.techcools.gostudy;

public class UserPosts {

    private String postUrl, userEmail, userID;

    public UserPosts(){}

    public UserPosts(String postUrl, String userEmail, String userID) {
        this.postUrl = postUrl;
        this.userEmail = userEmail;
        this.userID = userID;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
