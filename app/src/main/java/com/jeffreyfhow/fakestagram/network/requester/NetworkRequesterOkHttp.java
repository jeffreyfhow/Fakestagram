package com.jeffreyfhow.fakestagram.network.requester;

import android.util.Log;

import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.mainactivity.MainActivityPresenter;
import com.jeffreyfhow.fakestagram.network.LogOutServiceOkHttp;
import com.jeffreyfhow.fakestagram.network.PostServiceOkHttp;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkRequesterOkHttp extends NetworkRequesterBase<PostServiceOkHttp, LogOutServiceOkHttp> {

    public NetworkRequesterOkHttp(MainActivityPresenter mainActivityPresenter){
        super(mainActivityPresenter, PostServiceOkHttp.class, LogOutServiceOkHttp.class);
    }

    @DebugLog
    @Override
    public void sendGetPostsRequest(String id_token) {
        Call<ArrayList<Post>> call = postService.getAllPosts("Bearer " + id_token);
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                mainActivityPresenter.initializePosts(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                mainActivityPresenter.showShortToast("Error retrieving posts. Please try again.");
                mainActivityPresenter.exit();
                Log.v(this.getClass().getSimpleName(), t.getMessage());
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
                mainActivityPresenter.showShortToast("Like Posted Response - Success");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v(this.getClass().getSimpleName(), t.getMessage());
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
                mainActivityPresenter.showShortToast("Like Deleted Response - Success");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v(this.getClass().getSimpleName(), t.getMessage());
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
                mainActivityPresenter.exit();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(this.getClass().getSimpleName(), t.getMessage());
                mainActivityPresenter.exit();
            }
        });
    }
}
