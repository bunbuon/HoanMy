package com.hoanmy.kleanco;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.github.piasy.safelyandroid.component.support.SafelyAppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.hoanmy.kleanco.receivers.NetworkChangeReceiver;
import com.hoanmy.kleanco.utils.NetworkStatusUtil;

public abstract class BaseActivity extends SafelyAppCompatActivity {

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NetworkChangeReceiver.ACTION_NETWORK_STATE_CHANGED)) {
                if (NetworkStatusUtil.isNetworkAvaiable(BaseActivity.this)) onNetworkRestore();
                else onNetworkLost();
            }
        }
    };

    private boolean destroyed = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Snackbar mSnackbar;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getSimpleName();

//        if (checkLogin() && !Settings.isChecked()) logout();

        if (!canRotate()) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setStatusBarColor(ContextCompat.getColor(this, R.color.white));

    }

    @Override
    protected void onStart() {
        super.onStart();
        destroyed = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    protected boolean canRotate() {
        return true;
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) getWindow().setStatusBarColor(color);
    }

    protected void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(br, new IntentFilter(NetworkChangeReceiver.ACTION_NETWORK_STATE_CHANGED));

        if (!NetworkStatusUtil.isNetworkAvaiable(this)) {
            showBottomSheetErrorNetwork();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(br);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    private void initBottomSheet() {
        mSnackbar = Snackbar.make(getWindow().getDecorView().getRootView(), R.string.alert_error_network, Snackbar.LENGTH_INDEFINITE);
    }

    protected void showBottomSheetErrorNetwork() {
        if (mSnackbar == null) initBottomSheet();
        mSnackbar.show();
    }

    protected void onNetworkRestore() {
        if (mSnackbar != null) mSnackbar.dismiss();
    }

    protected void onNetworkLost() {
        showBottomSheetErrorNetwork();
    }

    @Override
    public boolean isDestroyed() {
        return Build.VERSION.SDK_INT >= 17 ? super.isDestroyed() : destroyed;
    }

    @Override
    protected void onDestroy() {
        destroyed = true;
        super.onDestroy();
    }


    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

}
