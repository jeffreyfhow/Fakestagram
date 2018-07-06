package com.jeffreyfhow.fakestagram.data;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.Weeks;

/**
 * Data Model for a single Fakestagram Post
 */
public class Post {

    private String postId;
    private Long userId;
    private String username;
    private String profilePictureUrl;

    private Long likeCnt;
    private LocalDateTime dateCreated;

    private String imgUrlStd;
    private String imgUrlThumb;
    private String imgLowRes;

    private Boolean userHasLiked;

    public Post(String postId, Long userId, String username, String profilePictureUrl, Long likeCnt,
                LocalDateTime dateCreated, String imgUrlStd, String imgUrlThumb, String imgLowRes,
                Boolean userHasLiked) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.likeCnt = likeCnt;
        this.dateCreated = dateCreated;
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public String getRecencyString(){
        LocalDateTime now = LocalDateTime.now();

        Weeks weeksDuration = Weeks.weeksBetween(dateCreated, now);
        int numWeeks = weeksDuration.getWeeks();
        if(numWeeks > 0){
            return numWeeks + "w ago";
        }

        Period period = new Period(dateCreated, now);

        int numDays = period.getDays();
        if(numDays > 0){
            return numDays + "d ago";
        }

        int numHours = period.getHours();
        if(numHours > 0){
            return numHours + "h ago";
        }

        int numMins = period.getMinutes();
        if(numMins > 0){
            return numMins + "m ago";
        }

        return period.getSeconds() + "s ago";
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
