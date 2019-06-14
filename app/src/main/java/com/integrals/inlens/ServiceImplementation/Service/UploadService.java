package com.integrals.inlens.ServiceImplementation.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.integrals.inlens.AlbumProcedures.AlbumStoppingServices;
import com.integrals.inlens.AlbumProcedures.Checker;
import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.Helper.UploadDatabaseHelper;
import com.integrals.inlens.R;
import com.integrals.inlens.ServiceImplementation.InLensGallery.MainActivity;
import com.integrals.inlens.ServiceImplementation.Includes.UploadServiceHelper;
import java.util.ArrayList;

public class UploadService extends Service {
   private String UPLOAD_STATUS;
   private int UploadingIntegerID;
   private String CommunityID;
   private int Record;
   private Handler handler;
   private Runnable runnable;
   private Checker checker;
   private CurrentDatabase currentDatabase;
   private UploadDatabaseHelper uploadDatabaseHelper;
   private AlbumStoppingServices albumStoppingServices;


   @Override
    public void onCreate()
   {
         super.onCreate();

        handler = new Handler();

        albumStoppingServices=new AlbumStoppingServices(getApplicationContext());
        uploadDatabaseHelper = new UploadDatabaseHelper(getApplicationContext(), "", null, 1);
        currentDatabase = new CurrentDatabase(getApplicationContext(), "", null, 1);
        uploadDatabaseHelper.UpdateUploadStatus(currentDatabase.GetUploadingTargetColumn(), "NOT_UPLOADED");
        checker=new Checker(getApplicationContext());

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {

            String input = intent.getStringExtra("inputExtra");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);


            Notification notification = new NotificationCompat.Builder(this, "ID_505")
                    .setContentTitle("InLens Upload Service running background")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.inlens_notification)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);

        }


        runnable = new Runnable() {
            @Override
            public void run() {

                if(checker.isConnectedToNet())
                {
                        uploadProcedure();
                }
                else
                    {
                    Log.d("Upload","No internet");
                    albumStoppingServices.deinitiateUploadService();
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

    private void uploadProcedure()
    {
        CommunityID = currentDatabase.GetLiveCommunityID();
        UploadingIntegerID = currentDatabase.GetUploadingTargetColumn();
        Record = currentDatabase.GetUploadingTotal();
        UPLOAD_STATUS = uploadDatabaseHelper.GetUploadStatus(UploadingIntegerID);

        if ((UploadingIntegerID <= Record)) {
            try {
                if (UPLOAD_STATUS.contentEquals("NOT_UPLOADED")) {
                    initiateUploadHelper(uploadDatabaseHelper,UploadingIntegerID);
                }

                } catch (NullPointerException e) {
                e.printStackTrace();

                uploadDatabaseHelper.close();
                currentDatabase.close();

                Log.d("InLens","Cancelled upload");
                albumStoppingServices.deinitiateUploadService();

            }

        }
        else
            {
            uploadDatabaseHelper.close();
            currentDatabase.close();
            Log.d("InLens","No image to upload");
            albumStoppingServices.deinitiateUploadService();
            }

        uploadDatabaseHelper.close();
        currentDatabase.close();
    }

    private void initiateUploadHelper(UploadDatabaseHelper uploadDatabaseHelper ,
                                      int uploadingIntegerID)
    {

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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onDestroy() {
        super.onDestroy();

        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                handler.removeCallbacks(runnable);
                return null;
            }
        }.execute("");

   }

   //Upload Service perfectly done//////////////////////////////////////////////////////////////////
}
