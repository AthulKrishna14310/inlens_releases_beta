package com.integrals.inlens.ViewHolder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.integrals.inlens.Models.Blog;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.integrals.inlens.Helper.PhotoListHelper;
import com.integrals.inlens.R;
import com.integrals.inlens.Models.SituationModel;


public class SituationAdapter extends RecyclerView.Adapter<SituationAdapter.SituationViewHolder> {

    Context context;
    List<SituationModel> Situation;
    List<String> SIdList;
    DatabaseReference databaseReference;
    String CommunityID;
    Activity CloudAlbum;
    List MembersList = new ArrayList();
    Dialog Renamesituation;
    PhotoListHelper photoListHelper;
    DatabaseReference databaseReferencePhotoList;
    private Dialog mBottomSheetDialog;
    private RecyclerView mBottomSheetDialogRecyclerView;
    private ImageButton mBottomSheetDialogCloseBtn;
    private TextView mBottomSheetDialogTitle;
    private ProgressBar mBottomSheetDialogProgressbar;
    private List<Blog>          BlogList,OrgBlogList;
    private List<String>        BlogListID;
    private String              TimeEnd,TimeStart,GlobalID;
    private Boolean             LastPost;
    private String              PhotoThumb;
    private String              BlogTitle,ImageThumb,BlogDescription,Location;
    private String              TimeTaken,UserName,User_ID,WeatherDetails,PostedByProfilePic,OriginalImageName;

    public SituationAdapter(Context context,
                            List<SituationModel> situation,
                            List<String> SIdList,
                            DatabaseReference databaseReference,
                            DatabaseReference db,
                            String communityID,
                            Activity cloudAlbum,
                            Dialog mBottomSheetDialog,
                            RecyclerView mBottomSheetDialogRecyclerView,
                            ImageButton mBottomSheetDialogCloseBtn,
                            TextView mBottomSheetDialogTitle,
                            ProgressBar mBottomSheetDialogProgressbar) {
        this.context = context;
        Situation = situation;
        this.SIdList = SIdList;
        this.databaseReference = databaseReference;
        CommunityID = communityID;
        CloudAlbum = cloudAlbum;
        this.databaseReferencePhotoList = db;
        this.mBottomSheetDialog = mBottomSheetDialog;
        this.mBottomSheetDialogCloseBtn = mBottomSheetDialogCloseBtn;
        this.mBottomSheetDialogRecyclerView = mBottomSheetDialogRecyclerView;
        this.mBottomSheetDialogProgressbar = mBottomSheetDialogProgressbar;
        this.mBottomSheetDialogTitle = mBottomSheetDialogTitle;
        OrgBlogList = new ArrayList<>();

    }


