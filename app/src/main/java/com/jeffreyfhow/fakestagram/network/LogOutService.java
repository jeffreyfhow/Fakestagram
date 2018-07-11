package com.jeffreyfhow.fakestagram.network;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LogOutService {
    @GET("logout")
    Observable<Response<Void>> logOut(
        @Query("logout_uri") String logoutUri
    );
}
