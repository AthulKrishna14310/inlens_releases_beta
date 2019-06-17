package com.integrals.inlens;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cocosw.bottomsheet.BottomSheet;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.integrals.inlens.AlbumProcedures.AlbumStartingServices;
import com.integrals.inlens.AlbumProcedures.Checker;
import com.integrals.inlens.AlbumProcedures.QuitCloudAlbumProcess;
import com.integrals.inlens.Helper.NotificationHelper;
import com.integrals.inlens.Helper.UploadDatabaseHelper;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vistrav.ask.Ask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import com.integrals.inlens.Activities.CloudAlbum;
import com.integrals.inlens.Activities.CreateCloudAlbum;
import com.integrals.inlens.Activities.IntroActivity;
import com.integrals.inlens.Activities.IssueActivity;
import com.integrals.inlens.Activities.LoginActivity;
import com.integrals.inlens.Activities.QRCodeReader;
import com.integrals.inlens.Activities.SharedImageActivity;
import com.integrals.inlens.Activities.WorkingIntroActivity;
import com.integrals.inlens.Helper.CurrentDatabase;

import com.integrals.inlens.Models.AlbumModel;
import com.integrals.inlens.ViewHolder.AlbumViewHolder;


public class MainActivity extends AppCompatActivity {

    private RecyclerView MemoryRecyclerView;
    private DatabaseReference InDatabaseReference;

    private String CurrentUser;
    private FirebaseAuth InAuthentication;
    private FirebaseUser firebaseUser;
    private DatabaseReference participantDatabaseReference;
    private ProgressBar MainLoadingProgressBar;

    private DatabaseReference ProgressRef;

    private String PostKeyForEdit;
    private Activity activity;
    private Dialog ProfileDialog;
    private static final int GALLERY_PICK = 1;
    private StorageReference mStorageRef;
    private ProgressBar progressBar;
    private CircleImageView UserImage;
    private ImageButton ChangeuserImage, CloseProfileDialog;
    private TextView ProfileuserName, ProfileUserEmail;

    private ProgressBar MainBottomSheetAlbumCoverEditprogressBar;
    private CircleImageView MainBottomSheetAlbumCoverEditUserImage;
    private ImageButton MainBottomSheetAlbumCoverEditChangeuserImage;
    private TextView MainBottomSheetAlbumCoverEditDialogHeader;
    private static final int COVER_GALLERY_PICK = 78;

    // Static boolean for cover and profile
    // do not delete
    private static boolean COVER_CHANGE = false, PROFILE_CHANGE = false;

    //For All ParticipantsBottomSheet
    private Dialog  QRCodeDialog;
    private RecyclerView MainBottomSheetParticpantsBottomSheetDialogRecyclerView;

    //For snackbar about Connectivity Info;
    private CoordinatorLayout RootForMainActivity;

    //For Searching
    private static boolean SEARCH_IN_PROGRESS = false;
    private Menu MainMenu;
    private MainSearchAdapter MainAdapterForSearch;
    private List<AlbumModel> SearchedAlbums = new ArrayList<>();
    private List<String> AlbumKeys = new ArrayList<>();
    private Boolean QRCodeVisible = false;
    private int INTID = 3939;
    private SharedPreferences AlbumClickDetails;
    private FloatingActionButton MainFab, CreateAlbumFab, ScanQrFab;
    private Animation FabOpen, FabClose, FabRotateForward, FabRotateBackward, AlbumCardOpen, AlbumCardClose;
    private boolean isOpen = false;
    private ImageButton MainDimBackground;

    //for details Dialog
    private TextView MainBottomSheetAlbumTitle,
            MainBottomSheetAlbumDesc, MainBottomSheetAlbumOwner,
            MainBottomSheetAlbumType, MainBottomSheetAlbumStartTime,
            MainBottomSheetAlbumEndTime, MainBottomSheetAlbumPostCount, MainBottomSheetAlbumMemberCount;
    private int PostCount, MemberCount;

    private TextView NoAlbumTextView;

    private Boolean SHOW_TOUR;
    private NotificationManager ImageNotyManager;
    private NotificationCompat.Builder ImageNotyBuilder;
    private NotificationHelper ImageNotyHelper;


