package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.integrals.inlens.GridView.MainActivity;
import com.integrals.inlens.R;

import java.util.List;

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.CustomViewHolder> {

    private final List<String> mFileList;
    private final Activity mActivity;
    private final List<String> mTimeList;
    private  DatabaseOperations databaseOperations;
    public GalleryAdapter(Activity activity, List<String> fileList,List<String> mTimeList) {
        this.mActivity = activity;
        this.mFileList = fileList;
        this.mTimeList=mTimeList;
        this.databaseOperations=new DatabaseOperations(mActivity);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_list_row, parent, false);
        return new GalleryAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {


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
                        databaseOperations.insertToDatabase(mFileList.get(itemPosition).toString().trim(),
                                mTimeList.get(itemPosition).toString().trim());

                        Toast.makeText(mActivity,"Image added to upload queue",Toast.LENGTH_SHORT).show();


                    }
                });
                ImageDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageResource;
        CustomViewHolder(View itemView) {
            super(itemView);
            this.imageResource = (ImageView) itemView.findViewById(R.id.image_resource);
        }
    }
}
