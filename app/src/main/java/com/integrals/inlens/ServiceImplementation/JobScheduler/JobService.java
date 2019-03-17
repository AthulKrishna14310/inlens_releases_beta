package com.integrals.inlens.ServiceImplementation.JobScheduler;

import android.app.job.JobParameters;
import android.os.AsyncTask;
import android.util.Log;


public class JobService extends android.app.job.JobService {
    private static final String TAG = "MyJob::";
    boolean isWorking = false;
    boolean jobCancelled = false;

    @Override
    public void onCreate() {
        super.onCreate();

        }

    // Called by the Android system when it's time to run the job
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
                for(int i=0;i<20;i++){
                    Log.d("MyJob::","Ongoing::");
                }
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
