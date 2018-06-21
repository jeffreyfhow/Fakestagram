package com.jeffreyfhow.fakestagram.Retrofit;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostLikeService {
    @POST("Prod/media/{id}/likes")
    Call<Void> postLike(@Header("Authorization") String authorization, @Path(value = "id", encoded = true) String id);
}
