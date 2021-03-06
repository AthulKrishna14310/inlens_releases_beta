package com.integrals.inlens.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import info.androidhive.barcode.BarcodeReader;

import com.integrals.inlens.AlbumProcedures.AlbumStartingServices;
import com.integrals.inlens.Helper.CurrentDatabase;

import com.integrals.inlens.Helper.UploadDatabaseHelper;
import com.integrals.inlens.R;


public class QRCodeReader extends AppCompatActivity
        implements BarcodeReader.BarcodeReaderListener {

    private TextView NewCommunityStatus;
    private BarcodeReader barcodeReader;
    private DatabaseReference CommunityPhotographer;
    private FirebaseAuth CommunityPhotographerAuthentication;
    private String UserID;
    private DatabaseReference UserData;
    private String CommunityID = "1122333311101";
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2,databaseReference3,databaseReference4;
    private Activity activity;
    private AlbumStartingServices albumStartingServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);
        getSupportActionBar().hide();
        activity=this;

        albumStartingServices=new AlbumStartingServices(getApplicationContext());
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        NewCommunityStatus = (TextView) findViewById(R.id.NewCommunityStatus);
        CommunityPhotographerAuthentication = FirebaseAuth.getInstance();
        UserData = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(CommunityPhotographerAuthentication.getCurrentUser()
                        .getUid());
        UserID = CommunityPhotographerAuthentication.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();


    }









    @Override
    public void onScanned(final Barcode barcode) {
        barcodeReader.pauseScanning();
        // play beep sound
        barcodeReader.playBeep();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommunityID=barcode.displayValue;
                AddPhotographerToCommunity();

            }
        });
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }

    private void AddPhotographerToCommunity() {

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
        databaseReference4=databaseReference2.child("AlbumExpiry");
        databaseReference3=databaseReference2.child("ActiveIndex");
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue().equals("T")){

                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setCancelable(true);
                        builder.setTitle("Join");
                        builder.setMessage("You have  scanned a Cloud-Album. Proceed joining it ?");
                        builder.setPositiveButton(" YES ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                {


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

                                    databaseReference = databaseReference.child("Users").child(UserID).child("Communities");
                                    final DatabaseReference AddingAlbumToReference = databaseReference.child(CommunityID);
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
                                                    String AlbumExpiry=dataSnapshot.getValue().toString();
                                                    albumStartingServices.deleteDatabases();
                                                    albumStartingServices.createDatabases(CommunityID,AlbumExpiry);
                                                    StartServices();

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                            StartServices();
                                            }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(), "Sorry network error...please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });




                                }
                                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
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
                                finish();
                            }
                        });
                        builder.create().show();


                }else{
                    Toast.makeText(getApplicationContext(),
                            "Album time expired. You can't participate in this Cloud-Album.",
                            Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}



