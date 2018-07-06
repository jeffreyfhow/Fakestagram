
package com.jeffreyfhow.fakestagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.HashMap;

/**
 * Initial screen to sign in
 */
public class AuthenticatorActivity extends AppCompatActivity {

    public static final String ID_TOKEN_MESSAGE = "com.jeffreyfhow.fakestagram.ID_TOKEN_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        initializeWebView();
    }

    private void initializeWebView(){
        Log.d(this.getLocalClassName(), "Initializing WebView");
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
                startMainActivity(fragmentMap.get("id_token"));
            } else {
                return super.shouldOverrideUrlLoading(view, request);
            }
            return true;
            }
        });
    }

    private void startMainActivity(String id_token){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ID_TOKEN_MESSAGE, id_token);
        startActivity(intent);
    }

    /*
    * Parses URL fragment pieces into a HashMap of key/value pairs.
    * Returns null if fragment DNE
    */
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
}
