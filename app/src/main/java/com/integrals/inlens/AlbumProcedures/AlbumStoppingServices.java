package com.integrals.inlens.AlbumProcedures;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.ServiceImplementation.JobScheduler.JobHelper;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;

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

    public void deinitiateJobServices(){

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

        SharedPreferences sharedPreferencesCX = context.getSharedPreferences(
                "InCommunity.pref", MODE_PRIVATE);
        SharedPreferences.Editor editorCX = sharedPreferencesCX.edit();
        editorCX.putBoolean("UsingCommunity::", false);
        editorCX.commit();

    }

    public void deinitiateDatabaseIfOwner(){

        final SharedPreferences sharedPreferences4 = context.getSharedPreferences("Owner.pref",
                MODE_PRIVATE);
        CurrentDatabase currentDatabase = new CurrentDatabase(context, "", null, 1);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Communities")
                .child(currentDatabase.GetLiveCommunityID())
                .child("ActiveIndex");

        if (sharedPreferences4.getBoolean("ThisOwner::", false) == true) {
            databaseReference.setValue("F")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"Quit Cloud-Album succesfully",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context,"Failed to quit Cloud-Album  , Please check your " +
                                    "internet connection",
                            Toast.LENGTH_LONG).show();

                }
            });
        } else {
               }

    }


     //Need to write code for to stop services
}