    private ImageButton MainMenuButton , MainSearchButton , MainBackButton;
    private EditText MainSearchEdittext;
    private RelativeLayout MainActionbar , MainSearchView;
    private BottomSheetBehavior MainCloudAlbumInfoBottomSheetBehavior;
    private View MainCloudInfoBottomSheetView;
    String MemberName="";
    String MemberImage="";
    private AlbumStartingServices albumStartingServices;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //User Authentication
        InAuthentication = FirebaseAuth.getInstance();
        ProgressRef = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseUser = InAuthentication.getCurrentUser();
        albumStartingServices=new AlbumStartingServices(getApplicationContext());
        try {
            if (firebaseUser == null) {
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                finish();
            } else {
                if (!firebaseUser.isEmailVerified()) {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();

                }
                else {
                    CurrentUser = firebaseUser.getUid();
                    QRCodeInit();
                    PermissionsInit();
                    FabAnimationAndButtonsInit();
                    ProfileDialogInit();
                    AlbumCoverEditDialogInit();
                    ParticipantsBottomSheetDialogInit();
                    DetailsDialogInit();
                    if(SHOW_TOUR)
                    {
                        //Only check if userimage is uploaded once
                        CheckIfUserImageExist(CurrentUser);

                    }
                    ShowAllAlbums();
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ImageNotyHelper=new NotificationHelper(getBaseContext());

        NoAlbumTextView = findViewById(R.id.nocloudalbumtextview);
        MainDimBackground = findViewById(R.id.main_dim_background);
        MainDimBackground.setVisibility(View.GONE);
        MainMenuButton = findViewById(R.id.mainactivity_actionbar_menubutton);
        MainSearchButton = findViewById(R.id.mainactivity_actionbar_searchbutton);
        MainActionbar = findViewById(R.id.mainactivity_actionbar_relativelayout);
        MainSearchView = findViewById(R.id.mainactivity_searchview_relativelayout);
        MainBackButton = findViewById(R.id.mainactivity_searchview_backbutton);
        MainSearchEdittext = findViewById(R.id.mainactivity_searchview_edittext);

        MainCloudInfoBottomSheetView = findViewById(R.id.main_bottomsheetview);
        MainCloudAlbumInfoBottomSheetBehavior = BottomSheetBehavior.from(MainCloudInfoBottomSheetView);

        SHOW_TOUR = getIntent().getBooleanExtra("ShowTour",false);
        QRCodeVisible = getIntent().getBooleanExtra("QRCodeVisible", false);

        if (QRCodeVisible) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    QRCodeDialog.show();
                }
            }, 600);
        }

        activity = this;

        // to handle album clicks

        AlbumClickDetails = getSharedPreferences("LastClickedAlbum", MODE_PRIVATE);
        //Snackbar
        RootForMainActivity = findViewById(R.id.root_for_main_activity);



        participantDatabaseReference = FirebaseDatabase.getInstance().getReference();
        MemoryRecyclerView = (RecyclerView) findViewById(R.id.CloudAlbumRecyclerView);
        MemoryRecyclerView.setHasFixedSize(true);
        MemoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MainLoadingProgressBar = findViewById(R.id.mainloadingpbar);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ProgressRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild("Communities")) {
                        MainLoadingProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        DecryptDeepLink();

        if (SHOW_TOUR)
        {
            ShowAllTapTargets();
        }



        MainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsConnectedToNet())
                {
                    new BottomSheet.Builder(MainActivity.this).title(" Options").sheet(R.menu.main_menu).listener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case R.id.upload_activity:
                                    startActivity(
                                            new Intent(MainActivity.this,
                                                    com.integrals.inlens.ServiceImplementation.InLensGallery.MainActivity.class));

                                    break;
                                case R.id.profile_pic:
                                    DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    DbRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.hasChild("Name")) {
                                                String dbname = dataSnapshot.child("Name").getValue().toString();
                                                ProfileuserName.setText(dbname);

                                            } else {
                                                ProfileuserName.setText("-NA-");
                                            }
                                            if (dataSnapshot.hasChild("Profile_picture")) {
                                                String image = dataSnapshot.child("Profile_picture").getValue().toString();
                                                if (image.equals("default")) {
                                                    Toast.makeText(getApplicationContext(),"No profile picture detected.",Toast.LENGTH_SHORT).show();
                                                    Glide.with(MainActivity.this).load(R.drawable.ic_account_200dp).into(UserImage);
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                                else if (!TextUtils.isEmpty(image) && !image.equals("default")) {
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    Picasso.get().load(image).into(UserImage, new Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                            progressBar.setVisibility(View.GONE);

                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            Toast.makeText(getApplicationContext(),"Image loading failed.",Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);

                                                        }
                                                    });


                                                }  else {
                                                    Toast.makeText(getApplicationContext(),"Loading failed.",Toast.LENGTH_SHORT).show();
                                                    Glide.with(MainActivity.this).load(R.drawable.ic_account_200dp).into(UserImage);
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            } else {
                                                Glide.with(MainActivity.this).load(R.drawable.ic_account_200dp).into(UserImage);
                                            }
                                            if (dataSnapshot.hasChild("Email")) {

                                                String dbemail = dataSnapshot.child("Email").getValue().toString();
                                                ProfileUserEmail.setText(String.format("Email : %s", dbemail));
                                            } else {
                                                ProfileUserEmail.setText("Email : -NA-");
                                            }

                                            ProfileDialog.show();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    break;
                                case R.id.working_tour:
                                {
                                    startActivity(new Intent(MainActivity.this, WorkingIntroActivity.class).putExtra("ShowTour","no"));
                                    overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
                                    finish();
                                    break;
                                }
                                case R.id.quit_cloud_album:
                                    quitCloudAlbum(0);
                                    break;


                                case R.id.restart_service: {
                                   AlbumStartingServices albumStartingServices
                                           =new AlbumStartingServices(getApplicationContext());
                                   albumStartingServices.initiateUploadService();
                                }
                                break;
                                case R.id.create_issues: {
                                    startActivity(new Intent(MainActivity.this, IssueActivity.class));
                                    overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                                    finish();
                                }
                                break;
                                case R.id.invite:
                                    final Intent SharingIntent = new Intent(Intent.ACTION_SEND);
                                    SharingIntent.setType("text/plain");
                                    SharingIntent.putExtra(Intent.EXTRA_TEXT,"InLens \n\n"+"Store all memories with unlimited storage and without quality compromise. Haven't got inLens? Get it now."+"\nhttps://play.google.com/store/apps/details?id=com.integrals.inlens");
                                    startActivity(SharingIntent);
                            }
                        }
                    }).show();
                }
                else
                {
                    Snackbar.make(RootForMainActivity, "Unable to connect to internet. Try again.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        MainSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SEARCH_IN_PROGRESS = true;

                MainActionbar.clearAnimation();
                MainActionbar.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_out));
                MainActionbar.getAnimation().start();
                MainActionbar.setVisibility(View.GONE);

                MainSearchView.clearAnimation();
                MainSearchView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_in));
                MainSearchView.getAnimation().start();
                MainSearchView.setVisibility(View.VISIBLE);

                MainSearchEdittext.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                MainSearchEdittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        if (!TextUtils.isEmpty(editable.toString())) {
                            MemoryRecyclerView.setVisibility(View.VISIBLE);
                            NoAlbumTextView.setVisibility(View.GONE);
                            ShowSearchResults(editable.toString());
                        } else {
                            SearchedAlbums.clear();
                            MemoryRecyclerView.removeAllViews();
                            ShowAllAlbums();
                        }

                    }
                });

            }
        });


        MainBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SEARCH_IN_PROGRESS = false;
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                MainSearchEdittext.setText("");

                MainSearchView.clearAnimation();
                MainSearchView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_out));
                MainSearchView.getAnimation().start();
                MainSearchView.setVisibility(View.GONE);

                MainActionbar.clearAnimation();
                MainActionbar.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_in));
                MainActionbar.getAnimation().start();
                MainActionbar.setVisibility(View.VISIBLE);
                ShowAllAlbums();

            }
        });

        MainDimBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOpen)
                {
                    CloseFabs();
                    isOpen = false;
                }
                if(MainCloudAlbumInfoBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                MainCloudAlbumInfoBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                {
                    MainCloudAlbumInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

            }
        });


        MainCloudAlbumInfoBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
               if(newState == BottomSheetBehavior.STATE_HIDDEN)
               {
                   MainDimBackground.setVisibility(View.GONE);
               }
               else
               {
                   MainDimBackground.setVisibility(View.VISIBLE);

               }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                MainDimBackground.setVisibility(View.VISIBLE);

            }
        });


    }

    private void CheckIfUserImageExist(String currentUser) {

        ProfileUserEmail = ProfileDialog.findViewById(R.id.custom_profile_dialog_useremail);
        ProfileUserEmail.setText("No Profile Image detected. Let your friends identify you.");

        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("Profile_picture"))
                {
                    String Image = dataSnapshot.child("Profile_picture").getValue().toString();
                    if(Image.equals("default"))
                    {
                        ProfileDialog.show();
                    }
                }
                else
                {
                    ProfileDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ShowAllTapTargets() {
        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.main_fab_btn), "Adding New Albums", "Click here to create new album or join one.")
                        .tintTarget(false)
                        .cancelable(false)
                        .outerCircleColor(R.color.colorPrimaryDark)
                        .textColor(R.color.white),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        AnimateFab();
                        TapTargetView.showFor(MainActivity.this,
                                TapTarget.forView(findViewById(R.id.main_create_album_fab_btn), "Create New Albums", "Click here to create new album.")
                                        .tintTarget(false)
                                        .cancelable(false)
                                        .outerCircleColor(R.color.colorPrimaryDark)
                                        .textColor(R.color.white),
                                new TapTargetView.Listener() {
                                    @Override
                                    public void onTargetClick(TapTargetView view) {
                                        super.onTargetClick(view);

                                        TapTargetView.showFor(MainActivity.this,
                                                TapTarget.forView(findViewById(R.id.main_scan_qr_fab_btn), "Join Albums", "Click here to join a new album.")
                                                        .tintTarget(false)
                                                        .cancelable(false)
                                                        .outerCircleColor(R.color.colorPrimaryDark)
                                                        .textColor(R.color.white),
                                                new TapTargetView.Listener() {
                                                    @Override
                                                    public void onTargetClick(TapTargetView view) {
                                                        super.onTargetClick(view);
                                                        AnimateFab();

                                                        TapTargetView.showFor(MainActivity.this,
                                                                TapTarget.forView(findViewById(R.id.mainactivity_actionbar_searchbutton), "Search", "Click here perform a search on albums.")
                                                                        .tintTarget(false)
                                                                        .cancelable(false)
                                                                        .outerCircleColor(R.color.colorPrimaryDark)
                                                                        .textColor(R.color.white)
                                                                        .targetCircleColor(R.color.black),
                                                                new TapTargetView.Listener() {
                                                                    @Override
                                                                    public void onTargetClick(TapTargetView view) {
                                                                        super.onTargetClick(view);

                                                                        TapTargetView.showFor(MainActivity.this,
                                                                                TapTarget.forView(findViewById(R.id.mainactivity_actionbar_menubutton), "More Options", "Click here get more options.")
                                                                                        .tintTarget(false)
                                                                                        .cancelable(false)
                                                                                        .targetCircleColor(R.color.black)
                                                                                        .outerCircleColor(R.color.colorPrimaryDark)
                                                                                        .textColor(R.color.white),
                                                                                new TapTargetView.Listener() {
                                                                                    @Override
                                                                                    public void onTargetClick(TapTargetView view) {
                                                                                        super.onTargetClick(view);



                                                                                    }
                                                                                });

                                                                    }
                                                                });
                                                    }
                                                });

                                    }
                                });

                    }
                });


    }


    private void DecryptDeepLink() {

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {

                Uri DeepLink;
                if (pendingDynamicLinkData != null) {
                    DeepLink = pendingDynamicLinkData.getLink();
                    if (DeepLink != null) {


                        if (DeepLink.toString().contains("comid=")) {

                            String UrlOrDComId = (DeepLink.toString().substring(DeepLink.toString().length() - 27)).substring(0, 26);

                            SharedPreferences sharedPreferences2 = getSharedPreferences("InCommunity.pref", MODE_PRIVATE);
                            if (sharedPreferences2.getBoolean("UsingCommunity::", false) == true) {

                                Toast.makeText(getApplicationContext(), "Sorry,You can't participate in a new Cloud-Album before you quit the current one.", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Join " + UrlOrDComId.substring(6, 26), Toast.LENGTH_SHORT).show();
                                AddToCloud(UrlOrDComId.substring(6, 26), progressBar);
                            }


                        } else if (DeepLink.toString().contains("imagelink") && DeepLink.toString().contains("linkimage")) {

                            String first = DeepLink.toString().replace("https://integrals.inlens.in/", "");
                            String second = first.replace("imagelink", "https://firebasestorage.googleapis.com/v0/b/inlens-f0ce2.appspot.com/o/OriginalImage_thumb%2F");
                            String third = second.replace("linkimage", "media&token=");
                            String ImageUrl = third.substring(0, third.length() - 1);

                            startActivity(new Intent(MainActivity.this, SharedImageActivity.class).putExtra("url", ImageUrl));

                        }
                    }
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(), "Invite Link Failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void DetailsDialogInit() {

        MainBottomSheetAlbumTitle = findViewById(R.id.main_bottomsheet_details_dialog_albumtitle);
        MainBottomSheetAlbumDesc = findViewById(R.id.main_bottomsheet_details_dialog_albumdesc);
        MainBottomSheetAlbumOwner = findViewById(R.id.main_bottomsheet_details_dialog_albumowner);
        MainBottomSheetAlbumType = findViewById(R.id.main_bottomsheet_details_dialog_albumtype);

        MainBottomSheetAlbumStartTime = findViewById(R.id.main_bottomsheet_details_dialog_albumstarttime);
        MainBottomSheetAlbumEndTime = findViewById(R.id.main_bottomsheet_details_dialog_albumendtime);

        MainBottomSheetAlbumPostCount = findViewById(R.id.main_bottomsheet_details_dialog_albumpostcount);
        MainBottomSheetAlbumMemberCount = findViewById(R.id.main_bottomsheet_details_dialog_albumparticipantscount);

    }

    private void ParticipantsBottomSheetDialogInit() {

        MainBottomSheetParticpantsBottomSheetDialogRecyclerView = findViewById(R.id.main_bottomsheet_particpants_bottomsheet_recyclerview);
        MainBottomSheetParticpantsBottomSheetDialogRecyclerView.setHasFixedSize(true);
        GridLayoutManager Gridmanager = new GridLayoutManager(MainActivity.this, 3);
        MainBottomSheetParticpantsBottomSheetDialogRecyclerView.setLayoutManager(Gridmanager);

    }

    private void AlbumCoverEditDialogInit() {

        MainBottomSheetAlbumCoverEditDialogHeader = findViewById(R.id.main_bottomsheet_custom_header_dialog_username);
        MainBottomSheetAlbumCoverEditprogressBar = findViewById(R.id.main_bottomsheet_custom_cover_dialog_progressbar);
        MainBottomSheetAlbumCoverEditUserImage = findViewById(R.id.main_bottomsheet_custom_cover_dialog_userprofilepic);
        MainBottomSheetAlbumCoverEditChangeuserImage = findViewById(R.id.main_bottomsheet_custom_cover_dialog_profilechangebtn);

        MainBottomSheetAlbumCoverEditChangeuserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (IsConnectedToNet()) {

                    COVER_CHANGE = true;
                    PROFILE_CHANGE = false;

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio((int) 390, 285)
                            .setFixAspectRatio(true)
                            .start(MainActivity.this);

                } else {
                    Snackbar.make(RootForMainActivity, "Unable to connect to internet. Try again.", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void ProfileDialogInit() {

        ProfileDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar);
        ProfileDialog.setCancelable(true);
        ProfileDialog.setCanceledOnTouchOutside(true);
        ProfileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ProfileDialog.setContentView(R.layout.custom_profile_dialog);
        ProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ProfileDialog.getWindow().getAttributes().windowAnimations = R.style.BottomUpSlideDialogAnimation;

        Window ProfileDialogwindow = ProfileDialog.getWindow();
        ProfileDialogwindow.setGravity(Gravity.BOTTOM);
        ProfileDialogwindow.setLayout(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT);
        ProfileDialogwindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ProfileDialogwindow.setDimAmount(0.50f);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        progressBar = ProfileDialog.findViewById(R.id.custom_profile_dialog_progressbar);
        UserImage = ProfileDialog.findViewById(R.id.custom_profile_dialog_userprofilepic);
        ChangeuserImage = ProfileDialog.findViewById(R.id.custom_profile_dialog_profilechangebtn);
        ProfileUserEmail = ProfileDialog.findViewById(R.id.custom_profile_dialog_useremail);
        ProfileuserName = ProfileDialog.findViewById(R.id.custom_profile_dialog_username);

        CloseProfileDialog = ProfileDialog.findViewById(R.id.custom_profile_dialog_closebtn);

        CloseProfileDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProfileDialog.dismiss();

            }
        });

        ChangeuserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (IsConnectedToNet()) {
                    COVER_CHANGE = false;
                    PROFILE_CHANGE = true;
                    GetStartedWithNewProfileImage();
                } else {
                    Snackbar.make(RootForMainActivity, "Unable to connect to internet. Try again.", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void FabAnimationAndButtonsInit() {

        AlbumCardOpen = AnimationUtils.loadAnimation(this, R.anim.album_card_open);
        AlbumCardClose = AnimationUtils.loadAnimation(this, R.anim.album_card_close);

        FabOpen = AnimationUtils.loadAnimation(this, R.anim.main_fab_open);
        FabClose = AnimationUtils.loadAnimation(this, R.anim.main_fab_close);
        FabRotateForward = AnimationUtils.loadAnimation(this, R.anim.main_fab_rotate_forward);
        FabRotateBackward = AnimationUtils.loadAnimation(this, R.anim.main_fab_rotate_backward);

        MainFab = findViewById(R.id.main_fab_btn);
        ScanQrFab = findViewById(R.id.main_scan_qr_fab_btn);
        CreateAlbumFab = findViewById(R.id.main_create_album_fab_btn);

        MainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimateFab();
            }
        });

        ScanQrFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimateFab();

                SharedPreferences sharedPreferences1 = getSharedPreferences("InCommunity.pref", MODE_PRIVATE);
                if (sharedPreferences1.getBoolean("UsingCommunity::", false) == true) {
                    Toast.makeText(getApplicationContext(), "Sorry,You can't scan a new Cloud-Album before you quit the current one.", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(MainActivity.this, QRCodeReader.class));

                }
            }
        });

        CreateAlbumFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimateFab();
                SharedPreferences sharedPreferences = getSharedPreferences("InCommunity.pref", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("UsingCommunity::", false)) {
                    Toast.makeText(getApplicationContext(), "Sorry.You can't create a new Cloud-Album before you quit the current one.", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(MainActivity.this, CreateCloudAlbum.class));
                    finish();
                }
            }
        });

    }

    private void CloseFabs()
    {
        if(MainDimBackground.isShown()){

            MainDimBackground.setVisibility(View.GONE);
            ScanQrFab.clearAnimation();
            ScanQrFab.setAnimation(FabClose);
            ScanQrFab.getAnimation().start();

            CreateAlbumFab.clearAnimation();
            CreateAlbumFab.setAnimation(FabClose);
            CreateAlbumFab.getAnimation().start();

            CreateAlbumFab.setVisibility(View.INVISIBLE);
            ScanQrFab.setVisibility(View.INVISIBLE);

            MainFab.clearAnimation();
            MainFab.setAnimation(FabRotateBackward);
            MainFab.getAnimation().start();
        }
        isOpen = false;
    }

    private void AnimateFab() {

        if (isOpen) {

            MainDimBackground.setVisibility(View.GONE);
            ScanQrFab.clearAnimation();
            ScanQrFab.setAnimation(FabClose);
            ScanQrFab.getAnimation().start();

            CreateAlbumFab.clearAnimation();
            CreateAlbumFab.setAnimation(FabClose);
            CreateAlbumFab.getAnimation().start();


            CreateAlbumFab.setVisibility(View.INVISIBLE);
            ScanQrFab.setVisibility(View.INVISIBLE);

            MainFab.clearAnimation();
            MainFab.setAnimation(FabRotateBackward);
            MainFab.getAnimation().start();

            isOpen = false;
        } else {

            ScanQrFab.clearAnimation();
            ScanQrFab.setAnimation(FabOpen);
            ScanQrFab.getAnimation().start();

            CreateAlbumFab.clearAnimation();
            CreateAlbumFab.setAnimation(FabOpen);
            CreateAlbumFab.getAnimation().start();


            CreateAlbumFab.setVisibility(View.VISIBLE);
            ScanQrFab.setVisibility(View.VISIBLE);
            //MainScanQrTxtview.setVisibility(View.VISIBLE);
            //MainCreateAlbumTxtview.setVisibility(View.VISIBLE);

            MainFab.clearAnimation();
            MainFab.setAnimation(FabRotateForward);
            MainFab.getAnimation().start();
            isOpen = true;

            MainDimBackground.setVisibility(View.VISIBLE);

        }

    }

    private void PermissionsInit() {
        Ask.on(this)
                .id(INTID) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.INTERNET
                        , Manifest.permission.CAMERA
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.RECORD_AUDIO
                        , Manifest.permission.VIBRATE
                        , Manifest.permission.SYSTEM_ALERT_WINDOW
                )
                .go();
    }

    private void DisplayAllParticipantsAsBottomSheet(String postKeyForEdit,
                                                     DatabaseReference getParticipantDatabaseReference) {

        final Dialog BottomSheetUserDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar);
        BottomSheetUserDialog.setCancelable(true);
        BottomSheetUserDialog.setCanceledOnTouchOutside(true);
        BottomSheetUserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BottomSheetUserDialog.setContentView(R.layout.custom_profile_dialog);
        BottomSheetUserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        BottomSheetUserDialog.getWindow().getAttributes().windowAnimations = R.style.BottomUpSlideDialogAnimation;

        Window BottomSheetUserDialogWindow = BottomSheetUserDialog.getWindow();
        BottomSheetUserDialogWindow.setGravity(Gravity.BOTTOM);
        BottomSheetUserDialogWindow.setLayout(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT);
        BottomSheetUserDialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        BottomSheetUserDialogWindow.setDimAmount(0.75f);

        final ProgressBar bprogressBar = BottomSheetUserDialog.findViewById(R.id.custom_profile_dialog_progressbar);
        final CircleImageView UserImage = BottomSheetUserDialog.findViewById(R.id.custom_profile_dialog_userprofilepic);
        ImageButton ChangeuserImage = BottomSheetUserDialog.findViewById(R.id.custom_profile_dialog_profilechangebtn);
        ChangeuserImage.setVisibility(View.GONE);
        final TextView ProfileUserEmail = BottomSheetUserDialog.findViewById(R.id.custom_profile_dialog_useremail);
        final TextView ProfileuserName = BottomSheetUserDialog.findViewById(R.id.custom_profile_dialog_username);
        ImageButton CloseProfileDialog = BottomSheetUserDialog.findViewById(R.id.custom_profile_dialog_closebtn);

        CloseProfileDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetUserDialog.dismiss();
            }
        });

        final List<String> MemberImageList = new ArrayList<>();
        final List<String> MemberNamesList = new ArrayList<>();

        getParticipantDatabaseReference.child("Communities").child(postKeyForEdit).child("CommunityPhotographer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MemberImageList.clear();
                MemberNamesList.clear();
                MainBottomSheetParticpantsBottomSheetDialogRecyclerView.removeAllViews();

                for(DataSnapshot snapshot : dataSnapshot.getChildren() )
                {
                    if(snapshot.hasChild("Profile_picture"))
                    {
                        MemberImage = snapshot.child("Profile_picture").getValue().toString();
                    }
                    else
                    {
                        MemberImage = "default";
                    }
                    if(snapshot.hasChild("Name"))
                    {
                        MemberName = snapshot.child("Name").getValue().toString();
                    }
                    else
                    {
                        MemberName = "-NA-";
                    }

                    MemberImageList.add(MemberImage);
                    MemberNamesList.add(MemberName);
                }

                ParticipantsAdapter participantsAdapter = new ParticipantsAdapter(MemberImageList,MemberNamesList);
                MainBottomSheetParticpantsBottomSheetDialogRecyclerView.setAdapter(participantsAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        ShowAllAlbums();
        MainCloudAlbumInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void ShowAllAlbums() {
        MainLoadingProgressBar.setVisibility(View.VISIBLE);
        MemoryRecyclerView.setVisibility(View.GONE);

        InDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUser).child("Communities");
        InDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SearchedAlbums.clear();
                MemoryRecyclerView.removeAllViews();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String AlbumName = snapshot.child("AlbumTitle").getValue().toString();
                    String AlbumCoverImage="default",PostedByProfilePic="default" ,AlbumDescription="default",User_ID="default",UserName="defaulr";

                    final String AlbumKey = snapshot.getKey();
                    if(snapshot.hasChild("AlbumCoverImage"))
                    {
                        AlbumCoverImage = snapshot.child("AlbumCoverImage").getValue().toString();

                    }
                    if(snapshot.hasChild("PostedByProfilePic"))
                    {
                        PostedByProfilePic = snapshot.child("PostedByProfilePic").getValue().toString();

                    }
                    if(snapshot.hasChild("AlbumDescription"))
                    {
                        AlbumDescription = snapshot.child("AlbumDescription").getValue().toString();

                    }
                    if(snapshot.hasChild("User_ID"))
                    {
                        User_ID = snapshot.child("User_ID").getValue().toString();

                    }
                    if(snapshot.hasChild("UserName"))
                    {
                        UserName = snapshot.child("UserName").getValue().toString();

                    }
                    String DateandTime = "";
                    AlbumModel Album = new AlbumModel(AlbumCoverImage, AlbumDescription, AlbumName, PostedByProfilePic, DateandTime, UserName, User_ID);
                    SearchedAlbums.add(Album);
                    AlbumKeys.add(AlbumKey);

                }

                Collections.reverse(SearchedAlbums);
                Collections.reverse(AlbumKeys);

                if (AlbumKeys.size() == 0) {
                    NoAlbumTextView.setVisibility(View.VISIBLE);
                } else {
                    NoAlbumTextView.setVisibility(View.GONE);
                }


                MainAdapterForSearch = new MainSearchAdapter(getApplicationContext(), SearchedAlbums, AlbumKeys, FirebaseDatabase.getInstance().getReference().child("Communities"));
                MemoryRecyclerView.setAdapter(MainAdapterForSearch);
                MemoryRecyclerView.scrollToPosition(AlbumClickDetails.getInt("last_clicked_position", 0));
                MainLoadingProgressBar.setVisibility(View.GONE);
                MemoryRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ShowSearchResults(final String s) {

        MainLoadingProgressBar.setVisibility(View.VISIBLE);
        MemoryRecyclerView.setVisibility(View.GONE);

        InDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUser).child("Communities");
        InDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SearchedAlbums.clear();
                MemoryRecyclerView.removeAllViews();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String AlbumName = snapshot.child("AlbumTitle").getValue().toString();
                    if (AlbumName.toLowerCase().contains(s.toLowerCase())) {
                        final String AlbumKey = snapshot.getKey();
                        String AlbumCoverImage = snapshot.child("AlbumCoverImage").getValue().toString();
                        String PostedByProfilePic = snapshot.child("PostedByProfilePic").getValue().toString();
                        String AlbumDescription = snapshot.child("AlbumDescription").getValue().toString();
                        String DateandTime = "";
                        String User_ID = snapshot.child("User_ID").getValue().toString();
                        String UserName = snapshot.child("UserName").getValue().toString();
                        AlbumModel Album = new AlbumModel(AlbumCoverImage, AlbumDescription, AlbumName, PostedByProfilePic, DateandTime, UserName, User_ID);
                        SearchedAlbums.add(Album);
                        AlbumKeys.add(AlbumKey);
                    }
                }

                if (SearchedAlbums.size() == 0) {
                    NoAlbumTextView.setVisibility(View.VISIBLE);
                }

                Collections.reverse(SearchedAlbums);
                Collections.reverse(AlbumKeys);

                MainAdapterForSearch = new MainSearchAdapter(getApplicationContext(), SearchedAlbums, AlbumKeys, FirebaseDatabase.getInstance().getReference().child("Communities"));
                MemoryRecyclerView.setAdapter(MainAdapterForSearch);
                MainLoadingProgressBar.setVisibility(View.GONE);
                MemoryRecyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void quitCloudAlbum(int x) {
        Checker checker = new Checker(getApplicationContext());
        if (checker.isConnectedToNet()) {
            if (checker.checkIfInAlbum()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Quit Cloud-Album");
                builder.setMessage("Are you sure you want to quit the current community .");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                builder.setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        QuitCloudAlbumProcess quitCloudAlbumProcess = new QuitCloudAlbumProcess(
                                getApplicationContext(),
                                MainActivity.this

                        );
                        quitCloudAlbumProcess.execute();
                    }

                });
                builder.create().show();
            } else {
                  Toast.makeText(getApplicationContext(),
                          "Sorry you don't participate in any Cloud-Album to quit",
                          Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void AddToCloud(String substring, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        final DatabaseReference CommunityPhotographer;
        FirebaseAuth CommunityPhotographerAuthentication;
        final String UserID;
        final DatabaseReference UserData;
        final String CommunityID = substring;
        final DatabaseReference[] databaseReference = new DatabaseReference[1];
        final DatabaseReference databaseReference2, databaseReference3, databaseReference4;
        CommunityPhotographerAuthentication = FirebaseAuth.getInstance();
        UserData = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(CommunityPhotographerAuthentication.getCurrentUser()
                        .getUid());
        UserID = CommunityPhotographerAuthentication.getCurrentUser().getUid();
        databaseReference[0] = FirebaseDatabase.getInstance().getReference();
        CommunityPhotographer = FirebaseDatabase.getInstance()
                .getReference()
                .child("Communities")
                .child(CommunityID)
                .child("CommunityPhotographer");
        databaseReference2 = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Communities")
                .child(CommunityID);
        databaseReference4 = databaseReference2.child("AlbumExpiry");
        databaseReference3 = databaseReference2.child("ActiveIndex");
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().equals("T")) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setCancelable(true);
                    builder.setTitle("Join");
                    builder.setMessage("Join this Cloud-Album. Proceed joining it ?");
                    builder.setPositiveButton(" YES ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CurrentDatabase currentDatabase1 = new CurrentDatabase(getApplicationContext(), "", null, 1);
                            currentDatabase1.DeleteDatabase();
                            UploadDatabaseHelper uploadDatabaseHelper = new UploadDatabaseHelper(getApplicationContext(), "", null, 1);
                            uploadDatabaseHelper.DeleteDatabase();


                            final DatabaseReference NewPhotographer = CommunityPhotographer.push();
                            UserData.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    NewPhotographer.child("Photographer_UID").setValue(UserID);
                                    NewPhotographer.child("Name").setValue(dataSnapshot.child("Name").getValue());
                                    NewPhotographer.child("Profile_picture").setValue(dataSnapshot.child("Profile_picture").getValue());
                                    NewPhotographer.child("Email_ID").setValue(dataSnapshot.child("Email").getValue());

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            databaseReference[0] = databaseReference[0].child("Users").child(UserID).child("Communities");
                            final DatabaseReference AddingAlbumToReference = databaseReference[0].child(CommunityID);
                            databaseReference2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    AddingAlbumToReference.child("AlbumTitle").setValue(dataSnapshot.child("AlbumTitle").getValue().toString());
                                    AddingAlbumToReference.child("AlbumDescription").setValue(dataSnapshot.child("AlbumDescription").getValue().toString());
                                    AddingAlbumToReference.child("AlbumCoverImage").setValue(dataSnapshot.child("AlbumCoverImage").getValue().toString());
                                    AddingAlbumToReference.child("User_ID").setValue(dataSnapshot.child("User_ID").getValue().toString());
                                    AddingAlbumToReference.child("PostedByProfilePic").setValue(dataSnapshot.child("PostedByProfilePic").getValue().toString());
                                    AddingAlbumToReference.child("UserName").setValue(dataSnapshot.child("UserName").getValue().toString());
                                    AddingAlbumToReference.child("CreatedTimestamp").setValue(dataSnapshot.child("CreatedTimestamp").getValue().toString());
                                    AddingAlbumToReference.child("CommunityID").setValue(CommunityID);


                                    databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String ALBT = dataSnapshot.getValue().toString();
                                            albumStartingServices.deleteDatabases();
                                            albumStartingServices.createDatabases(CommunityID,ALBT);

                                            StartServices();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    setIntent(null);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Sorry network error...please try again", Toast.LENGTH_SHORT).show();
                                }
                            });


                            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                        }

                        private void StartServices() {
                            SharedPreferences sharedPreferences = getSharedPreferences("InCommunity.pref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("UsingCommunity::", true);
                            editor.commit();
                            SharedPreferences sharedPreferences1 = getSharedPreferences("Owner.pref", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.putBoolean("ThisOwner::", false);
                            editor1.commit();
                            albumStartingServices.initiateJobServices();
                            albumStartingServices.intiateNotificationAtStart();

                        }

                    });
                    builder.create().show();
                    progressBar.setVisibility(View.INVISIBLE);


                } else {
                    Toast.makeText(getApplicationContext(), "Album time expired. You can't participate in this Cloud-Album.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void GetStartedWithNewProfileImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COVER_GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(this);
            finish();
        } else if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);
            finish();

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && COVER_CHANGE && !PROFILE_CHANGE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri ImageUri = result.getUri();
                MainBottomSheetAlbumCoverEditUserImage.setImageURI(ImageUri);
                MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.VISIBLE);
                UploadCOverPhoto(ImageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && !COVER_CHANGE && PROFILE_CHANGE) {

            ProfileDialog.setCancelable(false);
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                progressBar.setVisibility(View.VISIBLE);

                Uri resultUri = result.getUri();
                try {
                    InputStream stream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    UserImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                File thumb_filePath = new File(resultUri.getPath());
                final String current_u_i_d = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(100)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                final StorageReference filepath = mStorageRef.child("profile_images").child(current_u_i_d + ".jpg");
                final StorageReference thumb_filepath = mStorageRef.child("profile_images").child("thumbs").child(current_u_i_d + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            com.google.firebase.storage.UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {


                                    String thumb_downloadurl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()) {


                                        Map update_Hashmap = new HashMap();
                                        update_Hashmap.put("Profile_picture", downloadUrl);
                                        update_Hashmap.put("thumb_image", thumb_downloadurl);

                                        FirebaseDatabase.getInstance().getReference().child("Users").child(current_u_i_d).updateChildren(update_Hashmap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            {
                                                                Toast.makeText(MainActivity.this, "SUCCESSFULLY UPLOADED", Toast.LENGTH_LONG).show();
                                                                progressBar.setVisibility(View.GONE);
                                                                ProfileDialog.setCancelable(true);
                                                                ProfileDialog.dismiss();
                                                                ProfileDialog.show();

                                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                    ImageNotyHelper.cancelUploadDataNotification();
                                                                }
                                                                else
                                                                {
                                                                    ImageNotyManager.cancel(503);
                                                                }

                                                            }
                                                        } else {
                                                            progressBar.setVisibility(View.GONE);
                                                            ProfileDialog.setCancelable(true);
                                                            Toast.makeText(MainActivity.this, "FAILED TO SAVE TO DATABASE.MAKE SURE YOUR INTERNET IS CONNECTED AND TRY AGAIN.", Toast.LENGTH_LONG).show();
                                                            ProfileDialog.dismiss();
                                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                ImageNotyHelper.cancelUploadDataNotification();
                                                            }
                                                            else
                                                            {
                                                                ImageNotyManager.cancel(503);
                                                            }
                                                        }

                                                    }
                                                });
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        ProfileDialog.setCancelable(true);
                                        Toast.makeText(MainActivity.this, "FAILED TO UPLOAD THUMBNAIL", Toast.LENGTH_LONG).show();
                                        ProfileDialog.dismiss();
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            ImageNotyHelper.cancelUploadDataNotification();
                                        }
                                        else
                                        {
                                            ImageNotyManager.cancel(503);
                                        }
                                    }

                                }

                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            ProfileDialog.setCancelable(true);
                            Toast.makeText(MainActivity.this, "FAILED TO UPLOAD", Toast.LENGTH_LONG).show();
                            ProfileDialog.dismiss();
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                ImageNotyHelper.cancelUploadDataNotification();
                            }
                            else
                            {
                                ImageNotyManager.cancel(503);
                            }
                        }
                    }


                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                ProfileDialog.setCancelable(true);
            }
        }

    }

    private void UploadCOverPhoto(Uri imageUri) {

        MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(PostKeyForEdit) && imageUri != null) {

            StorageReference
                    FilePath = mStorageRef
                    .child("CommunityCoverPhoto")
                    .child(imageUri.getLastPathSegment());

            FilePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {
                        final String downloadUrl = task.getResult().getDownloadUrl().toString();
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Communities")
                                .child(PostKeyForEdit)
                                .child("AlbumCoverImage")
                                .setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Communities")
                                            .child(PostKeyForEdit)
                                            .child("AlbumCoverImage")
                                            .setValue(downloadUrl);
                                    MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "Successfully changed the Cover-Photo.", Toast.LENGTH_LONG).show();

                                } else {
                                    MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "Unable to perform to change cover now.", Toast.LENGTH_LONG).show();

                                     }
                            }
                        });
                    } else {
                        MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Unable to perform to change cover now.", Toast.LENGTH_LONG).show();


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Unable to perform to change cover now.", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress =
                            (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                    Toast toast=Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);
                    toast.setText("Uploading your cover photo  " +
                            (int)progress+"%  " +
                            "completed.");
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                }
            });

        } else {
            MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Unable to perform to change cover now.", Toast.LENGTH_LONG).show();
        }


    }

    private boolean IsConnectedToNet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

    }

    @Override
    public void onBackPressed() {

        if (SEARCH_IN_PROGRESS) {

            SEARCH_IN_PROGRESS = false;
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                imm.hideSoftInputFromInputMethod(MainSearchEdittext.getWindowToken(), 0);
            }
            MainSearchEdittext.setText("");

            MainSearchView.clearAnimation();
            MainSearchView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_out));
            MainSearchView.getAnimation().start();
            MainSearchView.setVisibility(View.GONE);

            MainActionbar.clearAnimation();
            MainActionbar.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_in));
            MainActionbar.getAnimation().start();
            MainActionbar.setVisibility(View.VISIBLE);
            ShowAllAlbums();

        }
        else if(MainCloudAlbumInfoBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED || MainCloudAlbumInfoBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            MainCloudAlbumInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        else if (isOpen)
        {
            CloseFabs();
            isOpen = false;
        }
        else {
            super.onBackPressed();
        }
    }

    private class MainSearchAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

        Context context;
        List<AlbumModel> AlbumList;
        List<String> AlbumKeyIDs;
        DatabaseReference CommunityRef;

        public MainSearchAdapter(Context context, List<AlbumModel> albumList, List<String> albumKeyIDs, DatabaseReference communityRef) {
            this.context = context;
            AlbumList = albumList;
            AlbumKeyIDs = albumKeyIDs;
            CommunityRef = communityRef;
        }

        @NonNull
        @Override
        public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View SearchLayoutView = LayoutInflater.from(context).inflate(R.layout.cloud_album_card, parent, false);
            return new AlbumViewHolder(SearchLayoutView);
        }

        @Override
        public void onBindViewHolder(@NonNull final AlbumViewHolder holder, final int position) {

            holder.SetAlbumCover(getApplicationContext(), AlbumList.get(position).getAlbumCoverImage());
            holder.SetTitle(AlbumList.get(position).getAlbumTitle());
            holder.SetProfilePic(getApplicationContext(), AlbumList.get(position).getPostedByProfilePic());
            if (holder.AlbumContainer.isShown()) {


                holder.DetailsAlbumn.clearAnimation();
                holder.DetailsAlbumn.setAnimation(AlbumCardClose);
                holder.DetailsAlbumn.getAnimation().start();

                holder.DetailsAlbumn.setVisibility(View.INVISIBLE);

            } else {


                holder.DetailsAlbumn.clearAnimation();
                holder.DetailsAlbumn.setAnimation(AlbumCardOpen);
                holder.DetailsAlbumn.getAnimation().start();


                holder.DetailsAlbumn.setVisibility(View.VISIBLE);

            }


            holder.DetailsAlbumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    CloseFabs();

                    DisplayAllParticipantsAsBottomSheet(AlbumKeyIDs.get(position), FirebaseDatabase.getInstance().getReference());


                    MainBottomSheetAlbumTitle.setText(String.format("Album Title : %s", AlbumList.get(position).getAlbumTitle()));
                    MainBottomSheetAlbumDesc.setText(String.format("Album About : %s", AlbumList.get(position).getAlbumDescription()));
                    MainBottomSheetAlbumOwner.setText("Created By : " + AlbumList.get(position).getUserName());


                    CommunityRef.child(AlbumKeyIDs.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            PostCount = 0;
                            MemberCount = 0;

                            if (dataSnapshot.hasChild("CommunityPhotographer")) {
                                for (DataSnapshot PostSnapShot : dataSnapshot.child("CommunityPhotographer").getChildren()) {
                                    MemberCount++;
                                }
                            }

                            if (dataSnapshot.hasChild("Situations")) {
                                for (DataSnapshot PostSnapShot : dataSnapshot.child("Situations").getChildren()) {
                                    PostCount++;
                                }
                            }

                            MainBottomSheetAlbumMemberCount.setText(String.format("Total Members : %d", MemberCount));
                            MainBottomSheetAlbumPostCount.setText(String.format("Total Situations : %d", PostCount));

                            if (dataSnapshot.hasChild("ActiveIndex")) {
                                if (dataSnapshot.child("ActiveIndex").getValue().toString().equals("T")) {
                                    if (dataSnapshot.hasChild("AlbumExpiry")) {
                                        String DateEnd = "Event expires on : " + dataSnapshot.child("AlbumExpiry").getValue().toString() + " @ 11:59 PM";
                                        MainBottomSheetAlbumEndTime.setText(DateEnd);
                                    } else {
                                        String DateEnd = "Data not available";
                                        MainBottomSheetAlbumEndTime.setText(String.format("Album End Time : %s", DateEnd));
                                    }
                                } else {
                                    if (dataSnapshot.hasChild("AlbumExpiry")) {
                                        String DateEnd = "Event expired on :" + dataSnapshot.child("AlbumExpiry").getValue().toString() + " @ 11:59 PM";
                                        MainBottomSheetAlbumEndTime.setText(DateEnd);
                                    } else {
                                        String DateEnd = "Data not available";
                                        MainBottomSheetAlbumEndTime.setText(String.format("Album End Time : %s", DateEnd));
                                    }
                                }
                            } else {
                                String DateEnd = "Data not available";
                                MainBottomSheetAlbumEndTime.setText(String.format("Album End Time : %s", DateEnd));
                            }


                            if (dataSnapshot.hasChild("CreatedTimestamp")) {
                                String timestamp = dataSnapshot.child("CreatedTimestamp").getValue().toString();
                                long time = Long.parseLong(timestamp);
                                CharSequence Time = DateUtils.getRelativeDateTimeString(getApplicationContext(), time, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
                                String timesubstring = Time.toString().substring(Time.length() - 8);
                                Date date = new Date(time);
                                String dateformat = DateFormat.format("dd-MM-yyyy", date).toString();
                                String DateandTime = "Event started on : " + dateformat + " @ " + timesubstring;
                                MainBottomSheetAlbumStartTime.setText(DateandTime);
                            } else if (dataSnapshot.hasChild("Time")) {
                                String DateandTime = dataSnapshot.child("Time").getValue().toString();
                                MainBottomSheetAlbumStartTime.setText(DateandTime);
                            } else {
                                String DateEnd = "Data not available";
                                MainBottomSheetAlbumEndTime.setText(String.format("Album Start Time : %s", DateEnd));
                            }

                            if (dataSnapshot.hasChild("AlbumType")) {
                                String EventType = dataSnapshot.child("AlbumType").getValue().toString();
                                MainBottomSheetAlbumType.setText("Event Type : " + EventType);
                            } else {
                                String EventType = "Data not available";
                                MainBottomSheetAlbumType.setText("Event Type : " + EventType);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.VISIBLE);
                    MainBottomSheetAlbumCoverEditDialogHeader.setText(AlbumList.get(position).getAlbumTitle());

                    PostKeyForEdit = AlbumKeyIDs.get(position);
                    FirebaseDatabase.getInstance().getReference().child("Communities")
                            .child(PostKeyForEdit)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String Image = dataSnapshot.child("AlbumCoverImage").getValue().toString();
                                    if (Image.equals("default"))
                                    {

                                        Toast.makeText(getApplicationContext(),"No cover image detected.",Toast.LENGTH_SHORT).show();

                                        Glide.with(getApplicationContext())
                                                .load(R.drawable.image_avatar)
                                                .thumbnail(0.1f)
                                                .into(MainBottomSheetAlbumCoverEditUserImage);
                                        MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.GONE);

                                    }
                                    else if(!TextUtils.isEmpty(Image) && !Image.equals("default"))
                                    {

                                        MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.VISIBLE);
                                        Picasso.get().load(Image).into(MainBottomSheetAlbumCoverEditUserImage, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                                MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.GONE);

                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Toast.makeText(getApplicationContext(),"Image loading failed.",Toast.LENGTH_SHORT).show();
                                                MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.GONE);

                                            }
                                        });
                                    }

                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Loading failed.",Toast.LENGTH_SHORT).show();
                                        Glide.with(getApplicationContext())
                                                .load(R.drawable.image_avatar)
                                                .thumbnail(0.1f)
                                                .into(MainBottomSheetAlbumCoverEditUserImage);
                                        MainBottomSheetAlbumCoverEditprogressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"Loading failed.",Toast.LENGTH_SHORT).show();

                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                    MainCloudAlbumInfoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CloseFabs();

                    final String PostKey = AlbumKeyIDs.get(position);
                    if (!TextUtils.isEmpty(PostKey)) {
                        try {

                            SharedPreferences.Editor AlbumEditor = AlbumClickDetails.edit();
                            AlbumEditor.putInt("last_clicked_position", position);
                            AlbumEditor.apply();

                            startActivity(new Intent(MainActivity.this, CloudAlbum.class)
                                    .putExtra("AlbumName", AlbumList.get(position).getAlbumTitle())
                                    .putExtra("GlobalID::", PostKey)
                                    .putExtra("LocalID::", PostKey)
                                    .putExtra("UserID::", CurrentUser));

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });


            MainLoadingProgressBar.setVisibility(View.INVISIBLE);
            MemoryRecyclerView.setVisibility(View.VISIBLE);

        }

        @Override
        public int getItemCount() {
            return AlbumList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences AlbumClickDetails = getSharedPreferences("LastClickedAlbum", MODE_PRIVATE);
        SharedPreferences.Editor AlbumEditor = AlbumClickDetails.edit();
        AlbumEditor.putInt("last_clicked_position", 0);
        AlbumEditor.apply();
    }

    private void QRCodeInit() {

        QRCodeDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar);
        QRCodeDialog.setCancelable(false);
        QRCodeDialog.setCanceledOnTouchOutside(false);
        QRCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        QRCodeDialog.setContentView(R.layout.activity_qrcode_generator);
        QRCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        QRCodeDialog.getWindow().getAttributes().windowAnimations = R.style.BottomUpSlideDialogAnimation;

        Window QRCodewindow = QRCodeDialog.getWindow();
        QRCodewindow.setGravity(Gravity.BOTTOM);
        QRCodewindow.setLayout(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT);
        QRCodewindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        QRCodewindow.setDimAmount(0.75f);
        QRCodewindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        CurrentDatabase currentDatabase = new CurrentDatabase(getApplicationContext(), "", null, 1);
        final String QRCommunityID = currentDatabase.GetLiveCommunityID();
        currentDatabase.close();

        Button InviteLinkButton = QRCodeDialog.findViewById(R.id.InviteLinkButton);
        String QRPhotographerID = QRCommunityID;

        ImageButton QRCodeCloseBtn = QRCodeDialog.findViewById(R.id.QR_dialog_closebtn);
        QRCodeCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QRCodeDialog.dismiss();

            }
        });
        TextView textView = QRCodeDialog.findViewById(R.id.textViewAlbumQR);
        ImageView QRCodeImageView = QRCodeDialog.findViewById(R.id.QR_Display);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(QRPhotographerID, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QRCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            QRCodeImageView.setVisibility(View.INVISIBLE);
            textView.setText("You must be in an album to generate QR code");
            InviteLinkButton.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            QRCodeImageView.setVisibility(View.INVISIBLE);
            textView.setText("You must be in an album to generate QR code");
            InviteLinkButton.setVisibility(View.GONE);

        }
        InviteLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent SharingIntent = new Intent(Intent.ACTION_SEND);
                SharingIntent.setType("text/plain");
                String CommunityPostKey = QRCommunityID;
                SharingIntent.putExtra(Intent.EXTRA_TEXT, "InLens Cloud-Album Invite Link \n\n" + GenarateDeepLinkForInvite(CommunityPostKey));
                startActivity(SharingIntent);

            }
        });
    }


    private static String GenarateDeepLinkForInvite(String CommunityID) {
        return "https://inlens.page.link/?link=https://integrals.inlens.in/comid=" + CommunityID + "/&apn=com.integrals.inlens";
    }

    private class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder> {

        List<String> ImagesList;
        List<String> NamesList;

        public ParticipantsAdapter(List<String> imagesList, List<String> namesList) {
            ImagesList = imagesList;
            NamesList = namesList;
        }

        @NonNull
        @Override
        public ParticipantsAdapter.ParticipantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.member_card,parent,false);
            return new ParticipantsAdapter.ParticipantsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ParticipantsAdapter.ParticipantsViewHolder holder, int position) {

            holder.PName.setText(NamesList.get(position));

            RequestOptions rq = new RequestOptions().placeholder(R.drawable.image_avatar_background);
            Glide.with(getApplicationContext()).load(ImagesList.get(position)).apply(rq).into(holder.PImage);

        }

        @Override
        public int getItemCount() {
            return ImagesList.size();
        }

        public class ParticipantsViewHolder extends RecyclerView.ViewHolder {

            CircleImageView PImage;
            TextView PName;

            public ParticipantsViewHolder(View itemView) {
                super(itemView);

                PImage = itemView.findViewById(R.id.participants_profile_pic);
                PName = itemView.findViewById(R.id.participants_username);
            }
        }
    }
}


