package com.integrals.inlens.AlbumProcedures;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class QuitCloudAlbumProcess {
    private Context context;
    private Activity activity;

    public QuitCloudAlbumProcess(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void execute(){
        final AlbumStoppingServices albumStoppingServices=new AlbumStoppingServices(context);
        new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] objects) {
                albumStoppingServices.deinitiateJobServices();
                albumStoppingServices.deinitiateAllSharedPreferences();
                albumStoppingServices.deinitiateDatabaseIfOwner();

                return null; }

            @Override
            protected void onPostExecute(Object o) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(true);
                builder.setTitle(" Cloud-Album Expired.");

                builder.setMessage("Your Album-Time had been expired. " +
                        "Now you can only upload images to Cloud-Album from inLens " +
                        "gallery. You can no longer capture the image to this " +
                        "inLens gallery");

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });
                builder.setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
            }

        }.execute("");

    }

}
