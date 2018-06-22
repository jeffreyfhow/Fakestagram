package com.jeffreyfhow.fakestagram.Network;

import android.util.Log;
import android.widget.Toast;

import com.jeffreyfhow.fakestagram.MainActivity;
import com.jeffreyfhow.fakestagram.Post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class used to send network requests.
 */
public class NetworkRequester {

    private MainActivity mainActivity;
    private NetworkService client;

    public NetworkRequester(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        client = ServiceGenerator.createService(NetworkService.class);
    }

    public void sendGetPostsRequest(String id_token) {
        Call<ArrayList<Post>> call = client.getAllPosts("Bearer " + id_token);
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                mainActivity.initializePosts(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Toast.makeText(mainActivity, "Error retrieving posts. Please try again.", Toast.LENGTH_SHORT).show();
                mainActivity.startAuthenticatorActivity();
                Log.v("MainActivity", t.getMessage());
            }
        });
    }

    public void sendLikeRequest(String id_token, String id) {
        Call<Void> call = client.postLike("Bearer " + id_token, id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(mainActivity, "Like Posted Response - Success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v("MainActivity", t.getMessage());
            }
        });
    }

    public void sendUnlikeRequest(String id_token, String id) {
        Call<Void> call = client.deleteLike("Bearer " + id_token, id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(mainActivity, "Like Deleted Response - Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v("MainActivity", t.getMessage());
            }
        });
    }
}
