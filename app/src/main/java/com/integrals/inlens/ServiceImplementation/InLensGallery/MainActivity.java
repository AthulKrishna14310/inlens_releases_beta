package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.integrals.inlens.R;
import com.vistrav.ask.Ask;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String DIRECTORY = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inlens_gallery);
        requirePermission();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        List<String> mFiles = FileUtil.findMediaFiles(getApplicationContext()); // media file or
//        List<String> mFiles = FileUtil.findImageFileInDirectory(DIRECTORY, new String[]{"png", "jpg"}); // device file
        recyclerView.setAdapter(new GalleryAdapter(this, mFiles));
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
