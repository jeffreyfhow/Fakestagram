package com.jeffreyfhow.fakestagram;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://kqlpe1bymk.execute-api.us-west-2.amazonaws.com/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
        //GitHubClient client =  retrofit.create(GitHubClient.class);
        //GetDataService client = retrofit.create(GetDataService.class);
    }

//    public
}
