package com.integrals.inlens.Helper;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.integrals.inlens.Services.InLensJobScheduler;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class JobSchedulerHelper {
    private ComponentName componentName;
    private static final int JOB_ID=7905;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;
    private Context context;

    public JobSchedulerHelper(Context context) {
        this.context=context;

        componentName= new ComponentName(context, InLensJobScheduler.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setPeriodic(15*60*1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        jobInfo = builder.build();
        jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);

    }

    public void startJobScheduler(){
        jobScheduler.schedule(jobInfo);


    }
    public void stopJobScheduler(){
        jobScheduler.cancel(7905);


    }
}
