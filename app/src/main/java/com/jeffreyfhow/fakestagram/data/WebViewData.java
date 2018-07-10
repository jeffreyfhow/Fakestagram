package com.jeffreyfhow.fakestagram.data;

import android.webkit.WebViewClient;

public class WebViewData {
    private String url;
    private WebViewClient webViewClient;

    public WebViewData(String url, WebViewClient webViewClient){
        this.url = url;
        this.webViewClient = webViewClient;
    }

    public String getUrl() {
        return url;
    }

    public WebViewClient getWebViewClient() {
        return webViewClient;
    }
}
