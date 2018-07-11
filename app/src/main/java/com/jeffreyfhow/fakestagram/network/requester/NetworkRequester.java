package com.jeffreyfhow.fakestagram.network.requester;

import android.util.Log;
import android.widget.Toast;

import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.mainactivity.MainActivity;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.mainactivity.MainActivityPresenter;
import com.jeffreyfhow.fakestagram.network.LogOutService;
import com.jeffreyfhow.fakestagram.network.PostService;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NetworkRequester extends NetworkRequesterBase<PostService, LogOutService> {

    public NetworkRequester(MainActivity mainActivity, MainActivityPresenter mainActivityPresenter){
        super(mainActivity, mainActivityPresenter, PostService.class, LogOutService.class);
    }

    @DebugLog
    @Override
    public void sendGetPostsRequest(String id_token) {
        Observable<ArrayList<Post>> postsObservable = postService.getAllPosts(id_token);

        Disposable subscription = postsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                posts -> {
                    mainActivityPresenter.initializePosts(posts);
                },
                throwable -> {
                    Toast.makeText(
                        mainActivity,
                        "Error retrieving posts. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show();
                    Log.v(this.getClass().getSimpleName(), throwable.getMessage());
                    mainActivity.finish();
                }
            );
    }

    @DebugLog
    @Override
    public void sendLikeRequest(String id_token, String id) {
        Observable<Void> likeObservable = postService.postLike("Bearer " + id_token, id);

        Disposable subscription = likeObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> Toast.makeText(mainActivity, "Like Posted Response - Success.", Toast.LENGTH_SHORT).show(),
                throwable -> Log.v("MainActivity", throwable.getMessage())
            );
    }

    @DebugLog
    @Override
    public void sendUnlikeRequest(String id_token, String id) {
        Observable<Void> unlikeObservable = postService.deleteLike("Bearer " + id_token, id);
        Disposable subscription = unlikeObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> Toast.makeText(mainActivity, "Like Deleted Response - Success", Toast.LENGTH_SHORT).show(),
                throwable -> Log.v("MainActivity", throwable.getMessage())
            );
    }

    @DebugLog
    @Override
    public void logOut() {
        Observable<Void> logOutObservable = logOutService.logOut(Constants.LOGOUT_URL);
        Disposable subscription = logOutObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> mainActivity.finish(),
                throwable -> mainActivity.finish()
            );
    }
}