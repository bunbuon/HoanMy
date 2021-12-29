package com.hoanmy.kleanco.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hoanmy.kleanco.EmployeeAtivity;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.models.TaskProject;
import com.hoanmy.kleanco.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

import static com.hoanmy.kleanco.services.HandleClickService.ACTION_DONE;
import static com.hoanmy.kleanco.services.HandleClickService.ACTION_PAUSE;
import static com.hoanmy.kleanco.services.HandleClickService.ACTION_RESUME;

public class NotificationForegroundService extends Service {
    public static final String CHANNEL_ID = "NotificationForegroundService";
    private long timeTest = 1640370148;
    private RemoteViews notificationLayout;
    private RemoteViews notificationLayoutExpanded;
    private NotificationManager manager;
    private Notification customNotification;
    private String nameJob;
    private Handler mHandler = new Handler();
    public static Timer mTimer = null;
    public static final long NOTIFY_INTERVAL = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            new TimeDisplayTimerTask().cancel();
        }
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 1000, NOTIFY_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Action.STARTFOREGROUND_ACTION + "")) {
            Log.i(Utils.TAG, "Received Start Foreground Intent ");
            createNotificationChannel();
            pendingIntent();
        } else if (intent.getAction().equals(Action.STOPFOREGROUND_ACTION + "")) {
            Log.i(Utils.TAG, "Received Stop Foreground Intent");
            //your end servce code
            stopForeground(true);
            stopSelfResult(startId);
        }


        return START_NOT_STICKY;
    }


    private void pendingIntent() {
        Intent notificationIntent = new Intent(this, EmployeeAtivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        TaskProject dataJobNow = Paper.book().read("DataService");
        nameJob = dataJobNow.getName();
        timeTest = dataJobNow.getTime_end_timestamp();
        // Get the layouts to use in the custom notification
        notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);
        notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.custom_notification_expand);
        notificationLayout.setTextViewText(R.id.tvTitle, nameJob);
        notificationLayoutExpanded.setTextViewText(R.id.tvTitle, nameJob);


        // done
        Intent intentDone = new Intent(this, HandleClickService.class);
        intentDone.setAction(ACTION_DONE);
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btn_done, PendingIntent.getService(this, 0, intentDone, PendingIntent.FLAG_UPDATE_CURRENT));


        // pause
        Intent intentPause = new Intent(this, HandleClickService.class);
        intentPause.setAction(ACTION_PAUSE);
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btn_pause, PendingIntent.getService(this, 0, intentPause, PendingIntent.FLAG_UPDATE_CURRENT));


        // resume
        Intent intentResume = new Intent(this, HandleClickService.class);
        intentResume.setAction(ACTION_RESUME);
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.btn_continue, PendingIntent.getService(this, 0, intentResume, PendingIntent.FLAG_UPDATE_CURRENT));

        customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_hoanmy)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(pendingIntent)
                .setNotificationSilent()
                .build();

        startForeground(1, customNotification);
    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    Log.d(Utils.TAG, "runmHandler:----------- " + Paper.book().read("PAUSE"));
                    long countTime = 0;
                    long timeCurrent = System.currentTimeMillis() / 1000;
                    if (Paper.book().read("PAUSE") != null) {
                        notificationLayout.setTextViewText(R.id.txt_time,
                                getString(R.string.text_pause));
                        notificationLayoutExpanded.setTextViewText(R.id.txt_time,
                                getString(R.string.text_pause));
                    } else {
                        if (timeCurrent <= timeTest) {
                            countTime = (timeTest - timeCurrent);
                        } else {
                            countTime = timeCurrent - timeTest;
                            notificationLayout.setTextColor(R.id.txt_time, Color.parseColor("#FF0000"));
                            notificationLayoutExpanded.setTextColor(R.id.txt_time, Color.parseColor("#FF0000"));
                        }

                        notificationLayout.setTextViewText(R.id.txt_time,
                                String.format("%02d:%02d:%02d",
                                        TimeUnit.SECONDS.toHours(countTime) % 60,
                                        TimeUnit.SECONDS.toMinutes(countTime) % 60,
                                        TimeUnit.SECONDS.toSeconds(countTime) % 60));
                        notificationLayoutExpanded.setTextViewText(R.id.txt_time,
                                String.format("%02d:%02d:%02d",
                                        TimeUnit.SECONDS.toHours(countTime) % 60,
                                        TimeUnit.SECONDS.toMinutes(countTime) % 60,
                                        TimeUnit.SECONDS.toSeconds(countTime) % 60));
                    }
                    manager.notify(1, customNotification);

                }

            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
