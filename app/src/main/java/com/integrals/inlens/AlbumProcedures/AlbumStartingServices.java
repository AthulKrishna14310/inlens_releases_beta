package com.integrals.inlens.AlbumProcedures;

import android.content.Context;

import com.integrals.inlens.ServiceImplementation.JobScheduler.JobHelper;

public class AlbumStartingServices {
private JobHelper jobHelper;
private Context context;

    public AlbumStartingServices(Context context) {
        this.context = context;
    }

    public void initiateJobServices(){
    jobHelper=new JobHelper(context);
    jobHelper.initiateJobInfo();
    jobHelper.scheduleJob();

    }



}
