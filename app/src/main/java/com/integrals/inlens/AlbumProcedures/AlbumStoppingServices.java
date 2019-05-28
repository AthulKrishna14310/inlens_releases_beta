package com.integrals.inlens.AlbumProcedures;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.integrals.inlens.ServiceImplementation.JobScheduler.JobHelper;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;
import com.integrals.inlens.ServiceImplementation.Service.UploadService;

import static android.content.Context.MODE_PRIVATE;

public class AlbumStoppingServices {

    private JobHelper jobHelper;
    private Context context;
    private com.integrals.inlens.ServiceImplementation.
            JobScheduler.AlertNotificationJobPackage.JobHelper jobHelperAlert;
    private NotificationHelper notificationHelper;

    public AlbumStoppingServices(Context context) {

        this.context = context;
        this.jobHelper=new JobHelper(context);
        this.jobHelperAlert=
                new com.integrals.inlens.ServiceImplementation.JobScheduler.
                        AlertNotificationJobPackage.JobHelper(context);
        notificationHelper=new NotificationHelper(context);


    }

    public void deinitiateAlbumServices(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.stopService(new Intent(context, UploadService.class));
        }else{
            context.stopService(new Intent(context, UploadService.class));
        }
        NotificationManager notificationManager =
                (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        jobHelper.stopJob();
        jobHelperAlert.stopJob();

    }

    public void deinitiateAllSharedPreferences(){

        SharedPreferences sharedPreferencesC = context.getSharedPreferences("InCommunity.pref",
                MODE_PRIVATE);
        SharedPreferences.Editor editorC = sharedPreferencesC.edit();
        editorC.putBoolean("UsingCommunity::", false);
        editorC.commit();

    }

}
