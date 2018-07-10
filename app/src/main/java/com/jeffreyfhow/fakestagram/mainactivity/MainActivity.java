package com.jeffreyfhow.fakestagram.mainactivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.jeffreyfhow.fakestagram.R;
import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.data.PostAdapter;
import com.jeffreyfhow.fakestagram.network.requester.NetworkRequesterOkHttp;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hugo.weaving.DebugLog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Main Scene where all the functionality happens
 */
public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private CompositeDisposable compositeDisposable;

    private String mTokenId;
    private ArrayList<Post> mPosts;
    private RecyclerView recyclerView;
    private boolean mIsDetailView = false;
    private String profileURL;
    private Menu menu;

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private PostAdapter adapter;
    int detailIdx = -1;

    private NetworkRequesterOkHttp networkRequester;

    // -----------------------------------------------------------
    //                      INITIALIZATION
    // -----------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar t = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(t);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mainActivityViewModel = new MainActivityViewModel(
            getApplicationContext(),
            this,
            getIntent().getStringExtra(Constants.ID_TOKEN_MESSAGE)
        );

        compositeDisposable = new CompositeDisposable();

        mTokenId = getIntent().getStringExtra(Constants.ID_TOKEN_MESSAGE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        compositeDisposable.add(mainActivityViewModel.onConnect().subscribe(
            connectivity -> startPosts()
        ));
        compositeDisposable.add(mainActivityViewModel.onNotConnect().subscribe(
            connectivity -> finish(),
            throwable -> Log.d(this.getLocalClassName(), throwable.getMessage())
        ));
    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    public void startPosts(){
        networkRequester = new NetworkRequesterOkHttp(this);
        networkRequester.sendGetPostsRequest(mTokenId);

        Log.v("MainActivity", "Token: " + mTokenId);

        recyclerView = findViewById(R.id.post_recycler_view);

        adapter = new PostAdapter(this);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this, 2);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @DebugLog
    public void initializePosts(ArrayList<Post> posts){
        mPosts = posts;

        profileURL = mPosts.get(0).getProfilePictureUrl();
        setUserIcon();
        Collections.sort(mPosts, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                return p1.getDateCreated().isAfter(p2.getDateCreated()) ? -1 : 1;
            }
        });

        displayGridView();
    }

    // -----------------------------------------------------------
    //                    FUNCTIONALITY
    // -----------------------------------------------------------
    @DebugLog
    private void displayGridView() {
        mIsDetailView = false;
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setHasRecencyText(false);
        adapter.update(mPosts);
        adapter.setListener(new PostAdapter.Listener() {
            @Override
            public void onClick(int position) {
                displayDetailView(position);
            }
        });
    }

    @DebugLog
    private void displayDetailView(final int pos) {
        mIsDetailView = true;

        adapter.setHasRecencyText(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        detailIdx = pos;
        adapter.update(mPosts.get(pos));
        adapter.setListener(new PostAdapter.Listener() {
            @Override
            public void onClick(int position) {
                if(detailIdx >= 0){
                    tryLikePhoto(detailIdx);
                }
            }
        });
    }

    @DebugLog
    private void tryLikePhoto(int pos) {
        Post p = mPosts.get(pos);
        boolean hasLiked = p.getUserHasLiked();
        long likeCnt = p.getLikeCnt();
        if (hasLiked) {
            p.setUserHasLiked(false);
            p.setLikeCnt(--likeCnt);
            networkRequester.sendUnlikeRequest(mTokenId, p.getPostId());
        } else {
            p.setUserHasLiked(true);
            p.setLikeCnt(++likeCnt);
            networkRequester.sendLikeRequest(mTokenId, p.getPostId());
        }
        adapter.notifyDataSetChanged();
        recyclerView.refreshDrawableState();
    }

    // -----------------------------------------------------------
    //                      TOOLBAR MENU
    // -----------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                networkRequester.logOut();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsDetailView) {
            displayGridView();
        } else {
            networkRequester.logOut();
        }
    }

    private void setUserIcon() {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                MenuItem m = menu.getItem(0);
                m.setIcon(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.v("MainActivity", e.getMessage());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.get().load(profileURL).into(target);

    }
}
