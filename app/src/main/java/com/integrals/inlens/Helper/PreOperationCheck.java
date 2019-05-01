package com.integrals.inlens.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PreOperationCheck
{
    DatabaseReference databaseReference;
    String communityId;
    String status = "F";

    public PreOperationCheck()
    {

    }

    public PreOperationCheck(DatabaseReference databaseReference, String communityId) {

        this.databaseReference = databaseReference;
        this.communityId = communityId;
    }

    public boolean getAlbumStatus()
    {

        databaseReference.child("Communities").child(communityId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("ActiveIndex"))
                {
                    status = dataSnapshot.child("ActiveIndex").getValue().toString();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return status.equals("T");

    }

    public boolean checkInternetConnectivity(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

}
