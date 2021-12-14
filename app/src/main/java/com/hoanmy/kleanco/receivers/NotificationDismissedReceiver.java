package com.hoanmy.kleanco.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("com.my.app.notificationId");
    }
}
