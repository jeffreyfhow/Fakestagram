package com.jeffreyfhow.fakestagram;

public class Post {

    private String postId;
    private Long userId;
    private String username;
    private String profilePictureUrl;

    private Long likeCnt;
    private Long timeCreated;

    private String imgUrlStd;
    private String imgUrlThumb;
    private String imgLowRes;

    private Boolean userHasLiked;

    public Post(String postId, Long userId, String username, String profilePictureUrl, Long likeCnt,
                Long timeCreated, String imgUrlStd, String imgUrlThumb, String imgLowRes,
                Boolean userHasLiked) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.likeCnt = likeCnt;
        this.timeCreated = timeCreated;
        this.imgUrlStd = imgUrlStd;
        this.imgUrlThumb = imgUrlThumb;
        this.imgLowRes = imgLowRes;
        this.userHasLiked = userHasLiked;
    }

    public String getPostId() {

        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public Long getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(Long likeCnt) {
        this.likeCnt = likeCnt;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public String getImgUrlStd() {
        return imgUrlStd;
    }

    public String getImgUrlThumb() {
        return imgUrlThumb;
    }

    public String getImgLowRes() {
        return imgLowRes;
    }

    public Boolean getUserHasLiked() {
        return userHasLiked;
    }

    public void setUserHasLiked(Boolean userHasLiked) {
        this.userHasLiked = userHasLiked;
    }

}
