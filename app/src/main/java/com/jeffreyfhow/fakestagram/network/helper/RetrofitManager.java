package com.jeffreyfhow.fakestagram.network.helper;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class RetrofitManager {

    private volatile static RetrofitManager instance;
    private static HashMap<String, Retrofit> retrofits;

    private RetrofitManager(){
        retrofits = new HashMap<>();
    }

    public static RetrofitManager getInstance(){
        if(instance == null){
            synchronized (RetrofitManager.class){
                if(instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public Retrofit createOrGetRetrofit(
        String baseUrl,
        OkHttpClient okHttpClient,
        CallAdapter.Factory callAdapterFactory,
        Converter.Factory converterFactory
    ){

        if(retrofits.containsKey(baseUrl)){
            return retrofits.get(baseUrl);
        }

        Retrofit r = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build();

        retrofits.put(baseUrl, r);
        return r;
    }

    public Retrofit getRetrofit(String baseUrl){
        if(retrofits.containsKey(baseUrl)){
            return retrofits.get(baseUrl);
        }
        return null;
    }

    public void removeRetrofit(String baseUrl){
        retrofits.remove(baseUrl);
    }

    public void clearRetrofits(){
        retrofits.clear();
    }
}
