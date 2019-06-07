package com.integrals.inlens.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;


public class NotificationHelper extends ContextWrapper {
    private NotificationManager notificationManager;


    public NotificationHelper(Context base) {
        super(base);
        createNotificationChannels();

    }

    private void createNotificationChannels() {
        NotificationChannel notificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("ID_503","InLens",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableLights(true);
            notificationChannel.setSound(null,null);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getNotificationManager().createNotificationChannel(notificationChannel);

        }

        NotificationChannel uploadNotificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            uploadNotificationChannel = new NotificationChannel("ID_504","Upload  Notification",NotificationManager.IMPORTANCE_MIN);
            getNotificationManager().createNotificationChannel(uploadNotificationChannel);

        }

        NotificationChannel inlensDisplayNotificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            inlensDisplayNotificationChannel = new
                    NotificationChannel("ID_505",
                    "InLens running on background",
                    NotificationManager.IMPORTANCE_NONE);
            getNotificationManager().createNotificationChannel(inlensDisplayNotificationChannel);

        }
        

    }

    public NotificationManager getNotificationManager() {
     if(notificationManager==null)
         notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }








    public void cancelUploadDataNotification(){
        notificationManager.cancel(672);
        }

}
