package com.jeffreyfhow.fakestagram.Retrofit;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DeleteLikeService {
    @DELETE("Prod/media/{id}/likes")
    Call<Void> deleteLike(@Header("Authorization") String authorization, @Path(value = "id", encoded = true) String id);
}
