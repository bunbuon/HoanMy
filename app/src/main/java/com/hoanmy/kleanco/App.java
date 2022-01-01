package com.hoanmy.kleanco;

import android.content.ComponentCallbacks2;

import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

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

        Stetho.initializeWithDefaults(this);

    }
}
