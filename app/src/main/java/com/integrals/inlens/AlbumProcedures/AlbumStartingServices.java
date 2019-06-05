package com.integrals.inlens.AlbumProcedures;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.JobScheduler.JobHelper;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;
import com.integrals.inlens.ServiceImplementation.Service.UploadService;

public class AlbumStartingServices {

    private JobHelper jobHelper;
    private Context context;
    private com.integrals.inlens.ServiceImplementation.
        JobScheduler.AlertNotificationJobPackage.JobHelper jobHelperAlert;
    private NotificationHelper notificationHelper;

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

        Checker checker=new Checker(context);
        if(checker.isConnectedToNet()){
            if(checker.isThereImageToUpload(new CurrentDatabase(context,"",null,1))){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent serviceIntent = new Intent(context, UploadService.class);
                    serviceIntent.putExtra("inputExtra", "Ongoing InLens upload service..");
                    ContextCompat.startForegroundService(context, serviceIntent);
                }
                else
                {
                    Intent serviceIntent=new Intent(context, UploadService.class);
                    context.startService(serviceIntent);
                }


            }
            else{
                Toast.makeText(context,"No image to upload",Toast.LENGTH_SHORT).show();
            }


        } else {

               Toast.makeText(context,"No internet connection",Toast.LENGTH_SHORT).show();
        }

    }





    }


