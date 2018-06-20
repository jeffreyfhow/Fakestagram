package com.jeffreyfhow.fakestagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class User {
    @SerializedName("id")
    @Expose
    private Long userId;

    @SerializedName("profile_picture")
    @Expose
    private String profilePictureUrl;

    @SerializedName("username")
    @Expose
    private String username;

    public User(Long userId, String profilePictureUrl, String username) {
        this.userId = userId;
        this.profilePictureUrl = profilePictureUrl;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(profilePictureUrl, user.profilePictureUrl) &&
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, profilePictureUrl, username);
    }
}
