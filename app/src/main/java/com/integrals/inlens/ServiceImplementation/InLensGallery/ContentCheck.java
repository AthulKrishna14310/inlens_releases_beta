package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.content.Context;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.Toast;

import com.integrals.inlens.Helper.CurrentDatabase;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ContentCheck {
    private String  ImageName;
    private boolean isPresent=false;
    private Context context;

    public ContentCheck(String imageName,Context context)
    {
        ImageName = imageName;
        this.context=context;
    }

    public String getImageAddedDate(String filePath)
    {
        String stringDate=null;
        File file = new File(filePath);
        if(file.exists()) //Extra check, Just to validate the given path
        {
            ExifInterface intf = null;
            try
            {
                intf = new ExifInterface(filePath);
                if(intf != null)
                stringDate = intf.getAttribute(ExifInterface.TAG_DATETIME);
                else {
                    Date lastModDate = new Date(file.lastModified());
                    stringDate = lastModDate.toString();
                   }
            } catch (NullPointerException e){
               e.printStackTrace();
               Log.d("InLens:","NullPointException");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("InLens:","IOException");

            }
        }

        return stringDate;
    }

    public boolean isImageDateAfterAlbumCreatedDate(String imageDate,String albumCreatedDate)
    {
        Log.d("InLens_Compare:",imageDate);
        Log.d("InLens_Compare:",albumCreatedDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date d1 = null, d2 = null;
            try {

                d1 = dateFormat.parse(imageDate);
                d2 = dateFormat.parse(albumCreatedDate);

            } catch (ParseException e) {
               e.printStackTrace();
                Log.d("InLens::","Parse Exception");

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                if(d1.compareTo(d2)>=0) {
                    return true;
                }
                else {
                    return false;
                     }

            } catch (NullPointerException e) {
            Log.d("InLens::","Null for date");
            }


            return false;
    }



}
