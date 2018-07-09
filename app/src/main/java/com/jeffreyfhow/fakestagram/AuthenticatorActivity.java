
package com.jeffreyfhow.fakestagram;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.jeffreyfhow.fakestagram.utility.ActivityStarter;

import java.util.HashMap;

import hugo.weaving.DebugLog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Initial screen to sign in
 */
public class AuthenticatorActivity extends AppCompatActivity {

    Disposable networkDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpConnectionHandling();
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkDisposable.dispose();
    }

    @DebugLog
    private void initializeWebView(){
        WebView webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl("https://insta23prod.auth.us-west-2.amazoncognito.com/login?response_type=token&client_id=5khm2intordkd1jjr7rbborbfj&redirect_uri=https://www.23andme.com/");
        webview.setWebViewClient(new WebViewClient(){
            /*
            * If id_token found -> start MainActivity
            * else -> continue with webview
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            HashMap<String, String> fragmentMap = parseUrlFragment(request.getUrl().getFragment());
            if(fragmentMap != null && fragmentMap.containsKey("id_token")){
                ActivityStarter.startMainActivity(
                    AuthenticatorActivity.this,
                    fragmentMap.get("id_token")
                );
            } else {
                return super.shouldOverrideUrlLoading(view, request);
            }
            return true;
            }
        });
    }

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

    @DebugLog
    public void setUpConnectionHandling(){
        networkDisposable = ReactiveNetwork.observeNetworkConnectivity(getApplicationContext())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Connectivity>() {
                @Override public void accept(final Connectivity connectivity) {
                    NetworkInfo.State networkState = connectivity.getState();
                    if (networkState != NetworkInfo.State.CONNECTED) {
                        Toast.makeText(
                            AuthenticatorActivity.this,
                            "Please connect to the Internet.",
                            Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        initializeWebView();
                    }
                }
            });
    }

}
