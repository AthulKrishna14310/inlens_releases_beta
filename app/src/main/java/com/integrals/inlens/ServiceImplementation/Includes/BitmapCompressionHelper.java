package com.integrals.inlens.ServiceImplementation.Includes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class BitmapCompressionHelper  {

    private File   bitmapFile;
    private Bitmap result;
    private Context context;
    private Uri     resultUri;
    private File    pictureFile;
    private int COMPRESSION_HEIGHT=400;
    private int COMPRESSION_WIDTH=400;
    private int COMPRESSION_QUALITY=90;

    public BitmapCompressionHelper(File bitmapFile, Bitmap result,
                                   Context context, Uri resultUri,
                                   File pictureFile)
    {
        this.bitmapFile = bitmapFile;
        this.result = result;
        this.context = context;
        this.resultUri = resultUri;
        this.pictureFile = pictureFile;
        Log.d("inLens_upload","Bitmap compression initialised as constructor");

    }


    public void compressUploadFile()
    {
        try {
            compressionDimensions(bitmapFile);
            result = new Compressor(context)
                    .setMaxHeight(COMPRESSION_HEIGHT)
                    .setMaxWidth(COMPRESSION_WIDTH)
                    .setQuality(COMPRESSION_QUALITY)
                    .compressToBitmap(bitmapFile);
            Log.d("inLens_upload","Compressed upload file");
            storeImage(result);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void compressThumbImageFile()
    {
        try {
            result = new Compressor(context)
                    .setMaxHeight(130)
                    .setMaxWidth(130)
                    .setQuality(70)
                    .compressToBitmap(bitmapFile);
            Log.d("inLens_upload","Compressed thumb image file");
            storeImage(result);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void compressionDimensions(File file)
    {

        if(file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            if (bitmap.getHeight() > bitmap.getWidth()) {
                COMPRESSION_HEIGHT = 640;
                COMPRESSION_WIDTH = 480;
                COMPRESSION_QUALITY = 90;
            } else {
                COMPRESSION_WIDTH = 640;
                COMPRESSION_HEIGHT = 480;
                COMPRESSION_QUALITY = 90;
            }

        }
    }

    private void storeImage(Bitmap image)
    {
        pictureFile = getOutputMediaFile();

        if (pictureFile == null) {
            Toast.makeText(context, "Unable to create file " +
                    ",Please check Storage Permission", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            resultUri = Uri.fromFile(pictureFile);

            Log.d("inLens_upload","Result image uri generated");

            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error accessing file.", Toast.LENGTH_SHORT).show();
        }
    }

    private File getOutputMediaFile()
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName = "InLens_" + System.currentTimeMillis() + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        Log.d("inLens_upload","Compressed image file saved");
        return mediaFile;
    }

    public void deleteFile(File file)
    {
        file.delete();
        Log.d("inLens_upload","Deleting file");
    }


    public Uri getResultUri() {
        return resultUri;
    }

    public void setResultUri(Uri resultUri) {
        this.resultUri = resultUri;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public File getBitmapFile() {
        return bitmapFile;
    }

    public void setBitmapFile(File bitmapFile) {
        this.bitmapFile = bitmapFile;
    }

    public Bitmap getResult() {
        return result;
    }

    public void setResult(Bitmap result) {
        this.result = result;
    }

    public int getCOMPRESSION_HEIGHT() {
        return COMPRESSION_HEIGHT;
    }

    public void setCOMPRESSION_HEIGHT(int COMPRESSION_HEIGHT) {
        this.COMPRESSION_HEIGHT = COMPRESSION_HEIGHT;
    }

    public int getCOMPRESSION_WIDTH() {
        return COMPRESSION_WIDTH;
    }

    public void setCOMPRESSION_WIDTH(int COMPRESSION_WIDTH) {
        this.COMPRESSION_WIDTH = COMPRESSION_WIDTH;
    }

    public int getCOMPRESSION_QUALITY() {
        return COMPRESSION_QUALITY;
    }

    public void setCOMPRESSION_QUALITY(int COMPRESSION_QUALITY) {
        this.COMPRESSION_QUALITY = COMPRESSION_QUALITY;
    }


}
