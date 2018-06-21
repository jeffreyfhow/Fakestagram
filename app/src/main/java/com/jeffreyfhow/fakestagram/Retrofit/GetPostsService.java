package com.jeffreyfhow.fakestagram.Retrofit;

import com.jeffreyfhow.fakestagram.Post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetPostsService {
    @GET("Prod/users/self/media/recent")
    Call<ArrayList<Post>> getAllPosts(@Header("Authorization") String authorization);
}
