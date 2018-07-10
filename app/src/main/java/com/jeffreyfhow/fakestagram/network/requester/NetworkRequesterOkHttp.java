package com.jeffreyfhow.fakestagram.network.requester;

import android.util.Log;
import android.widget.Toast;

import com.jeffreyfhow.fakestagram.mainactivity.MainActivity;
import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.network.LogOutServiceOkHttp;
import com.jeffreyfhow.fakestagram.network.PostServiceOkHttp;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkRequesterOkHttp extends NetworkRequesterBase<PostServiceOkHttp, LogOutServiceOkHttp> {

    public NetworkRequesterOkHttp(MainActivity mainActivity){
        super(mainActivity, PostServiceOkHttp.class, LogOutServiceOkHttp.class);
    }

    @DebugLog
    @Override
    public void sendGetPostsRequest(String id_token) {
        Call<ArrayList<Post>> call = postService.getAllPosts("Bearer " + id_token);
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                mainActivity.initializePosts(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Toast.makeText(mainActivity, "Error retrieving posts. Please try again.", Toast.LENGTH_SHORT).show();
                mainActivity.finish();
                Log.v("MainActivity", t.getMessage());
            }
        });
    }

    @DebugLog
    @Override
    public void sendLikeRequest(String id_token, String id) {
        Call<Void> call = postService.postLike("Bearer " + id_token, id);
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

    @DebugLog
    @Override
    public void sendUnlikeRequest(String id_token, String id) {
        Call<Void> call = postService.deleteLike("Bearer " + id_token, id);
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

    @DebugLog
    @Override
    public void logOut() {
        Call<Void> call = logOutService.logOut(Constants.LOGOUT_URL);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mainActivity.finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(this.getClass().getSimpleName(), t.getMessage());
                mainActivity.finish();
            }
        });
    }
}
