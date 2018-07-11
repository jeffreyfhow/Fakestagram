package com.jeffreyfhow.fakestagram.network.helper;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Encapsulates the creation of different interceptors (used in OkHttpClient creation)
 */
public class InterceptorFactory {
    public static Interceptor createHttpLoggingInterceptor(HttpLoggingInterceptor.Level level){
        return new HttpLoggingInterceptor().setLevel(level);
    }

    public static Interceptor createQueryInterceptor(String entry, String key){
        HashMap<String, String> queries = new HashMap<>();
        queries.put(entry, key);
        return createQueriesInterceptor(queries);
    }

    public static Interceptor createQueriesInterceptor(final HashMap<String, String> queryMap){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d(this.getClass().getSimpleName(), "Intercepting");
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();

                for(HashMap.Entry<String, String> entry : queryMap.entrySet()) {
                    Log.d(this.getClass().getSimpleName(), "Adding query " + entry.toString());
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }

                HttpUrl url = urlBuilder.build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }
}
