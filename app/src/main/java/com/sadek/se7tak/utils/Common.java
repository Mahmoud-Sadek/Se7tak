package com.sadek.se7tak.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.sadek.se7tak.R;
import com.sadek.se7tak.firebase.FireDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Common {
    public static final String FIREBASE_DOCTOR = "FIREBASE_DOCTORS";
    public static final String FIREBASE_PATIENT = "FIREBASE_PATIENT";
    public static final String FIREBASE_SPECIALTY = "FIREBASE_SPECIALTY";
    public static final String FIREBASE_ABOUT_DOCTOR = "aboutDoctor";
    public static final String FIREBASE_DEGREE_DOCTOR = "degreeDoctor";
    public static final String FIREBASE_CLINIC_DOCTOR = "clinicDoctor";
    public static final String FIREBASE_CLINIC_PHOTO = "imges";
    public static final String FIREBASE_PATIENTS = "patients";
    public static final String FIREBASE_APPOINTMENT = "appointments";
    public static final String ORDER_DATE = "ORDER_DATE";
    public static final String ORDER_TIME = "ORDER_TIME";
    public static final String FIREBASE_DOCTOR_ORDER = "FIREBASE_DOCTOR_ORDER";
    public static final String FIREBASE_FAVORITES = "FIREBASE_FAVORITES";
    public static final String FIREBASE_DOCTOR_RATES = "FIREBASE_DOCTOR_RATES";
    public static final String SCREEN_NAME = "SCREEN_NAME";
    public static String language="language";
    public static final String lat = "lat";
    public static final String lng = "lng";
    public static final String ORDER_STATUS_PENDING = "0";
    public static final String ORDER_STATUS_ACCEPTED = "1";
    public static final String ORDER_STATUS_COMPLETED = "2";
    public static final String ORDER_STATUS_REJECTED = "3";
    public static final String ORDER_STATUS_CANCELED = "4";



    public static void keyHash(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    "com.sadek.se7tak",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static void showDeleteAlert(Context mContext, FireDatabase.ResultCallback callback){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle(R.string.delete_appointment);
        // Setting Dialog Message
        alertDialog.setMessage(R.string.are_you_sure_delete);
        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                callback.onCallback(true);
                dialog.cancel();
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callback.onCallback(false);
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

}

/*public static final String FIREBASE_DOCTOR = "FIREBASE_DOCTOR";
    public static final String FIREBASE_PATIENT = "FIREBASE_PATIENT";
    public static final String FIREBASE_SPECIALTY = "FIREBASE_SPECIALTY";
    public static final String FIREBASE_ABOUT_DOCTOR = "aboutDoctor";
    public static final String FIREBASE_DEGREE_DOCTOR = "degreeDoctor";
    public static final String FIREBASE_CLINIC_DOCTOR = "clinicDoctor";
    public static final String FIREBASE_CLINIC_PHOTO = "CLINIC_PHOTO";
    public static final String FIREBASE_PATIENTS = "patients";
    public static final String FIREBASE_APPOINTMENT = "appointments";
    public static String language="language";*/
