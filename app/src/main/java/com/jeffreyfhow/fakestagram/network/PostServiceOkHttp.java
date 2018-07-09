package com.jeffreyfhow.fakestagram.network;

import com.jeffreyfhow.fakestagram.data.Post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostServiceOkHttp {
    @GET("Prod/users/self/media/recent")
    Call<ArrayList<Post>> getAllPosts(@Header("Authorization") String authorization);

    @POST("Prod/media/{id}/likes")
    Call<Void> postLike(
        @Header("Authorization") String authorization,
        @Path(value = "id", encoded = true) String id
    );

    @DELETE("Prod/media/{id}/likes")
    Call<Void> deleteLike(
        @Header("Authorization") String authorization,
        @Path(value = "id", encoded = true) String id
    );
}
