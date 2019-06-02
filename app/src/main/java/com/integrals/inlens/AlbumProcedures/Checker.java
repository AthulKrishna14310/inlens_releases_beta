package com.integrals.inlens.AlbumProcedures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.integrals.inlens.Helper.CurrentDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Checker {

    private Context context;
    private String AlbumExpiry;
    private String AlbumTime;

    public Checker(Context context) {
        this.context = context;
    }



    public int checkAlbumActiveTime() {

        CurrentDatabase currentDatabase = new CurrentDatabase(context, "", null, 1);
        AlbumExpiry = currentDatabase.GetAlbumExpiry();
        currentDatabase.close();
        Calendar calendarW = Calendar.getInstance();
        AlbumTime = calendarW.get(Calendar.DAY_OF_MONTH) + "-" + (calendarW.get(Calendar.MONTH) + 1) + "-" + calendarW.get(Calendar.YEAR);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = null, d2 = null;
        try {

            d1 = dateFormat.parse(AlbumTime);
            d2 = dateFormat.parse(AlbumExpiry);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            return d1.compareTo(d2);

        } catch (NullPointerException e) {
            return 0;
        }




    }


    public boolean checkIfInAlbum(){
        boolean result=false;
        SharedPreferences sharedPreferences = context.getSharedPreferences("InCommunity.pref", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("UsingCommunity::", false)) {
            result = true;
        }

        return result;
       }


        public boolean isConnectedToNet() {

            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;


        }

}
