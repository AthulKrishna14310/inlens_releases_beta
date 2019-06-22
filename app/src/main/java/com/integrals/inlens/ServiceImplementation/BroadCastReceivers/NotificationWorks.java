package com.integrals.inlens.ServiceImplementation.BroadCastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Athul Krishna on 03/02/2018.
 */

public  class NotificationWorks extends BroadcastReceiver {

    private String UPLOAD_INTENT="com.InLens.UPLOAD";
    private String RIGHT_INTENT="com.InLens.RIGHT";
    private String LEFT_INTENT="com.InLens.LEFT";
    private int index=1;

   public NotificationWorks() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(UPLOAD_INTENT)) {
            Log.d("InLens::","Notification upload");
        }
        else if (intent.getAction().equals(RIGHT_INTENT)){
            Log.d("InLens::","Notification right");

        }
        else if(intent.getAction().equals(LEFT_INTENT)) {
            Log.d("InLens::","Notification left");

        }

    }



}
