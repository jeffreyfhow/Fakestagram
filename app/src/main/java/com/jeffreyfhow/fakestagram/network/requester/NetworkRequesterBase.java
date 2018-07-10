package com.jeffreyfhow.fakestagram.network.requester;

import com.jeffreyfhow.fakestagram.BuildConfig;
import com.jeffreyfhow.fakestagram.mainactivity.MainActivity;
import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.network.helper.GsonDeserializerManager;
import com.jeffreyfhow.fakestagram.network.helper.InterceptorFactory;
import com.jeffreyfhow.fakestagram.network.helper.OkHttpClientManager;
import com.jeffreyfhow.fakestagram.network.helper.RetrofitManager;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Abstract base class that sets up shared functionality of its subclasses.
 */
public abstract class NetworkRequesterBase<PS, LS> implements INetworkRequester{
    protected MainActivity mainActivity;
    private Class<PS> psClass;
    private Class<LS> lsClass;

    protected PS postService;
    protected LS logOutService;

    public NetworkRequesterBase(MainActivity mainActivity, Class<PS> psClass, Class<LS> lsClass){
        this.mainActivity = mainActivity;
        this.psClass = psClass;
        this.lsClass = lsClass;

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

        postService = postRetrofit.create(psClass);
        logOutService = logOutRetrofit.create(lsClass);
    }
}
