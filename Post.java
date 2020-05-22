package com.noorbala7418.instaghw;

public class Post {
    private String pauth;
    private int postNum;
    private String postCap;
    private int postId;
    private int postLikes;

    public String getPauth() {
        return pauth;
    }

    public int getPostId() {
        return postId;
    }

    public int getPostLikes() {
        return postLikes;
    }

    public int getPostNum() {
        return postNum;
    }

    public String getPostCap() {
        return postCap;
    }

    public void setPostCap(String postCap) {
        this.postCap = postCap;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setPostLikes(int postLikes) {
        this.postLikes = postLikes;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public void setPauth(String pauth) {
        this.pauth = pauth;
    }
}
