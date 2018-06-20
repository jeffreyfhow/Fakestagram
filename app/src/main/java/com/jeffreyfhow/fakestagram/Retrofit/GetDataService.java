package com.jeffreyfhow.fakestagram.Retrofit;

import com.jeffreyfhow.fakestagram.DataStructures.Posts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetDataService {
    @GET("Prod/users/self/media/recent")
    Call<Posts> getAllPosts(@Header("Authorization") String authorization);
}
