package com.jeffreyfhow.fakestagram.network.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jeffreyfhow.fakestagram.data.Post;

import org.joda.time.LocalDateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GsonDeserializerManager {

    private static Gson postSearchGson;

    private static JsonDeserializer<ArrayList<Post>> getPostSearchDeserializer(){
        return new JsonDeserializer<ArrayList<Post>>() {
            @Override
            public ArrayList<Post> deserialize(
                    JsonElement json, Type typeOfT, JsonDeserializationContext context
            ) throws JsonParseException {
            ArrayList<Post> res = new ArrayList<>();
            JsonArray dataArr = json.getAsJsonObject().get("data").getAsJsonArray();
            for(int i = 0; i < dataArr.size(); i++){
                JsonObject currPost = dataArr.get(i).getAsJsonObject();
                Post newPost = convertJsonObjectToPost(currPost);
                res.add(newPost);
            }
            return res;
            }
        };
    }

    private static Post convertJsonObjectToPost(JsonObject postJson){
        JsonObject user = postJson.get("user").getAsJsonObject();
        JsonObject imageData = postJson.get("images").getAsJsonObject();
        return new Post(
            postJson.get("id").getAsString(),
            user.get("id").getAsLong(),
            user.get("username").getAsString(),
            user.get("profile_picture").getAsString(),
            postJson.get("likes").getAsJsonObject().get("count").getAsLong(),
            new LocalDateTime(1000*postJson.get("created_time").getAsLong()),
            imageData.get("standard_resolution").getAsJsonObject()
                .get("url").getAsString(),
            imageData.get("thumbnail").getAsJsonObject()
                .get("url").getAsString(),
            imageData.get("low_resolution").getAsJsonObject()
                .get("url").getAsString(),
            postJson.get("user_has_liked").getAsBoolean()
        );
    }

    public static Gson getPostSearchGson(){
        if(postSearchGson == null){
            postSearchGson = new GsonBuilder()
                .registerTypeAdapter(ArrayList.class, getPostSearchDeserializer())
                .create();
        }
        return postSearchGson;
    }
}
