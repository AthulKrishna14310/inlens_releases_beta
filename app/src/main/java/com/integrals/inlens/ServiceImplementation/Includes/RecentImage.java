package com.integrals.inlens.ServiceImplementation.Includes;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import java.io.File;

public class RecentImage {
    private final String[][] projection = new String[1][1];
    private Cursor cursor;
    private String tempImagePathString;
    private String recentImageStringPath;
    private Context context;

    public RecentImage(Context context)
    {
        this.context = context;
        tempImagePathString="";
    }

    public String recentImagePath()
    {

        projection[0] = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.MIME_TYPE

        };


        cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection[0], null, null,
                        MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC");

        //Cursor Control
        try {

            if (cursor.moveToFirst())

            {
                tempImagePathString = cursor.getString(1);
                File file = new File(tempImagePathString);

                if (file.exists()) {
                    if ((!tempImagePathString.contains("/WhatsApp/"))
                            && !tempImagePathString.contains("/Screenshots/")
                            ) {
                        recentImageStringPath=tempImagePathString;
                    }

                } else {

                    projection[0] = null;
                    cursor.close();
                }

            }
        }
        //Error Fix 7
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return recentImageStringPath;
    }
}
