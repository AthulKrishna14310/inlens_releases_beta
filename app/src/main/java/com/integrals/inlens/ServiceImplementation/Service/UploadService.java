package com.integrals.inlens.ServiceImplementation.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.integrals.inlens.GridView.MainActivity;
import com.integrals.inlens.R;
import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.Includes.UploadServiceHelper;

import java.util.ArrayList;

import static android.os.Build.VERSION.SDK;

public class UploadService extends Service {



    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String input = intent.getStringExtra("inputExtra");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);


            Notification notification = new NotificationCompat.Builder(this, "ID_505")
                    .setContentTitle("InLens Service running background")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.inlens_notification)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);
        }

        ArrayList<String> list=new ArrayList<>();
        RecentImage recentImage=new RecentImage(getApplicationContext());
        list.add(recentImage.recentImagePath());
        UploadServiceHelper uploadServiceHelper=new UploadServiceHelper(
                getApplicationContext(),
                list,
                "",""
        );
        uploadServiceHelper.initiateUploadOperation();
        uploadServiceHelper.proceedUploadOperation();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
