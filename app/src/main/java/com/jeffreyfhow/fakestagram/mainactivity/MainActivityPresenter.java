package com.jeffreyfhow.fakestagram.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;
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
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter implements IPresenter{

    private IMainActivityView mainActivityView;

    private Context applicationContext;
    private Activity activity;
    private NetworkRequester networkRequester;
    private String tokenId;
    private ArrayList<Post> posts;
    private String profilePictureUrl;
    private int detailIdx = -1;

    public MainActivityPresenter(Context applicationContext, Activity activity, String tokenId){
        this.applicationContext = applicationContext;
        this.activity = activity;
        this.tokenId = tokenId;
        this.mainActivityView = (MainActivity) activity;
        networkRequester = new NetworkRequester(this);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @DebugLog
    public Observable<Connectivity> onNotConnect(){
        return ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(c -> c.getState() != NetworkInfo.State.CONNECTED);
    }

    @DebugLog
    public Observable<Connectivity> onConnect(){
        return ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(c -> c.getState() == NetworkInfo.State.CONNECTED);
    }

    @DebugLog
    public void requestPosts(){
        networkRequester.sendGetPostsRequest(tokenId);
    }

    @DebugLog
    public void initializePosts(ArrayList<Post> posts){

        this.posts = posts;

        Collections.sort(this.posts, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                return p1.getDateCreated().isAfter(p2.getDateCreated()) ? -1 : 1;
            }
        });

        String proUrl = this.posts.get(0).getProfilePictureUrl();
        mainActivityView.setUserIcon(proUrl);

        setGridView();
    }

    public void setGridView(){
        mainActivityView.displayGridView(
            posts,
            position -> setDetailView(position),
            false
        );
    }

    public void setDetailView(int pos){
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
    
    public void logOut(){
        networkRequester.logOut();
    }

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

    public void showShortToast(String text){
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show();
    }

    public void exit(){
        mainActivityView.exit();
    }
}
