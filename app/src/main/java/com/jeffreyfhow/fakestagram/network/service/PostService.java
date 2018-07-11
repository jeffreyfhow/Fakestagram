package com.jeffreyfhow.fakestagram.network.service;

import com.jeffreyfhow.fakestagram.data.Post;

import java.util.ArrayList;

import io.reactivex.Observable;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostService {
    @GET("Prod/users/self/media/recent")
    Observable<ArrayList<Post>> getAllPosts(
        @Header("Authorization") String authorization
    );

    @POST("Prod/media/{id}/likes")
    Observable<Response<Void>> postLike(
        @Header("Authorization") String authorization,
        @Path(value = "id", encoded = true) String id
    );

    @DELETE("Prod/media/{id}/likes")
    Observable<Response<Void>> deleteLike(
        @Header("Authorization") String authorization,
        @Path(value = "id", encoded = true) String id
    );
}
