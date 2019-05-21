package com.integrals.inlens.ServiceImplementation.JobScheduler;

import android.app.job.JobParameters;
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

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started!");
        isWorking = true;

        notificationHelper.cancelNotificationRecentImage();
        String url=recentImage.recentImagePath();
        notificationHelper.notifyRecentImage(url);
        return isWorking;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before being completed.");
        jobCancelled = true;
        boolean needsReschedule = isWorking;
        jobFinished(jobParameters, needsReschedule);
        return needsReschedule;
    }
}
