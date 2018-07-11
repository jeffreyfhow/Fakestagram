package com.jeffreyfhow.fakestagram.network.requester;

import android.util.Log;

import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.mainactivity.MainActivityPresenter;
import com.jeffreyfhow.fakestagram.network.service.LogOutService;
import com.jeffreyfhow.fakestagram.network.service.PostService;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NetworkRequester extends NetworkRequesterBase<PostService, LogOutService> {

    private CompositeDisposable compositeDisposable;

    public NetworkRequester(MainActivityPresenter mainActivityPresenter){
        super(mainActivityPresenter, PostService.class, LogOutService.class);
        compositeDisposable = new CompositeDisposable();
    }

    @DebugLog
    @Override
    public void sendGetPostsRequest(String id_token) {
        Observable<ArrayList<Post>> postsObservable = postService.getAllPosts(id_token);
        compositeDisposable.add(postsObservable
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
            )
        );
    }

    @DebugLog
    @Override
    public void sendLikeRequest(String id_token, String id) {
        Observable<Response<Void>> likeObservable = postService.postLike("Bearer " + id_token, id);
        compositeDisposable.add(likeObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> mainActivityPresenter.showShortToast("Like Posted Response - Success"),
                throwable -> Log.v(this.getClass().getSimpleName(), throwable.getMessage())
            )
        );
    }

    @DebugLog
    @Override
    public void sendUnlikeRequest(String id_token, String id) {
        Observable<Response<Void>> unlikeObservable = postService.deleteLike("Bearer " + id_token, id);
        compositeDisposable.add(unlikeObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> mainActivityPresenter.showShortToast("Like Deleted Response - Success"),
                throwable -> Log.v(this.getClass().getSimpleName(), throwable.getMessage())
            )
        );
    }

    @DebugLog
    @Override
    public void logOut() {
        Observable<Response<Void>> logOutObservable = logOutService.logOut(Constants.LOGOUT_URL);
        compositeDisposable.add(logOutObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                aVoid -> mainActivityPresenter.exit(),
                throwable -> mainActivityPresenter.exit()
            )
        );
    }

    @DebugLog
    public void dispose() {
        compositeDisposable.dispose();
    }
}