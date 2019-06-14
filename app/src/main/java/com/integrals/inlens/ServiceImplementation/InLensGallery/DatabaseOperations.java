package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.content.Context;
import android.util.Log;

import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.Helper.UploadDatabaseHelper;


public class DatabaseOperations {

    private UploadDatabaseHelper uploadDatabaseHelper;
    private CurrentDatabase      currentDatabase;
    private Context context;
    private String DatabaseImageUri,
    DatabaseWeatherDetails,
    DatabaseLocationDetails,
    DatabaseAudioCaptionUri,
    DatabaseTimeTaken,
    DatabaseUploadStatus,
    DatabaseTextCaption,
    DatabaseUploaderID,
    DatabaseUploaderName,
    DatabaseUploaderProfilePic,
    DatabaseCurrentTimeMilliSecond;


    public DatabaseOperations(Context context) {
        this.context = context;
        this.uploadDatabaseHelper = new UploadDatabaseHelper(context, "", null, 1);
        this.currentDatabase=new CurrentDatabase(context,"",null,1);

    }


    public void insertToDatabase(String filename,String time) {
        createDatabaseValues(filename,time);
        uploadDatabaseHelper.InsertUploadValues(
                DatabaseImageUri,
                DatabaseWeatherDetails,
                DatabaseLocationDetails,
                DatabaseAudioCaptionUri,
                DatabaseTimeTaken,
                DatabaseUploadStatus,
                DatabaseTextCaption,
                DatabaseUploaderID,
                DatabaseUploaderName,
                DatabaseUploaderProfilePic,
                DatabaseCurrentTimeMilliSecond

        );
        currentDatabase = new CurrentDatabase(context, "", null, 1);
        int Value = currentDatabase.GetUploadingTotal();
        currentDatabase.ResetUploadTotal((Value + 1));
        Log.d("InLens::",currentDatabase.GetUploadingTotal()+"");
        Log.d("InLens::",currentDatabase.GetUploadingTargetColumn()+"");

    }

    private void createDatabaseValues(String filename,String time) {
        String Default = "SS";
        DatabaseImageUri = filename;
        DatabaseCurrentTimeMilliSecond = String.valueOf(System.currentTimeMillis());
        DatabaseUploadStatus = "NOT_UPLOADED";
        DatabaseTimeTaken = time;
    }

    public void closeDatabases(){
        currentDatabase.close();
        uploadDatabaseHelper.getWritableDatabase().close();
    }

}
