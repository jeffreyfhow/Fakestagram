package com.jeffreyfhow.fakestagram.utility;

import android.app.Activity;
import android.content.Intent;

import com.jeffreyfhow.fakestagram.AuthenticatorActivity;
import com.jeffreyfhow.fakestagram.MainActivity;
import com.jeffreyfhow.fakestagram.data.Constants;

import hugo.weaving.DebugLog;

public class ActivityStarter {

    @DebugLog
    public static void startMainActivity(Activity currActivity, String id_token){
        Intent intent = new Intent(currActivity, MainActivity.class);
        intent.putExtra(Constants.ID_TOKEN_MESSAGE, id_token);
        currActivity.startActivity(intent);
    }

    @DebugLog
    public static void startAuthenticatorActivity(Activity currActivity) {
        Intent intent = new Intent(currActivity, AuthenticatorActivity.class);
        currActivity.startActivity(intent);
    }

}
