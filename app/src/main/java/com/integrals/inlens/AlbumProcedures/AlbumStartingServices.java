package com.integrals.inlens.AlbumProcedures;

import android.content.Context;

import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.JobScheduler.JobHelper;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;

public class AlbumStartingServices {
private JobHelper jobHelper;
private Context context;
private com.integrals.inlens.ServiceImplementation.JobScheduler.AlertNotificationJobPackage.JobHelper jobHelperAlert;

    public AlbumStartingServices(Context context) {
        this.context = context;
        this.jobHelper=new JobHelper(context);
        this.jobHelperAlert=
                new com.integrals.inlens.ServiceImplementation.JobScheduler.AlertNotificationJobPackage.JobHelper(context);

    }

    public void intiateNotificationAtStart(){
        NotificationHelper notificationHelper=new NotificationHelper(context);
        RecentImage recentImage=new RecentImage(context);
        notificationHelper.notifyRecentImageAlert(recentImage.recentImagePath());
    }



    public void initiateJobServices(){

    jobHelper.initiateJobInfo();
    jobHelper.scheduleJob();
    jobHelperAlert.initiateJobInfo();
    jobHelperAlert.scheduleJob();

    }



}
