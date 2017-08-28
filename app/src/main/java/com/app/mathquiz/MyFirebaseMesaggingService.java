package com.app.mathquiz;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.mathquiz.adapter.RecyclerViewAdapter;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by islam on 09/08/17.
 */

public class MyFirebaseMesaggingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    // send notification
    private void sendPushNotification(JSONObject json) {
        Log.e(TAG, "Notificaton Json: " + json);
        try {
            // getting json data
            JSONObject data = json.getJSONObject("data");

            // parsing json data
            String title = data.getString("title");
            String message = data.getString("message");

            Intent intent = new Intent("MyData");
            intent.putExtra("message",message);
            intent.putExtra("title",title);

            broadcaster.sendBroadcast(intent);

//            // create myNotificationManager object
//            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
//
//            // Creating an intent for the notification
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//
//            // if no image
//            if (imageUrl.equals(null)) {
//                // display small notif
//                mNotificationManager.showSmallNotification(title, message, intent);
//            } else {
//                // display big image
//                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
//            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception E) {
            Log.e(TAG, "Exception: " + E.getMessage());
        }
    }
}
