package com.integrals.inlens.ServiceImplementation.JobScheduler;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.os.AsyncTask;
import android.util.Log;

import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;


public class JobService extends android.app.job.JobService {
    private static final String TAG = "MyJob::";
    private boolean isWorking = false;
    private boolean jobCancelled = false;
    private NotificationHelper notificationHelper;
    private RecentImage        recentImage;
    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper=new NotificationHelper(getBaseContext());
        recentImage=new RecentImage(getApplicationContext());
    }

    // Called by the Android system when it's time to run the job
    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started!");
        isWorking = true;
        // We need 'jobParameters' so we can call 'jobFinished'
       // Services do NOT run on a separate thread
        new AsyncTask(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d("MyJob::","Started");
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.d("MyJob::","Stopped");
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                 String url=recentImage.recentImagePath();
                 notificationHelper.notifyRecentImage(url);
                 return null;
                }
        }.execute("");
        return isWorking;
    }



    // Called if the job was cancelled before being finished
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before being completed.");
        jobCancelled = true;
        boolean needsReschedule = isWorking;
        jobFinished(jobParameters, needsReschedule);
        return needsReschedule;
    }
}
