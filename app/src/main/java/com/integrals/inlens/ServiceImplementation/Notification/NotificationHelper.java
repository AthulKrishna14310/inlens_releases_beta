package com.integrals.inlens.ServiceImplementation.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import com.integrals.inlens.R;
import com.integrals.inlens.ServiceImplementation.InLensGallery.MainActivity;
import java.io.File;
import java.io.IOException;
import id.zelory.compressor.Compressor;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager notificationManager;
    private RemoteViews remoteViews;
    private Bitmap      logoBitmap;
    private Context     context;
    private Bitmap      recentImageBitmap;

    public NotificationHelper(Context base)
    {
        super(base);
        context=base;

        Resources res = getApplicationContext().getResources();
        int id = R.drawable.inlens_logo_m;
        logoBitmap = BitmapFactory.decodeResource(res, id);


        NotificationChannel notificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("ID_503","Recent Image Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableLights(true);
            notificationChannel.setSound(null,null);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getNotificationManager().createNotificationChannel(notificationChannel);

        }

        NotificationChannel inlensDisplayNotificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            inlensDisplayNotificationChannel = new
                    NotificationChannel("ID_503",
                    "InLens running on background",
                    NotificationManager.IMPORTANCE_NONE);
            getNotificationManager().createNotificationChannel(inlensDisplayNotificationChannel);

        }



    }

    public NotificationManager getNotificationManager()
    {
        if(notificationManager==null)
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }

    public void notifyRecentImage(String imageLocation)
    {
        try {
            generateNotificationBitmap(imageLocation);
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
            remoteViews.setImageViewBitmap(R.id.UploadImageViewNotification,recentImageBitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder builder = this.builderNotificationForRecentImageOreo();
                builder.setOnlyAlertOnce(true);
                builder.setOngoing(true);

                this.getNotificationManager().notify(7907, builder.build());
            }else{
                NotificationManager notificationManager =
                        (NotificationManager)
                                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(getApplicationContext(),
                        com.integrals.inlens.ServiceImplementation.InLensGallery.MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder =
                        (NotificationCompat.Builder)
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setContentTitle("Tap to view recent-images")
                                        .setContentText("View all recent-images that was " +
                                                "captured after the  Cloud-Album creation. ")
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setOnlyAlertOnce(true)
                                        .setCustomBigContentView(remoteViews)
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.inlens_logo_m)
                                        .setLargeIcon(logoBitmap)
                                        .setOngoing(true)
                                        .setPriority(Notification.PRIORITY_DEFAULT);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(7907, builder.build());


            }

        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d("InLens","No recent image found");
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
            Bitmap tempB=BitmapFactory.decodeResource(getResources(),R.drawable.ic_photo_camera);
            remoteViews.setImageViewBitmap(R.id.UploadImageViewNotification,tempB);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder builder = this.builderNotificationForRecentImageOreo();
                builder.setOnlyAlertOnce(true);
                builder.setOngoing(true);

                this.getNotificationManager().notify(7907, builder.build());
            }else{
                NotificationManager notificationManager =
                        (NotificationManager)
                                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(getApplicationContext(),
                        com.integrals.inlens.ServiceImplementation.InLensGallery.MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder =
                        (NotificationCompat.Builder)
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setContentTitle("Tap to view recent-images")
                                        .setContentText("View all recent-images that was " +
                                                "captured after the  Cloud-Album creation. ")
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setOnlyAlertOnce(true)
                                        .setOngoing(true)
                                        .setCustomBigContentView(remoteViews)
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.inlens_logo_m)
                                        .setLargeIcon(logoBitmap)
                                        .setPriority(Notification.PRIORITY_DEFAULT);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(7907, builder.build());


            }




        }

    }

    public void notifyRecentImageAlert(String imageLocation)
    {
        try {
            generateNotificationBitmap(imageLocation);
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
            remoteViews.setImageViewBitmap(R.id.UploadImageViewNotification,recentImageBitmap);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder builder = this.builderNotificationForRecentImageOreo();
                builder.setOnlyAlertOnce(false);
                builder.setOngoing(true);
                this.getNotificationManager().notify(7907, builder.build());
            }else{
                NotificationManager notificationManager =
                        (NotificationManager)
                                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(getApplicationContext(),
                        com.integrals.inlens.ServiceImplementation.InLensGallery.MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder =
                        (NotificationCompat.Builder)
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setContentTitle("Tap to view recent-images")
                                        .setContentText("View all recent-images that was " +
                                                "captured after the  Cloud-Album creation. ")
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setCustomBigContentView(remoteViews)
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.inlens_logo_m)
                                        .setLargeIcon(logoBitmap)
                                        .setOngoing(true)
                                        .setPriority(Notification.PRIORITY_MAX);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(7907, builder.build());


            }

        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d("InLens","No recent image found");
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
            Bitmap tempB=BitmapFactory.decodeResource(getResources(),R.drawable.ic_photo_camera);
            remoteViews.setImageViewBitmap(R.id.UploadImageViewNotification,tempB);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder builder = this.builderNotificationForRecentImageOreo();
                builder.setOnlyAlertOnce(true);
                builder.setOngoing(true);

                this.getNotificationManager().notify(7907, builder.build());
            }else{
                NotificationManager notificationManager =
                        (NotificationManager)
                                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(getApplicationContext(),
                        com.integrals.inlens.ServiceImplementation.InLensGallery.MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder =
                        (NotificationCompat.Builder)
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setContentTitle("Tap to view recent-images")
                                        .setContentText("View all recent-images that was " +
                                                "captured after the  Cloud-Album creation. ")
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setOnlyAlertOnce(true)
                                        .setCustomBigContentView(remoteViews)
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.inlens_logo_m)
                                        .setLargeIcon(logoBitmap)
                                        .setOngoing(true)
                                        .setPriority(Notification.PRIORITY_DEFAULT);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(7907, builder.build());


            }

        }

    }

    private void generateNotificationBitmap(String imageLocation)
    {
        File imageFile=new File(imageLocation);
        try {
            recentImageBitmap = new Compressor(getApplicationContext())
                    .setMaxHeight(130)
                    .setMaxWidth(130)
                    .setQuality(85)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToBitmap(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public Notification.Builder builderNotificationForRecentImageOreo()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = (Notification.Builder)
                    new Notification.Builder(getApplicationContext(), "ID_503")
                            .setContentTitle("Tap to view recent-images")

                            .setContentText("View all recent-images that was " +
                                    "captured after the  Cloud-Album creation. ")
                            .setOnlyAlertOnce(true)
                            .setCustomBigContentView(remoteViews)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.inlens_logo_m)
                            .setLargeIcon(logoBitmap);

            builder.setContentIntent(pendingIntent);
        }

        return builder;
    }
    public void cancelNotificationRecentImage()
    {try
    {
        notificationManager.cancel(7907);

    }catch (NullPointerException e){
        e.printStackTrace();
    }
    }
}