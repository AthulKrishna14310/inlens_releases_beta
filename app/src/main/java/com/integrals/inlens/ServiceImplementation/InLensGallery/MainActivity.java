package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.R;
import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.JobScheduler.JobHelper;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;
import com.vistrav.ask.Ask;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final String DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private Activity activity;
    private RecyclerView recyclerView;
    List<String> mFiles;
    // Test Implementation
    private JobHelper jobHelper;
    private NotificationHelper notificationHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inlens_gallery);
        requirePermission();
        activity=this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);

        jobHelper=new JobHelper(getApplicationContext());
        notificationHelper=new NotificationHelper(getBaseContext());
        RecentImage recentImage=new RecentImage(getApplicationContext());
        notificationHelper.notifyRecentImage(recentImage.recentImagePath());
          // media file or
//        List<String> mFiles = FileUtil.findImageFileInDirectory(DIRECTORY, new String[]{"png", "jpg"}); // device file

        //for test implementation

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onStart() {
        super.onStart();

        Snackbar.make(recyclerView,"Loading your images ..Please wait",Snackbar.LENGTH_SHORT).show();
        CurrentDatabase currentDatabase=new CurrentDatabase(getApplicationContext(),"",null,1);

        if(currentDatabase.GetAlbumExpiry()!=null) {
            new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    mFiles = FileUtil.findMediaFiles(getApplicationContext());
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }

                @Override
                protected void onProgressUpdate(Object[] values) {
                    super.onProgressUpdate(values);

                }

                @Override
                protected void onPostExecute(Object o) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new GalleryAdapter(activity, mFiles));
                        }
                    });

                }
            }.execute();
        }else{
            Snackbar.make(recyclerView,"Your album time is expired to upload photos ",Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        jobHelper=new JobHelper(getApplicationContext());
        jobHelper.initiateJobInfo();
        jobHelper.scheduleJob();

    }

    private void requirePermission() {
        Ask.on(this)
                .id(1543) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.SYSTEM_ALERT_WINDOW
                )
                .go();

    }



}
