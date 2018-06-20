package com.jeffreyfhow.fakestagram.DataStructures;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Likes {
    @SerializedName("count")
    @Expose
    private Integer count;

    public Likes(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
