package com.jeffreyfhow.fakestagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AuthenticatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        Uri authzUrl = Uri.parse(
                "https://insta23prod.auth.us-west-2.amazoncognito.com/login?response_type=token&client_id=5khm2intordkd1jjr7rbborbfj&redirect_uri=https://www.facebook.com/");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, authzUrl);
        startActivity(launchBrowser);
    }

}
