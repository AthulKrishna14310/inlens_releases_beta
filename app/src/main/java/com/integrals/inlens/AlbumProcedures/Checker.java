package com.integrals.inlens.AlbumProcedures;

import android.content.Context;

import com.integrals.inlens.Helper.CurrentDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Checker {
    private Context context;
    private String AlbumExpiry;
    private String AlbumTime;

    public Checker(Context context) {
        this.context = context;
    }


    //if active returns a value less than or equal to zero else more than zero

    public int checkAlbumActive() {
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

}
