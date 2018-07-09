package com.jeffreyfhow.fakestagram.network.requester;

import android.util.Log;
import android.widget.Toast;

import com.jeffreyfhow.fakestagram.BuildConfig;
import com.jeffreyfhow.fakestagram.MainActivity;
import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.network.LogOutServiceOkHttp;
import com.jeffreyfhow.fakestagram.network.LogOutService;
import com.jeffreyfhow.fakestagram.network.PostServiceOkHttp;
import com.jeffreyfhow.fakestagram.network.PostService;
import com.jeffreyfhow.fakestagram.network.helper.GsonDeserializerManager;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.network.helper.InterceptorFactory;
import com.jeffreyfhow.fakestagram.network.helper.OkHttpClientManager;
import com.jeffreyfhow.fakestagram.network.helper.RetrofitManager;
import com.jeffreyfhow.fakestagram.utility.ActivityStarter;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRequester {

    private MainActivity mainActivity;
    private PostService postService;
    private LogOutService logOutService;

    public NetworkRequester(MainActivity mainActivity){
        this.mainActivity = mainActivity;

        // 1. Create Interceptors
        Interceptor loggingInterceptor = InterceptorFactory.createHttpLoggingInterceptor(
            HttpLoggingInterceptor.Level.BODY
        );

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("response_type", Constants.RESPONSE_TYPE);
        queryMap.put("client_id", Constants.CLIENT_ID);
        Interceptor queryInterceptor = InterceptorFactory.createQueriesInterceptor(queryMap);

        // 2. Create Interceptor Lists
        ArrayList<Interceptor> loggingInterceptors = new ArrayList<>();
        ArrayList<Interceptor> queryInterceptors = new ArrayList<>();
        queryInterceptors.add(queryInterceptor);

        if (BuildConfig.DEBUG) {
            loggingInterceptors.add(loggingInterceptor);
            queryInterceptors.add(loggingInterceptor);
        }

        // 3. Create HTTP Clients
        OkHttpClient logOkHttpClient = OkHttpClientManager.getInstance().createOkHttpClient(
            "logger", loggingInterceptors
        );

        OkHttpClient queryOkHttpClient = OkHttpClientManager.getInstance().createOkHttpClient(
            "query", queryInterceptors
        );

        // 4. Create Shared RxJava2 Call Adapter
        RxJava2CallAdapterFactory callAdapterFactory = RxJava2CallAdapterFactory.create();

        // 5. Create Post Retrofit Client
        Retrofit postRetrofit = RetrofitManager.getInstance().createRetrofit(
            Constants.DATA_URL,
            logOkHttpClient,
            callAdapterFactory,
            GsonConverterFactory.create(GsonDeserializerManager.getPostSearchGson())
        );

        Retrofit logOutRetrofit = RetrofitManager.getInstance().createRetrofit(
            Constants.LOGIN_URL,
            queryOkHttpClient,
            callAdapterFactory,
            GsonConverterFactory.create()
        );

        // 4. Create Web Services
        postService = postRetrofit.create(PostService.class);
        logOutService = logOutRetrofit.create(LogOutService.class);
    }

    public void sendGetPostsRequestRx(String id_token){
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
                            NetworkRequester.this.getClass().getSimpleName(),
                            "GetPosts request complete"
                        );
                    }
                }
            );
    }

//    public void sendGetPostsRequest(String id_token) {
//        Call<ArrayList<Post>> call = postServiceOkHttp.getAllPosts("Bearer " + id_token);
//        call.enqueue(new Callback<ArrayList<Post>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
//                mainActivity.initializePosts(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
//                Toast.makeText(mainActivity, "Error retrieving posts. Please try again.", Toast.LENGTH_SHORT).show();
//                ActivityStarter.startAuthenticatorActivity(mainActivity);
//                Log.v("MainActivity", t.getMessage());
//            }
//        });
//    }
//
//    public void sendLikeRequest(String id_token, String id) {
//        Call<Void> call = postServiceOkHttp.postLike("Bearer " + id_token, id);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Toast.makeText(mainActivity, "Like Posted Response - Success.", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.v("MainActivity", t.getMessage());
//            }
//        });
//    }
//
//    public void sendUnlikeRequest(String id_token, String id) {
//        Call<Void> call = postServiceOkHttp.deleteLike("Bearer " + id_token, id);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Toast.makeText(mainActivity, "Like Deleted Response - Success", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.v("MainActivity", t.getMessage());
//            }
//        });
//    }
//
//    public void logOut(){
//        Call<Void> call = logOutServiceOkHttp.logOut(Constants.LOGOUT_URL);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                ActivityStarter.startAuthenticatorActivity(mainActivity);
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d(this.getClass().getSimpleName(), t.getMessage());
//                ActivityStarter.startAuthenticatorActivity(mainActivity);
//            }
//        });
//    }
}
