package com.jeffreyfhow.fakestagram.network.requester;

import android.util.Log;
import android.widget.Toast;

import com.jeffreyfhow.fakestagram.MainActivity;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.network.LogOutService;
import com.jeffreyfhow.fakestagram.network.PostService;
import com.jeffreyfhow.fakestagram.utility.ActivityStarter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NetworkRequesterRxJava7 extends NetworkRequesterBase<PostService, LogOutService> {
    public NetworkRequesterRxJava7(MainActivity mainActivity){
        super(mainActivity, PostService.class, LogOutService.class);
    }

    @Override
    public void sendGetPostsRequest(String id_token) {
        Observable<ArrayList<Post>> postsObservable = postService.getAllPosts(id_token);

        Disposable subscription = postsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Consumer<ArrayList<Post>>() {
                    @Override
                    public void accept(ArrayList<Post> posts) throws Exception {
                        mainActivity.initializePosts(posts);
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(
                            mainActivity,
                            "Error retrieving posts. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show();
                        mainActivity.finish();
                        Log.v("MainActivity", throwable.getMessage());
                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(
                            this.getClass().getSimpleName(),
                            "GetPosts request complete"
                        );
                    }
                }
            );
    }

    @Override
    public void sendLikeRequest(String id_token, String id) {
        // TODO: 7/8/18  
    }

    @Override
    public void sendUnlikeRequest(String id_token, String id) {
        // TODO: 7/8/18  
    }

    @Override
    public void logOut() {
        // TODO: 7/8/18  
    }
}
