package com.jeffreyfhow.fakestagram.network.requester;

import android.util.Log;

import com.jeffreyfhow.fakestagram.data.Constants;
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
import retrofit2.Response;

public class NetworkRequester extends NetworkRequesterBase<PostService, LogOutService> {

    public NetworkRequester(MainActivityPresenter mainActivityPresenter){
        super(mainActivityPresenter, PostService.class, LogOutService.class);
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
                    mainActivityPresenter.showShortToast("Error retrieving posts. Please try again.");
                    Log.v(this.getClass().getSimpleName(), throwable.getMessage());
                    mainActivityPresenter.exit();
                }
            );
    }

    @DebugLog
    @Override
    public void sendLikeRequest(String id_token, String id) {
        Observable<Response<Void>> likeObservable = postService.postLike("Bearer " + id_token, id);

        Disposable disposable = likeObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> mainActivityPresenter.showShortToast("Like Posted Response - Success"),
                throwable -> Log.v(this.getClass().getSimpleName(), throwable.getMessage())
            );
    }

    @DebugLog
    @Override
    public void sendUnlikeRequest(String id_token, String id) {
        Observable<Response<Void>> unlikeObservable = postService.deleteLike("Bearer " + id_token, id);
        Disposable subscription = unlikeObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> mainActivityPresenter.showShortToast("Like Deleted Response - Success"),
                throwable -> Log.v(this.getClass().getSimpleName(), throwable.getMessage())
            );
    }

    @DebugLog
    @Override
    public void logOut() {
        Observable<Response<Void>> logOutObservable = logOutService.logOut(Constants.LOGOUT_URL);
        Disposable subscription = logOutObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> mainActivityPresenter.exit(),
                throwable -> mainActivityPresenter.exit()
            );
    }
}