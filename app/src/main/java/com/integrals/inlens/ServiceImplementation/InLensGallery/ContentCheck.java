package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.content.Context;
import com.integrals.inlens.Helper.CurrentDatabase;


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
}
