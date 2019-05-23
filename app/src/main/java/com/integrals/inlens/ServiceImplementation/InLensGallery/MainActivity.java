package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.Helper.UploadDatabaseHelper;
import com.integrals.inlens.R;
import com.integrals.inlens.ServiceImplementation.Includes.RecentImage;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;
import com.vistrav.ask.Ask;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final String DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private Activity     activity;
    private RecyclerView recyclerView;
    private List<String> mFiles;
    private NotificationHelper notificationHelper;
    private DatabaseOperations databaseOperations;
    private GalleryAdapter galleryAdapter;
    private List<String> mTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inlens_gallery);
        requirePermission();
        activity=this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        databaseOperations=new DatabaseOperations(getApplicationContext());

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
                    mTimes=FileUtil.getTimeList();
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
                    galleryAdapter=new GalleryAdapter(activity,mFiles,mTimes);
                    recyclerView.setAdapter(galleryAdapter);
                }
            }.execute();
        }else{
            Snackbar.make(recyclerView,"Your album time is expired to upload photos ",Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();



        Boolean Default = false;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("InCommunity.pref", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("UsingCommunity::", Default) == true) {
            notificationHelper=new NotificationHelper(getBaseContext());
            RecentImage recentImage=new RecentImage(getApplicationContext());
            notificationHelper.notifyRecentImage(recentImage.recentImagePath());

        }

    }
}
