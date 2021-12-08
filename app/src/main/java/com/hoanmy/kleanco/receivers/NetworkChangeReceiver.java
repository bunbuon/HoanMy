package com.hoanmy.kleanco.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;


public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String ACTION_NETWORK_STATE_CHANGED = "Action.Network.Changed";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            context.sendBroadcast(new Intent(ACTION_NETWORK_STATE_CHANGED));
        }
    }
}
