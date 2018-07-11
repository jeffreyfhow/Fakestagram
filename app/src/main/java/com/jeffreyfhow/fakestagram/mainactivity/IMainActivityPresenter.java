package com.jeffreyfhow.fakestagram.mainactivity;

import android.app.Activity;
import android.content.Context;

public interface IMainActivityPresenter {

    void onCreate(Context applicationContext, Activity activity, String tokenId);
    void onPause();
    void onResume();
    void onBackPressed();
    void onLogoutButtonPressed();

}
