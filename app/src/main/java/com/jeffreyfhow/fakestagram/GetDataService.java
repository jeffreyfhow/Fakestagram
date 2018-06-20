package com.jeffreyfhow.fakestagram;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetDataService {
    @GET("Prod/users/self/media/recent")
    Call<Posts> getAllPosts(@Header("Authorization") String authorization);
}
