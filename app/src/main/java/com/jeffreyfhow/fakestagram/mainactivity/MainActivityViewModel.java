package com.jeffreyfhow.fakestagram.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.jeffreyfhow.fakestagram.data.Constants;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel {

    private Context applicationContext;
    private Activity activity;
    private String tokenId;

    public MainActivityViewModel(Context applicationContext, Activity activity, String tokenId){
        this.applicationContext = applicationContext;
        this.activity = activity;
        this.tokenId = tokenId;
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
}
