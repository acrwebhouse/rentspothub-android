package com.acr.rentspothub.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.acr.rentspothub.MainActivity;
import com.acr.rentspothub.R;
import com.acr.rentspothub.define.Constants;
import com.acr.rentspothub.tool.Model;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "default_channel_id";
    private static final String CHANNEL_NAME = "Default Channel";
    private static  int notifyId = 0;

    public void showNotificationExe(Intent intent, Context context, String title, String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification
        Notification notification = new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        // Show the notification
        notificationManager.notify(notifyId++, notification);
    }

    private void showNotification(String message){
        Model controlModel = new Model();
        JSONObject notificationJson = controlModel.getJsonObject(message);
        if(notificationJson != null){
            String title = Constants.EMPTY_STRING;
            String content = Constants.EMPTY_STRING;
            int type = 0;
            try {
                title = notificationJson.getString(Constants.NOTIFICATION_TITLE);
                content = notificationJson.getString(Constants.NOTIFICATION_CONTENT);
                type = notificationJson.getInt(Constants.NOTIFICATION_TYPE);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.NOTIFICATION_TYPE,type);
                switch (type){
                    case Constants.NOTIFICATION_TYPE_SYSTEM:
                        showNotificationExe(intent,this,title,content);
                        break;
                    case Constants.NOTIFICATION_TYPE_RESERVE_HOUSE:
                        String reserveHouseId = notificationJson.getString(Constants.NOTIFICATION_RESERVE_HOUSE_ID);
                        intent.putExtra(Constants.NOTIFICATION_RESERVE_HOUSE_ID,reserveHouseId);
                        showNotificationExe(intent,this,title,content);
                        break;
                    default:
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            showNotification(Constants.EMPTY_STRING + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]


    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }


}
