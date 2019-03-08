package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.content.Context;
import android.media.ExifInterface;
import android.util.Log;

import com.integrals.inlens.Helper.CurrentDatabase;

import java.io.File;
import java.io.IOException;
import java.util.Date;


public class ContentCheck {
    private String  ImageName;
    private boolean isPresent=false;
    private Context context;

    public ContentCheck(String imageName,Context context) {
        ImageName = imageName;
        this.context=context;
    }

    public boolean isImagePresent(){
        return isPresent;
    }

    public String  getExpiryDate(){
        CurrentDatabase currentDatabase=new CurrentDatabase(context,"",null,1);
        String ExpiryDate=currentDatabase.GetAlbumExpiry().toString().trim();
        return ExpiryDate;
    }


    public String getImageAddedDate(String filePath){
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
                } catch (NullPointerException e){
               e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringDate;
    }

    public String getImageModifiedDate(String filePath){
        String stringDate=null;
        File file = new File(filePath);
        if(file.exists()) //Extra check, Just to validate the given path
        {
                Date lastModDate = new Date(file.lastModified());
                stringDate=lastModDate.toString();
        }


        return stringDate;
    }





}
