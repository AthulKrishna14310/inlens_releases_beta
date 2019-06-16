package com.integrals.inlens.AlbumProcedures;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.Helper.UploadDatabaseHelper;
import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.JobScheduler.JobHelper;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;
import com.integrals.inlens.ServiceImplementation.Service.UploadService;

import java.util.Calendar;

public class AlbumStartingServices {

    private JobHelper jobHelper;
    private Context context;
    private com.integrals.inlens.ServiceImplementation.
        JobScheduler.AlertNotificationJobPackage.JobHelper jobHelperAlert;
    private NotificationHelper notificationHelper;
    private CurrentDatabase currentDatabase;

    public AlbumStartingServices(Context context)
    {
        this.context = context;
        this.jobHelper=new JobHelper(context);
        this.jobHelperAlert=
                new com.integrals.inlens.ServiceImplementation.JobScheduler.
                        AlertNotificationJobPackage.JobHelper(context);
        notificationHelper=new NotificationHelper(context);
        currentDatabase=new CurrentDatabase(context,"",null,1);
    }

    public void intiateNotificationAtStart()
    {
        RecentImage recentImage=new RecentImage(context);
        notificationHelper.notifyRecentImageAlert(recentImage.recentImagePath());
    }

    public void initiateJobServices()
    {

    jobHelper.initiateJobInfo();
    jobHelper.scheduleJob();
    jobHelperAlert.initiateJobInfo();
    jobHelperAlert.scheduleJob();

    }

    public void deleteDatabases(){
        CurrentDatabase currentDatabase1 = new CurrentDatabase(context, "", null, 1);
        currentDatabase1.DeleteDatabase();
        UploadDatabaseHelper uploadDatabaseHelper1 =
                new UploadDatabaseHelper(context, "", null, 1);
        uploadDatabaseHelper1.DeleteDatabase();

    }
    public void createDatabases(String PostKey,String AlbumTime){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
       String albumCreated=null;
       if(month<9) {
            albumCreated = day + "-0" + month + "-" + year;
       }else{
           albumCreated = day + "-" + month + "-" + year;

       }
       CurrentDatabase currentDatabase= new CurrentDatabase(context,"",null,1);
        currentDatabase.InsertUploadValues(PostKey,1,1,0,AlbumTime,1,1,"",albumCreated);
        currentDatabase.close();

    }

    public void initiateUploadService()
    {

        Checker checker=new Checker(context);
        if(checker.isConnectedToNet()){



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


            }else{
            Log.d("InLensGallery","No Internet");

            }

        }



 }





