package com.integrals.inlens.ServiceImplementation.JobScheduler;

import android.app.job.JobParameters;
import android.util.Log;
import android.widget.Toast;

import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;

public class JobService extends android.app.job.JobService {
    private static final String TAG = "MyJob::";
    boolean isWorking = false;
    boolean jobCancelled = false;
    private NotificationHelper notificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper=new NotificationHelper(getBaseContext());
        }

    // Called by the Android system when it's time to run the job
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started!");
        isWorking = true;
        notificationHelper.notifyRecentImage();
        // We need 'jobParameters' so we can call 'jobFinished'
        startWorkOnNewThread(jobParameters); // Services do NOT run on a separate thread

        return isWorking;
    }

    private void startWorkOnNewThread(final JobParameters jobParameters) {
        new Thread(new Runnable() {
            public void run() {
                doWork(jobParameters);
            }
        }).start();
    }

    private void doWork(JobParameters jobParameters) {
        // 10 seconds of working (1000*10ms)
        for (int i = 0; i < 1000; i++) {
            // If the job has been cancelled, stop working; the job will be rescheduled.
            if (jobCancelled)
                return;

            try {
                Thread.sleep(10);
                Log.d(TAG,"Ongoing ..");
            } catch (Exception e) { }
        }

        Log.d(TAG, "Job finished!");
        isWorking = false;
        boolean needsReschedule = false;
        jobFinished(jobParameters, needsReschedule);
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
