package com.hoanmy.kleanco.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hoanmy.kleanco.CustomerActivity;
import com.hoanmy.kleanco.EmployeeAtivity;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.models.Employee;
import com.hoanmy.kleanco.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "onMessageReceived: " + remoteMessage);

        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            Map<String, String> data = remoteMessage.getData();
            try {
                handleDataMessage(data);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        } else if (remoteMessage.getNotification() != null) {
            // Check if message contains a notification payload.
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent("pushNotification");
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(Map<String, String> data) {
        Log.e(TAG, "push json: " + data.toString());

        try {
//            JSONObject data = json.getJSONObject("data");
//
            String title = data.get("title");
            String message = data.get("message");
            long time = Long.valueOf(data.get("timestamp"))*1000;
            Date df = new java.util.Date(time);
            String vv = new SimpleDateFormat("hh:mma").format(df);
//            handleNotification(message);
            Intent resultIntent = new Intent("pushNotification");
            showNotificationMessage(getApplicationContext(), title, resultIntent, message, vv);
//            String imageUrl = data.getString("image");
//            String url = data.getString("url");
//            String type = data.getString("type");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "url: " + url);
//            Log.e(TAG, "type: " + type);


//            if (type.equals("web")) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                showNotificationMessage(getApplicationContext(), title, browserIntent, imageUrl);
//            } else if (type.equals("cpi")) {
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse(url)));
//                } catch (android.content.ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse(url)));
//                }
//            } else {
//                // app is in background, show the notification in notification tray
////                Intent resultIntent = new Intent(getApplicationContext(), VideoPlayerLandActivity.class);
////                resultIntent.putExtra("_video", url);
////                resultIntent.putExtra("_titlevideo", title);
////                showNotificationMessage(getApplicationContext(), title, resultIntent, imageUrl);
//            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, Intent intent, String message, String time) throws IOException {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotification(getApplicationContext(), title, intent, message, time);
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, CustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle(getString(R.string.notification))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    /**
     * Showing notification with text and image
     */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }
}
