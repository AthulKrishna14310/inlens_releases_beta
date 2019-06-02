package com.integrals.inlens.AlbumProcedures;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.JobScheduler.JobHelper;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;
import com.integrals.inlens.ServiceImplementation.Service.UploadService;

public class AlbumStartingServices {
private JobHelper jobHelper;
private Context context;
private com.integrals.inlens.ServiceImplementation.
        JobScheduler.AlertNotificationJobPackage.JobHelper jobHelperAlert;
private    NotificationHelper notificationHelper;

    public AlbumStartingServices(Context context) {
        this.context = context;
        this.jobHelper=new JobHelper(context);
        this.jobHelperAlert=
                new com.integrals.inlens.ServiceImplementation.JobScheduler.
                        AlertNotificationJobPackage.JobHelper(context);
        notificationHelper=new NotificationHelper(context);
    }

    public void intiateNotificationAtStart(){
        RecentImage recentImage=new RecentImage(context);
        notificationHelper.notifyRecentImageAlert(recentImage.recentImagePath());
    }



    public void initiateJobServices(){

    jobHelper.initiateJobInfo();
    jobHelper.scheduleJob();
    jobHelperAlert.initiateJobInfo();
    jobHelperAlert.scheduleJob();

    }

    public void initiateUploadService(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent serviceIntent = new Intent(context, UploadService.class);
                serviceIntent.putExtra("inputExtra", "Ongoing InLens Recent-Image service.");
                ContextCompat.startForegroundService(context, serviceIntent);
            }
            else
            {
                Intent serviceIntent=new Intent(context, UploadService.class);
                context.startService(serviceIntent);
            }
        }




    }


