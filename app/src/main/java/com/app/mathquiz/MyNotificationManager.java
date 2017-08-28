package com.app.mathquiz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by islam on 09/08/17.
 */

public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;

    private Context mCtx;

    public MyNotificationManager(Context context) {
        this.mCtx = context;
    }

    // show big notification with image
    // param message title, message text, image, intent
    public void showBigNotification(String title, String message, String url, Intent intent) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx,ID_BIG_NOTIFICATION,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(message);
        bigPictureStyle.bigPicture(getBitmapFromUrl(url));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = builder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(),R.mipmap.ic_launcher))
                .setContentText(message)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    // show small notification with image
    // param message title, message text, image, intent
    public void showSmallNotification(String title, String message,Intent intent) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx,ID_SMALL_NOTIFICATION,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = builder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(),R.mipmap.ic_launcher))
                .setContentText(message)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION,notification);
    }

    // return bitmap image from url
    private Bitmap getBitmapFromUrl(String url) {
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
