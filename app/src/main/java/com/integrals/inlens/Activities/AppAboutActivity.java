package com.integrals.inlens.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.integrals.inlens.MainActivity;
import com.integrals.inlens.R;

import com.squareup.picasso.Picasso;

public class AppAboutActivity extends AppCompatActivity {

    private ViewFlipper TourViewFlipper;
    private Boolean SHOW_TOUR = true;
    private String StringShowTour;
    private ImageButton CloseTourImageButton;
    private int Images[] = {R.drawable.intro_slide_1,R.drawable.intro_slide_2,R.drawable.intro_slide_3,R.drawable.intro_slide_4};
    private TextView AppAboutTextview;
    private String AppAbout = "Inlens is android application dedicated for providing unlimited storage to all its customers at zero cost. An online" +
            " gallery that can have infinite number of participants with equal access, except for the admin who is the owner of a particular album. " +
            "Your memories are precious. We will keep them safe."+"\n\n Your Inlens Team \n\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_about);

        getSupportActionBar().hide();
        TourViewFlipper = findViewById(R.id.tour_viewflipper);
        CloseTourImageButton = findViewById(R.id.close_tour_imagebutton);
        AppAboutTextview = findViewById(R.id.app_about_textview);
        AppAboutTextview.setText(AppAbout);

        StringShowTour = getIntent().getStringExtra("ShowTour");
        if(StringShowTour.equals("no"))
        {
            SHOW_TOUR = false;
        }
        else
        {
            SHOW_TOUR=true;
        }

        CloseTourImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AppAboutActivity.this, MainActivity.class).putExtra("ShowTour",SHOW_TOUR));
                overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
                finish();

            }
        });

        for(int i=0; i< Images.length;i++)
        {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(Images[i]).into(imageView);
            TourViewFlipper.addView(imageView);

            TourViewFlipper.setInAnimation(getApplicationContext(),R.anim.cloud_album_fade_in);
            TourViewFlipper.setOutAnimation(getApplicationContext(),R.anim.cloud_album_fade_out);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            startActivity(new Intent(AppAboutActivity.this,MainActivity.class));
            overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
