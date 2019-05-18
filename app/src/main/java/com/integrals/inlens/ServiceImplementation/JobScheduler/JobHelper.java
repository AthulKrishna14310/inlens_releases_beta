package com.integrals.inlens.ServiceImplementation.JobScheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class JobHelper {
private ComponentName componentName;
private Context       context;
private JobInfo       jobInfo;

public JobHelper( Context context) {
        this.context=context;
        this.componentName=new ComponentName(this.context,JobService.class);
    }

public void initiateJobInfo(){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        jobInfo = new JobInfo.Builder(12, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(900000)
                .build();
    }else{
        jobInfo = new JobInfo.Builder(12, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(90000)
                .build();

    }

}
public void scheduleJob(){
    JobScheduler jobScheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
    int resultCode = jobScheduler.schedule(jobInfo);
    if (resultCode == JobScheduler.RESULT_SUCCESS) {
        Log.d("Job:", "Job scheduled!");
    } else {
        Log.d("Job:", "Job not scheduled");
    }
}
}
