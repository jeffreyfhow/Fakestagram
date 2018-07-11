package com.jeffreyfhow.fakestagram.network.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LogOutServiceOkHttp {

    @GET("logout")
    Call<Void> logOut(
        @Query("logout_uri") String logoutUri
    );

}
