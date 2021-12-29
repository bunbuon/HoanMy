package com.hoanmy.kleanco.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import io.paperdb.Paper;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class HandleClickService extends Service {
    public static final String ACTION_PAUSE = PACKAGE_NAME + ".pause";
    public static final String ACTION_DONE = PACKAGE_NAME + ".done";
    public static final String ACTION_RESUME = PACKAGE_NAME + ".resume";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {

            switch (intent.getAction()) {
                case ACTION_DONE:
                    EventBus.getDefault().post(Action.DONE);
                    NotificationForegroundService.mTimer.cancel();
                    Log.d(Utils.TAG, "onStartCommand:  // go to previous song===== ");
                    break;
                case ACTION_PAUSE:
                    EventBus.getDefault().post(Action.PAUSE);
                    break;
                case ACTION_RESUME:
                    EventBus.getDefault().post(Action.RESUME);

                    break;
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
