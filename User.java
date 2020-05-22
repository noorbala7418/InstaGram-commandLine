package com.noorbala7418.instaghw;

public class User {
    private String username ;
    private int uid ;
    private String password ;
    private String bio ;
    private int follower ;
    private int following ;
    private int posts ;


    public void setUsername(String username) {
        this.username = username;
    }

    public int getUid() {
        return uid;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getPosts() {
        return posts;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFollower() {
        return follower;
    }

    public int getFollowing() {
        return following;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}
