package com.jeffreyfhow.fakestagram.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.network.requester.NetworkRequester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter implements IMainActivityPresenter {

    private IMainActivityView mainActivityView;
    private Context applicationContext;

    private NetworkRequester networkRequester;
    private CompositeDisposable compositeDisposable;

    private ArrayList<Post> posts;
    private String tokenId;

    private int detailIdx = -1;
    private boolean isDetailView = false;

    public MainActivityPresenter(){
        networkRequester = new NetworkRequester(this);
        compositeDisposable = new CompositeDisposable();
    }

    //region IMainActivityPresenter Overrides
    @Override
    public void onCreate(Context applicationContext, Activity activity, String tokenId) {
        this.applicationContext = applicationContext;
        this.tokenId = tokenId;
        this.mainActivityView = (MainActivity) activity;
    }

    @Override
    public void onResume() {
        compositeDisposable.add(onConnect().subscribe(
            connectivity -> networkRequester.sendGetPostsRequest(tokenId)
        ));
        compositeDisposable.add(onNotConnect().subscribe(
            connectivity -> exit(),
            throwable -> Log.d(((Activity)mainActivityView).getLocalClassName(), throwable.getMessage())
        ));
    }

    @Override
    public void onPause() {
        compositeDisposable.clear();
        networkRequester.dispose();
    }

    @Override
    public void onBackPressed() {
        if (isDetailView) {
            setGridView();
        } else {
            logOut();
        }
    }

    @Override
    public void onLogoutButtonPressed() {
        logOut();
    }
    //endregion

    //region Network Connection Monitoring
    @DebugLog
    private Observable<Connectivity> onNotConnect(){
        return ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(c -> c.getState() != NetworkInfo.State.CONNECTED);
    }

    @DebugLog
    private Observable<Connectivity> onConnect(){
        return ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(c -> c.getState() == NetworkInfo.State.CONNECTED);
    }
    //endregion

    //region Initialization & Display
    @DebugLog
    public void initializePosts(ArrayList<Post> posts){

        this.posts = posts;

        Collections.sort(this.posts, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                return p1.getDateCreated().isAfter(p2.getDateCreated()) ? -1 : 1;
            }
        });

        mainActivityView.setUserIcon(this.posts.get(0).getProfilePictureUrl());

        setGridView();
    }

    private void setGridView(){
        isDetailView = false;
        mainActivityView.displayGridView(
            posts,
            position -> setDetailView(position),
            false
        );
    }

    private void setDetailView(int pos){
        isDetailView = true;
        detailIdx = pos;
        mainActivityView.displayDetailView(
            posts.get(pos),
            position -> {
                if (detailIdx >= 0) {
                    tryLikePhoto(detailIdx);
                }
            },
            true
        );
    }
    //endregion

    //region Liking
    private void tryLikePhoto(int pos) {
        Post p = posts.get(pos);
        boolean hasLiked = p.getUserHasLiked();
        long likeCnt = p.getLikeCnt();
        if (hasLiked) {
            p.setUserHasLiked(false);
            p.setLikeCnt(--likeCnt);
            networkRequester.sendUnlikeRequest(tokenId, p.getPostId());
        } else {
            p.setUserHasLiked(true);
            p.setLikeCnt(++likeCnt);
            networkRequester.sendLikeRequest(tokenId, p.getPostId());
        }
        mainActivityView.refreshState();
    }
    //endregion

    //region Exiting
    private void logOut(){
        networkRequester.logOut();
    }

    public void exit(){
        mainActivityView.exit();
    }
    //endregion

    //region Helper Functions
    public void showShortToast(String text){
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show();
    }
    //endregion


}
