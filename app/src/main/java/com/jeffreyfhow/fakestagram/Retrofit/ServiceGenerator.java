package com.jeffreyfhow.fakestagram.Retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jeffreyfhow.fakestagram.DataStructures.FlatPost;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "https://kqlpe1bymk.execute-api.us-west-2.amazonaws.com/";

    private static JsonDeserializer<ArrayList<FlatPost>> deserializer = getJsonDeserializer();

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(ArrayList.class, deserializer)
            .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }

    private static JsonDeserializer<ArrayList<FlatPost>> getJsonDeserializer(){
        return new JsonDeserializer<ArrayList<FlatPost>>() {
            @Override
            public ArrayList<FlatPost> deserialize(
                    JsonElement json, Type typeOfT, JsonDeserializationContext context
            ) throws JsonParseException {
                ArrayList<FlatPost> res = new ArrayList<>();
                JsonArray dataArr = json.getAsJsonObject().get("data").getAsJsonArray();
                for(int i = 0; i < dataArr.size(); i++){
                    JsonObject currPost = dataArr.get(i).getAsJsonObject();
                    JsonObject user = currPost.get("user").getAsJsonObject();
                    JsonObject imageData = currPost.get("images").getAsJsonObject();

                    res.add(new FlatPost(
                            currPost.get("id").getAsString(),
                            user.get("id").getAsLong(),
                            user.get("username").getAsString(),
                            user.get("profile_picture").getAsString(),
                            currPost.get("likes").getAsJsonObject().get("count").getAsLong(),
                            currPost.get("created_time").getAsLong(),
                            imageData.get("standard_resolution").getAsJsonObject()
                                    .get("url").getAsString(),
                            imageData.get("thumbnail").getAsJsonObject()
                                    .get("url").getAsString(),
                            imageData.get("low_resolution").getAsJsonObject()
                                    .get("url").getAsString()
                    ));
                }
                return res;

            }
        };
    }
}