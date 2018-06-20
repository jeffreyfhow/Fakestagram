package com.jeffreyfhow.fakestagram.DataStructures;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("likes")
    @Expose
    private Likes likes;

    @SerializedName("id")
    @Expose
    private String postId;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("created_time")
    @Expose
    private Integer timeCreated;

    @SerializedName("images")
    @Expose
    private Images images;

    public Post(Likes likes, String postId, User user, Integer timeCreated, Images images) {
        this.likes = likes;
        this.postId = postId;
        this.user = user;
        this.timeCreated = timeCreated;
        this.images = images;
    }

    public Likes getLikes() {
        return likes;
    }

    public String getPostId() {
        return postId;
    }

    public User getUser() {
        return user;
    }

    public Integer getTimeCreated() {
        return timeCreated;
    }

    public Images getImages() {
        return images;
    }
}
