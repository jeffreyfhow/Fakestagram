package com.jeffreyfhow.fakestagram.DataStructures;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Posts {
    @SerializedName("data")
    @Expose
    private ArrayList<Post> data;

    public Posts(ArrayList<Post> data) {
        this.data = data;
    }

    public ArrayList<Post> getData() {
        return data;
    }
}
