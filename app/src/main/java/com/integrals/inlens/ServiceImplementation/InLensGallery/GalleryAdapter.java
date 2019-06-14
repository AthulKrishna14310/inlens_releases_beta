package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.integrals.inlens.AlbumProcedures.AlbumStartingServices;
import com.integrals.inlens.R;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.CustomViewHolder> {

    private final List<String> mFileList;
    private final Activity mActivity;
    private final List<String> mTimeList;
    private Queue<String> imageUriQueue;
    private Queue<String> timeQueue;
    private DatabaseOperations databaseOperations;

    public GalleryAdapter(Activity activity, List<String> fileList,List<String> mTimeList)
    {
        this.mActivity = activity;
        this.mFileList = fileList;
        this.mTimeList=mTimeList;
        databaseOperations=new DatabaseOperations(activity);
        imageUriQueue=new LinkedList<>();
        timeQueue=new LinkedList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_list_row, parent, false);
        return new GalleryAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position)
    {


            RequestOptions requestOptions=new RequestOptions()
                    .override(200,200)
                    .centerCrop();
        Glide
                .with(mActivity)
                .load(mFileList.get(position))
                .apply(requestOptions)
                .into(holder.imageResource);

        final int itemPosition = holder.getAdapterPosition();
        holder.imageResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, mFileList.get(itemPosition), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder ImageDialog = new AlertDialog.Builder(mActivity);
                ImageDialog.setTitle("UPLOAD QUEUE");
                final ImageView showImage = new ImageView(mActivity);
                showImage.setMinimumHeight(600);
                showImage.setMinimumWidth(600);
                ImageDialog.setView(showImage);

                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(mActivity)
                        .load(mFileList.get(itemPosition))
                        .thumbnail(0.5f)
                        .apply(requestOptions)
                        .into((showImage));

                ImageDialog.setPositiveButton("Add to upload Queue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        timeQueue.add(mTimeList.get(itemPosition).trim());
                        imageUriQueue.add(mFileList.get(itemPosition).trim());
                        Toast.makeText(mActivity,"Image added to upload queue",Toast.LENGTH_SHORT).show();


                    }
                });
                ImageDialog.show();
            }
        });
    }

    @Override
    public int getItemCount()

    {
        return mFileList.size();
    }

    public void executeUploadDatabaseTasks(final AlbumStartingServices albumStartingServices) {
        new AsyncTask(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(mActivity,"Adding images to upload database",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                if (!imageUriQueue.isEmpty()) {
                    while (!imageUriQueue.isEmpty()) {
                        databaseOperations.insertToDatabase(imageUriQueue.remove(), timeQueue.remove());
                    }
                }else{
                    Log.d("InLens","No image to upload");
                }
                databaseOperations.closeDatabases();
                albumStartingServices.initiateUploadService();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        }.execute("");


    }

    static class CustomViewHolder extends RecyclerView.ViewHolder
    {
        final ImageView imageResource;
        CustomViewHolder(View itemView) {
            super(itemView);
            this.imageResource = (ImageView) itemView.findViewById(R.id.image_resource);
        }
    }

    public Queue<String> getTimeQueue() {
        Log.d("InLens::",""+timeQueue.size());
        return timeQueue;

    }


    public Queue<String> getImageUriQueue() {
        Log.d("InLens::",""+imageUriQueue.size());

        return imageUriQueue;
    }
}
