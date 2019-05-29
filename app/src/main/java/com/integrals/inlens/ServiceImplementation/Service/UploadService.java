package com.integrals.inlens.ServiceImplementation.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.integrals.inlens.AlbumProcedures.Checker;
import com.integrals.inlens.GridView.MainActivity;
import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.Helper.NotificationHelper;
import com.integrals.inlens.Helper.UploadDatabaseHelper;
import com.integrals.inlens.R;
import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.Includes.UploadServiceHelper;

import java.util.ArrayList;

import static android.os.Build.VERSION.SDK;

public class UploadService extends Service {
   private String UPLOAD_STATUS;
   private int UploadingIntegerID;
   private String CommunityID;
   private int Record;
   private NotificationHelper notificationHelper;
   private NotificationManager UploadnotificationManager;
   private NotificationCompat.Builder Uploadbuilder;
   private Handler handler;
   private Runnable runnable;
   private Checker checker;

   @Override
    public void onCreate() {
         super.onCreate();

        notificationHelper=new NotificationHelper(getBaseContext());
        handler = new Handler();

       UploadDatabaseHelper uploadDatabaseHelper = new UploadDatabaseHelper(getApplicationContext(), "", null, 1);
       CurrentDatabase currentDatabase = new CurrentDatabase(getApplicationContext(), "", null, 1);
       uploadDatabaseHelper.UpdateUploadStatus(currentDatabase.GetUploadingTargetColumn(), "NOT_UPLOADED");
       currentDatabase.close();
       uploadDatabaseHelper.close();

       checker=new Checker(getApplicationContext());
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


        runnable = new Runnable() {
            @Override
            public void run() {

                if(checker.isConnectedToNet()) {
                    uploadProcedure();
                }
                else{

                }

                handler.postDelayed(this, 10000);
            }
        };

        handler.postDelayed(runnable, 9500);

                return START_NOT_STICKY;


   }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void uploadProcedure(){

        CurrentDatabase currentDatabase = new CurrentDatabase(getApplicationContext(), "", null, 1);
        CommunityID = currentDatabase.GetLiveCommunityID();
        UploadingIntegerID = currentDatabase.GetUploadingTargetColumn();
        Record = currentDatabase.GetUploadingTotal();
        currentDatabase.close();


        UploadDatabaseHelper uploadDatabaseHelper = new UploadDatabaseHelper(getApplicationContext(), "", null, 1);
        UPLOAD_STATUS = uploadDatabaseHelper.GetUploadStatus(UploadingIntegerID);
        if ((UploadingIntegerID <= Record)) {
            try {
                if (UPLOAD_STATUS.contentEquals("NOT_UPLOADED")) {
                    initiateUploadHelper(uploadDatabaseHelper,UploadingIntegerID);
                }

                } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

        uploadDatabaseHelper.close();

    }

    private void initiateUploadHelper(UploadDatabaseHelper uploadDatabaseHelper ,
                                      int uploadingIntegerID) {

        ArrayList<String> list=new ArrayList<>();
        list.add(uploadDatabaseHelper.GetPhotoUri(uploadingIntegerID));
        UploadServiceHelper uploadServiceHelper=new UploadServiceHelper(
                getApplicationContext(),
                list,""
                ,uploadDatabaseHelper.GetTimeTaken(uploadingIntegerID),uploadDatabaseHelper
        );
        Log.d("Upload::Status",uploadDatabaseHelper.GetUploadStatus(uploadingIntegerID));
        Log.d("Upload::URI",uploadDatabaseHelper.GetPhotoUri(uploadingIntegerID));
        uploadServiceHelper.initiateUploadOperation();
        uploadServiceHelper.proceedUploadOperation();


   }


}