    @NonNull
    @Override
    public SituationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cloud_album_layout, parent, false);
        return new SituationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SituationViewHolder holder, final int position) {


        if(Situation.get(position).getTime().contains("/") ||Situation.get(position).getTime().contains("-")  )
        {
            holder.Time.setText("created on : " + Situation.get(position).getTime());
        }
        else
        {
            long time = Long.parseLong(Situation.get(position).getTime());
            CharSequence Time = DateUtils.getRelativeDateTimeString(context, time, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
            String timesubstring = Time.toString().substring(Time.length() - 8);
            Date date = new Date(time);
            String dateformat = DateFormat.format("dd-MM-yyyy", date).toString();
            holder.Time.setText("created on : " + dateformat + " @ " + timesubstring);
        }



        holder.Title.setText(String.format("%s", Situation.get(position).getTitle()));

        holder.EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog(position);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    try {
                        photoListHelper = new PhotoListHelper(context, CloudAlbum, databaseReferencePhotoList);
                        photoListHelper.DisplayBottomSheet(
                                mBottomSheetDialog,
                                mBottomSheetDialogRecyclerView,
                                mBottomSheetDialogCloseBtn,
                                mBottomSheetDialogTitle,
                                mBottomSheetDialogProgressbar,
                                Situation.get(position).getSituationTime(),
                                Situation.get(position + 1).getSituationTime(),
                                CommunityID,
                                Situation.get(position).getTitle(),
                                false);

                    } catch (IndexOutOfBoundsException e) {
                        photoListHelper = new PhotoListHelper(context, CloudAlbum, databaseReferencePhotoList);
                        photoListHelper.DisplayBottomSheet(
                                mBottomSheetDialog,
                                mBottomSheetDialogRecyclerView,
                                mBottomSheetDialogCloseBtn,
                                mBottomSheetDialogTitle,
                                mBottomSheetDialogProgressbar,
                                Situation.get(position).getSituationTime(),
                                Situation.get(position).getSituationTime(),
                                CommunityID,
                                Situation.get(position).getTitle(),
                                true);

                    }

            }
        });




    }


    @Override
    public int getItemCount() {
        return Situation.size();
    }


    private void ShowDialog(final int position) {

        BottomSheet.Builder BottomS = new BottomSheet.Builder(CloudAlbum);
        BottomS.title("Edit Situation : " + Situation.get(position).getTitle())
                .sheet(R.menu.situationmenu).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i) {
                    case R.id.renamesituation:
                        RenameSituation(SIdList.get(position));
                        Renamesituation.show();
                        break;
                    case R.id.deletesituation: {
                        if (SIdList.size() <= 1) {
                            Toast.makeText(context, "Unable to perform deletion. Album should have at least one situation.", Toast.LENGTH_LONG).show();
                        } else {

                            final android.app.AlertDialog.Builder alertbuilder = new android.app.AlertDialog.Builder(CloudAlbum);
                            alertbuilder.setTitle("Delete Situation " + Situation.get(position).getTitle())
                                    .setMessage("You are about to delete the situation. Are you sure you want to continue ?")
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, int i) {


                                            databaseReference.child(SIdList.get(position)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Toast.makeText(context, "Successfully deleted the situation", Toast.LENGTH_LONG).show();
                                                    dialogInterface.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(context, "Failed to delete the situation", Toast.LENGTH_LONG).show();
                                                    dialogInterface.dismiss();
                                                }
                                            });


                                        }
                                    })
                                    .create()
                                    .show();

                        }

                    }

                    break;
                }

            }
        }).show();

    }


    private void RenameSituation(final String s) {

        Renamesituation = new Dialog(CloudAlbum,android.R.style.Theme_Light_NoTitleBar);
        Renamesituation.setContentView(R.layout.create_new_situation_layout);
        Renamesituation.setCancelable(false);
        Renamesituation.setCanceledOnTouchOutside(true);
        Renamesituation.getWindow().getAttributes().windowAnimations = R.style.UpBottomSlideDialogAnimation;

        Window Renamesituationwindow = Renamesituation.getWindow();
        Renamesituationwindow.setGravity(Gravity.TOP);
        Renamesituationwindow.setLayout(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT);
        Renamesituationwindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        Renamesituationwindow.setDimAmount(0.75f);
        Renamesituationwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final EditText SituationName = Renamesituation.findViewById(R.id.situation_name);
        SituationName.requestFocus();
        Button Done, Cancel;
        Done = Renamesituation.findViewById(R.id.done_btn);
        Cancel = Renamesituation.findViewById(R.id.cancel_btn);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(SituationName.getText().toString())) {
                    databaseReference.child(s).child("name").setValue(SituationName.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(context, "Situation renamed as : " + SituationName.getText().toString(), Toast.LENGTH_SHORT).show();
                                SituationName.setText("");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            if (e.toString().contains("FirebaseNetworkException"))
                                Toast.makeText(context, "Not Connected to Internet.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, "Unable to rename new Situation.", Toast.LENGTH_SHORT).show();

                            SituationName.setText("");
                        }
                    });
                    Renamesituation.dismiss();
                } else {
                    Toast.makeText(context, "No name given", Toast.LENGTH_LONG).show();
                }

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Renamesituation.dismiss();
            }
        });

    }


    public class SituationViewHolder extends RecyclerView.ViewHolder {

        TextView Name, Time, Title;
        ImageButton EditButton;
        RelativeLayout relativeLayout;
        ImageView CloudAlbumLayout_ImageView;

        public SituationViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.createdby);
            EditButton =  itemView.findViewById(R.id.EditSituationCard);
            Time = itemView.findViewById(R.id.SituationTimeCL);
            Title = itemView.findViewById(R.id.SituationNametextViewCloud_Layout);
            relativeLayout=itemView.findViewById(R.id.RelativeExtraTouch);
            CloudAlbumLayout_ImageView = itemView.findViewById(R.id.cloud_album_layout_imageview);
        }
    }

    private boolean CheckIntervel(String timeTaken, String timeStart, String timeEnd) {
        Boolean Result=false;
        try{
            SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
            java.util.Date dt_1 = objSDF.parse(timeTaken);
            java.util.Date dt_2 = objSDF.parse(timeStart);
            java.util.Date dt_3=  objSDF.parse(timeEnd);
            if(LastPost==false) {
                if (dt_1.after(dt_2) && (dt_1.before(dt_3))) {
                    Result = true;
                }
            }
            else
            {
                if(dt_1.after(dt_2)){
                    Result=true;
                }
            }

        }
        catch (ParseException e){
            Toast.makeText(context,"Parse Exception",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return Result;
    }

    public List<Blog> GetBlogItems(String timeStart,
                                String timeEnd,
                                String globalID,
                                Boolean lastPost) {
        BlogList=new ArrayList<>();
        BlogListID=new ArrayList<>();
        TimeStart=timeStart;
        TimeEnd=timeEnd;
        CommunityID=globalID;
        LastPost=lastPost;
        try {

            databaseReferencePhotoList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BlogList.clear();
                    BlogListID.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        if (snapshot.hasChildren()) {
                            try {


                                if (CheckIntervel(snapshot.child("TimeTaken").getValue().toString(), TimeStart, TimeEnd)) {
                                    String BlogListIDString = snapshot.getKey();
                                    if (snapshot.hasChild("Image")) {
                                        String photoThumb = snapshot.child("Image").getValue().toString();
                                        PhotoThumb = photoThumb;
                                    }

                                    if (snapshot.hasChild("BlogTitle")) {
                                        String blogTitle = snapshot.child("BlogTitle").getValue().toString();
                                        BlogTitle = blogTitle;
                                    }

                                    if (snapshot.hasChild("Location")) {
                                        String location = snapshot.child("Location").getValue().toString();
                                        Location = location;
                                    }

                                    if (snapshot.hasChild("TimeTaken")) {
                                        String timeTaken = snapshot.child("TimeTaken").getValue().toString();
                                        TimeTaken = timeTaken;
                                    }

                                    if (snapshot.hasChild("OriginalImageName")) {
                                        String originalImageName = snapshot.child("OriginalImageName").getValue().toString();
                                        OriginalImageName = originalImageName;
                                    }
                                    if (snapshot.hasChild("ImageThumb")) {
                                        String imageThumb = snapshot.child("ImageThumb").getValue().toString();
                                        ImageThumb = imageThumb;
                                    }


                                    if (snapshot.hasChild("WeatherDetails")) {
                                        String weatherDetails = snapshot.child("WeatherDetails").getValue().toString();
                                        WeatherDetails = weatherDetails;
                                    }


                                    if (snapshot.hasChild("UserName")) {
                                        String userName = snapshot.child("UserName").getValue().toString();
                                        UserName = userName;
                                    }


                                    if (snapshot.hasChild("User_ID")) {
                                        String user_id = snapshot.child("User_ID").getValue().toString();
                                        User_ID = user_id;
                                    }

                                    if (snapshot.hasChild("PostedByProfilePic")) {
                                        String postedByProfilePic = snapshot.child("PostedByProfilePic").getValue().toString();
                                        PostedByProfilePic = postedByProfilePic;
                                    }

                                    if (!BlogListID.contains(BlogListIDString)) {
                                        BlogListID.add(BlogListIDString);
                                        Blog model = new Blog("", PhotoThumb, ImageThumb,
                                                "", BlogTitle, Location, TimeTaken,
                                                UserName, User_ID,
                                                WeatherDetails,
                                                PostedByProfilePic,
                                                OriginalImageName);
                                        BlogList.add(model);
                                    }
                                }
                                else
                                {
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }


                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return BlogList;

    }
}
