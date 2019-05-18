package com.integrals.inlens.ViewHolder;

/**
 * Created by Athul Krishna on 08/02/2018.
 */
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import com.integrals.inlens.Models.Participants;
import com.integrals.inlens.R;


/**
 * Created by Athul Krishna on 27/08/2017.
 */

   public class AlbumViewHolder extends RecyclerView.ViewHolder {

    private View view;
    public Dialog UserDialog;
    public ImageButton DetailsAlbumn;
    public LinearLayout AlbumContainer;
    public ProgressBar MainAlbumProgressbar;

    public AlbumViewHolder(View ItemView) {
        super(ItemView);
        view=ItemView;
        DetailsAlbumn = view.findViewById(R.id.album_details_btn);
        AlbumContainer = view.findViewById(R.id.album_card_button_container);
        MainAlbumProgressbar = view.findViewById(R.id.main_album_progressbar);
        }



    public void SetAlbumCover(Context context,String Uri){

        MainAlbumProgressbar.setVisibility(View.VISIBLE);

        ImageView imageView=(ImageView)view.findViewById(R.id.CloudAlbumCover);
        RequestOptions requestOptions=new RequestOptions()
                .placeholder(R.drawable.image_avatar_background)
                .fitCenter();

        Glide.with(context)
                .load(Uri)
                .thumbnail(0.1f)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        MainAlbumProgressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        MainAlbumProgressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }
    public void SetTitle(String Text){
        TextView textView=(TextView)view.findViewById(R.id.AlbumTitle);
        textView.setText(Text);
    }

    public void SetProfilePic(Context context,String ImageUri){
        ImageView imageView=(ImageView)view.findViewById(R.id.CreatedByProfilePic);
         RequestOptions requestOptions=new RequestOptions()
                 .centerCrop()
                 .placeholder(R.drawable.ic_account_circle)
                 .override(176,176);

         Glide.with(context)
                 .load(ImageUri)
                 .thumbnail(0.1f)
                 .apply(requestOptions)
                 .into(imageView);

     }





  }