
package com.jeffreyfhow.fakestagram.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.jeffreyfhow.fakestagram.R;
import com.jeffreyfhow.fakestagram.data.WebViewData;

import hugo.weaving.DebugLog;
import io.reactivex.disposables.Disposable;

/**
 * Initial screen to sign in
 */
public class AuthenticatorActivity extends AppCompatActivity {

    private WebView webView;

    private AuthenticatorActivityViewModel authenticatorActivityViewModel;
    private Disposable webViewDisposable;
    private Disposable connectivityDisposable;

    //region Activity Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        webView = findViewById(R.id.webview);

        authenticatorActivityViewModel = new AuthenticatorActivityViewModel(
            getApplicationContext(),
            this
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        webViewDisposable = authenticatorActivityViewModel.onConnect().subscribe(
            data -> setWebView(data)
        );
        connectivityDisposable = authenticatorActivityViewModel.onNotConnect().subscribe(
            data -> showToast(data)
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        webViewDisposable.dispose();
        connectivityDisposable.dispose();
    }
    //endregion

    //region Display Functionality
    @DebugLog
    public void setWebView(WebViewData webViewData){
        webView.loadUrl(webViewData.getUrl());
        webView.setWebViewClient(webViewData.getWebViewClient());
    }

    @DebugLog
    public void showToast(String text, int toastLength){
        Toast.makeText(AuthenticatorActivity.this, text, toastLength).show();
    }

    @DebugLog
    public void showToast(String text){
        showToast(text, Toast.LENGTH_SHORT);
    }
    //endregion

}
