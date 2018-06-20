package com.jeffreyfhow.fakestagram;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthenticatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        WebView webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl("https://insta23prod.auth.us-west-2.amazoncognito.com/login?response_type=token&client_id=5khm2intordkd1jjr7rbborbfj&redirect_uri=https://www.23andme.com/");
        final Activity myAct = this;
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Toast.makeText(myAct, "URL LOADING", Toast.LENGTH_SHORT).show();

                HashMap<String, String> data = parseUrlFragment (request.getUrl().getFragment ());

                doRetrofit(data.get("id_token"));
                return true;
            }

        });
//        Uri authzUrl = Uri.parse(
//                "https://insta23prod.auth.us-west-2.amazoncognito.com/login?response_type=token&client_id=5khm2intordkd1jjr7rbborbfj&redirect_uri=https://www.23andme.com/");
//        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, authzUrl);
//        startActivity(launchBrowser);

    }
//    public void parseMyURL(String myStr){
//        String[] strs = myStr.split("\\&");
//        for(int i = 0; i < strs.length; i++){
//            Log.v("MainAc", strs[i]);
//        }
//    }
//
    public void connectToDestination(String token){
//        URL url = new URL("https://kqlpe1bymk.execute-api.us-west-2.amazonaws.com/Prod/users/self/media/recent");
//        URLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.addRequestProperty("client_id", "5khm2intordkd1jjr7rbborbfj");
//        conn.setRequestProperty("Authorization", "Bearer " + token);

//        Uri authzUrl = Uri.parse(
//                "https://insta23prod.auth.us-west-2.amazoncognito.com/login?response_type=token&client_id=5khm2intordkd1jjr7rbborbfj&redirect_uri=https://www.23andme.com/");
//        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, authzUrl);
//        startActivity(launchBrowser);
    }

    public void doRetrofit(String id_token){
        Log.v("AuthenticatorActivity", "Doing Retrofit on ID: " + id_token);
//        GetDataService client = ServiceGenerator.createService(GetDataService.class);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService client = retrofit.create(GetDataService.class);
        Call<Posts> call = client.getAllPosts("Bearer " + id_token);

// Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                Log.v("AuthenticatorActivity", "RESPONDING!!!!");
                // The network call was a success and we got a response
//                ArrayList<Post> p = new ArrayList(response.body());
//                for(int i = 0; i < p.size(); i++){
//                    Log.v(this.getClass().getName(), p.get(i).toString());
//                }
                String a = this.getClass().getName();
                Posts p = response.body();
                String b = p.toString();

                Log.v("AuthenticatorActivity", b);
                // TODO: use the repository list and display it
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                Log.v("AuthenticatorActivity", t.getMessage());
            }
        });
    }

    public HashMap<String, String> parseUrlFragment (String url) {

        HashMap<String, String> output = new HashMap<> ();

        String[] keys = url.split ("&");

        for (String key : keys) {

            String[] values = key.split ("=");
            output.put (values[0], (values.length > 1 ? values[1] : ""));

        }

        return output;

    }
}
