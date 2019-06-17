package com.integrals.inlens.ServiceImplementation.Includes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.integrals.inlens.Helper.CurrentDatabase;
import com.integrals.inlens.Helper.UploadDatabaseHelper;
import com.integrals.inlens.ServiceImplementation.Notification.NotificationHelper;
import java.io.File;
import java.util.ArrayList;

public class UploadServiceHelper {
    private AsyncTask          uploadAsyncTask;
    private ArrayList<String>  stringArrayList;
    private String             uploadUrl;
    private NotificationHelper notificationHelper;
    private int                uploadID;
    private FirebaseAuth       firebaseAuth;
    private FirebaseUser       firebaseUser;
    private StorageReference   postStorageReference;
    private Context            context;
    private BitmapCompressionHelper bitmapCompressionHelper;
    private Uri                     uploadImageUri;
    private Uri                     thumbImageUri;
    private DatabaseReference       postDatabaseReference;
    private DatabaseReference       inUserReference;
    private String                  communityID;
    private Uri                     downloadThumbUrl;
    private Uri                     downloadUrl;
    private String                  titleValue;
    private String                  timeTaken;
    private File                    thumbFile;
    private File                    imageFile;
    private UploadDatabaseHelper    uploadDatabaseHelper;
    private CurrentDatabase         currentDatabase;

    public UploadServiceHelper(Context context,
                               ArrayList<String>stringArrayList,
                               String titleValue,
                               String timeTaken,
                               UploadDatabaseHelper uploadDatabaseHelper,
                               CurrentDatabase currentDatabase,int uploadID,String communityID)
    {

        this.stringArrayList=stringArrayList;
        uploadUrl=stringArrayList.get(0);
        notificationHelper=new NotificationHelper(context);

        bitmapCompressionHelper=new BitmapCompressionHelper(
                new File(uploadUrl),
                null,
                context,
                null,
                null

        );
        this.uploadDatabaseHelper=uploadDatabaseHelper;
        this.context=context;
        this.downloadThumbUrl=null;
        this.titleValue=titleValue;
        this.timeTaken=timeTaken;
        this.currentDatabase=currentDatabase;
        this.uploadID=uploadID;
        this.communityID=communityID;
    }



    @SuppressLint("StaticFieldLeak")
    public void initiateUploadOperation()
    {

        uploadAsyncTask=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {


                uploadDatabaseHelper.UpdateUploadStatus(uploadID,"UPLOADING");
                bitmapCompressionHelper.compressUploadFile();
                uploadImageUri=bitmapCompressionHelper.getResultUri();
                bitmapCompressionHelper.compressThumbImageFile();
                thumbImageUri=bitmapCompressionHelper.getResultUri();
                thumbFile=new File(thumbImageUri.getPath());
                imageFile=new File(uploadImageUri.getPath());



                final StorageReference FilePath = postStorageReference.child("CommunityPosts").child(uploadImageUri.getLastPathSegment());
                final StorageReference ThumbNailImage = postStorageReference.child("OriginalImage_thumb").child(uploadImageUri.getLastPathSegment() + System.currentTimeMillis());

                ThumbNailImage.putFile(thumbImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                downloadThumbUrl = taskSnapshot.getDownloadUrl();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //OriginalFilePath.delete();
                        ThumbNailImage.delete();
                        cancelUploadOperation();


                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            FilePath.putFile(uploadImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    downloadUrl = taskSnapshot.getDownloadUrl();
                                    }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {


                                            final DatabaseReference NewPost = postDatabaseReference.push();
                                            inUserReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                NewPost.child("BlogTitle").setValue(titleValue);
                                                NewPost.child("BlogDescription").setValue("NULLX");
                                                NewPost.child("Image").setValue((downloadUrl).toString());
                                                NewPost.child("ImageThumb").setValue((downloadThumbUrl).toString());
                                                NewPost.child("User_ID").setValue(firebaseUser.getUid());
                                                NewPost.child("OriginalImageName").setValue("NULLX");
                                                NewPost.child("Location").setValue("NULLX");
                                                NewPost.child("TimeTaken").setValue(timeTaken);
                                                NewPost.child("WeatherDetails").setValue("NULLX");
                                                NewPost.child("AudioCaption").setValue("NULLX");
                                                NewPost.child("PostedByProfilePic").setValue(dataSnapshot.child("Profile_picture").getValue());
                                                NewPost.child("UserName").setValue(dataSnapshot.child("Name").getValue());

                                                bitmapCompressionHelper.deleteFile(imageFile);
                                                bitmapCompressionHelper.deleteFile(thumbFile);




                                                try {
                                                    uploadDatabaseHelper.UpdateUploadStatus(uploadID, "UPLOADED");
                                                } catch (SQLiteReadOnlyDatabaseException e) {
                                                    e.printStackTrace();
                                                }
                                                int Value=uploadID+1;
                                                currentDatabase.ResetUploadTargetColumn(Value);


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // OriginalFilePath.delete();
                                                ThumbNailImage.delete();
                                                FilePath.delete();
                                                cancelUploadOperation();

                                            }
                                        });


                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //OriginalFilePath.delete();
                                    ThumbNailImage.delete();
                                    FilePath.delete();
                                    cancelUploadOperation();
                                }
                            });


                        }
                    }
                });


                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                               firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                postStorageReference= FirebaseStorage.getInstance().getReference();
                postDatabaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Communities")
                        .child(communityID)
                        .child("BlogPosts");
                inUserReference = FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Users")
                        .child(firebaseUser.getUid());

              }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }

            @Override
            protected void onCancelled(Object o) {
                super.onCancelled(o);

                bitmapCompressionHelper.deleteFile(imageFile);
                bitmapCompressionHelper.deleteFile(thumbFile);
                uploadDatabaseHelper.UpdateUploadStatus(uploadID, "NOT_UPLOADED");

                int Value = currentDatabase.GetUploadingTargetColumn();
                currentDatabase.ResetUploadTargetColumn((Value));
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);
             }
        };

    }

    public void proceedUploadOperation()
    {
        uploadAsyncTask.execute("");

    }

    public void cancelUploadOperation()
    {
        uploadAsyncTask.cancel(false);

    }
}
