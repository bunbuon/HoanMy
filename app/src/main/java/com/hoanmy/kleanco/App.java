package com.hoanmy.kleanco;

import android.content.ComponentCallbacks2;

import androidx.multidex.MultiDexApplication;

import com.zing.zalo.zalosdk.oauth.ZaloSDKApplication;

import io.paperdb.Paper;

public class App extends MultiDexApplication implements ComponentCallbacks2 {
    private static String deviceId;

    private static App mInstance;

    public static App getmInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getApplicationContext());
        ZaloSDKApplication.wrap(this);


    }
}
