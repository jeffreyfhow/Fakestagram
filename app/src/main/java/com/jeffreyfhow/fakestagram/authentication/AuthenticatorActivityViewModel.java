package com.jeffreyfhow.fakestagram.authentication;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.data.WebViewData;
import com.jeffreyfhow.fakestagram.utility.ActivityStarter;

import java.util.HashMap;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthenticatorActivityViewModel {

    private Context applicationContext;
    private WebViewData webViewData;

    @DebugLog
    public AuthenticatorActivityViewModel(Context applicationContext, Activity activity){
        this.applicationContext = applicationContext;

        webViewData = new WebViewData(Constants.LOGIN_URL_FULL, new WebViewClient(){
            /*
             * If id_token found -> start MainActivity
             * else -> continue with webview
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                HashMap<String, String> fragmentMap = parseUrlFragment(
                    request.getUrl().getFragment()
                );

                if(fragmentMap != null && fragmentMap.containsKey("id_token")){
                    ActivityStarter.startMainActivity( activity, fragmentMap.get("id_token"));
                } else {
                    return super.shouldOverrideUrlLoading(view, request);
                }
                return true;
            }
        });
    }

    //region Observables/Hooks
    @DebugLog
    public Observable<WebViewData> onConnect(){
        return ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED))
            .map(x -> webViewData);
    }

    @DebugLog
    public Observable<String> onNotConnect(){
        return ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(c -> c.getState() != NetworkInfo.State.CONNECTED)
            .map(data -> Constants.CONNECT_TO_INTERNET);
    }
    //endregion

    //region Helper Functions
    /*
     * Parses URL fragment pieces into a HashMap of key/value pairs.
     * Returns null if fragment DNE
     */
    @DebugLog
    private HashMap<String, String> parseUrlFragment(String fragmentStr) {
        if(fragmentStr == null || fragmentStr.equals("")){
            return null;
        }

        HashMap<String, String> result = new HashMap<>();
        String[] keys = fragmentStr.split("&");
        for (String key : keys) {
            String[] values = key.split("=");
            result.put(values[0], (values.length > 1 ? values[1] : ""));
        }
        return result;
    }
    //endregion
}
