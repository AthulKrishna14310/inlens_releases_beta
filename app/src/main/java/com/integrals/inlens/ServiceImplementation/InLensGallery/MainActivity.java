package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.integrals.inlens.AlbumProcedures.AlbumStartingServices;
import com.integrals.inlens.AlbumProcedures.AlbumStoppingServices;
import com.integrals.inlens.AlbumProcedures.Checker;
import com.integrals.inlens.AlbumProcedures.QuitCloudAlbumProcess;
import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.R;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;
import com.vistrav.ask.Ask;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class MainActivity extends AppCompatActivity {

    private final String DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private Activity activity;
    private RecyclerView recyclerView;
    private List<String> mFiles;
    private NotificationHelper notificationHelper;
    private DatabaseOperations databaseOperations;
    private GalleryAdapter galleryAdapter;
    private List<String> mTimes;
    private AlbumStoppingServices albumStoppingServices;
    private AlbumStartingServices albumStartingServices;
    private String albumCreated;
    private CurrentDatabase currentDatabase;
    private Queue<String> timeQueue;
    private Queue<String> imageUriQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_inlens_gallery);
        requirePermission();

        activity = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);

        databaseOperations = new DatabaseOperations(getApplicationContext());
        albumStoppingServices = new AlbumStoppingServices(getApplicationContext());
        albumStartingServices = new AlbumStartingServices(getApplicationContext());

        currentDatabase=new CurrentDatabase(getApplicationContext(),"",null,1);
        albumCreated=currentDatabase.GetAlbumCreated();
        currentDatabase.close();

        timeQueue=new LinkedList<>();
        imageUriQueue=new LinkedList<>();
        Log.d("InLens::AlbumExpiry::",albumCreated);
    }

    @Override
    protected void onStart() {

        super.onStart();

        Checker checker = new Checker(getApplicationContext());
        if (checker.isConnectedToNet()) {
            if (checker.checkIfInAlbum()) {
                if (checker.checkAlbumActiveTime() <= 0) {
                } else {
                    QuitCloudAlbumProcess quitCloudAlbumProcess = new QuitCloudAlbumProcess(
                            getApplicationContext(),
                            MainActivity.this
                    );
                    quitCloudAlbumProcess.execute();

                }
            }
        }


    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onResume()
    {
        super.onResume();

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                mFiles = FileUtil.findMediaFiles(getApplicationContext(),albumCreated);
                mTimes = FileUtil.getTimeList();

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
                galleryAdapter = new GalleryAdapter(activity, mFiles, mTimes);
                recyclerView.setAdapter(galleryAdapter);

            }
        }.execute();


    }

    private void requirePermission()
    {
        Ask.on(this)
                .id(1543) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.SYSTEM_ALERT_WINDOW
                )
                .go();

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        galleryAdapter.executeUploadDatabaseTasks(albumStartingServices);

    }

}