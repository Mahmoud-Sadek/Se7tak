package com.sadek.se7tak.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;

import com.sadek.se7tak.R;
import com.sadek.se7tak.utils.Common;
import com.sadek.se7tak.utils.LocaleUtils;

import io.paperdb.Paper;

public class BaseActivity extends AppCompatActivity {

//    LanguageDialog languageDialog;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Paper.init(this);
        String lang = Paper.book().read(Common.language);
        if (lang != null)
            LocaleUtils.initialize(getBaseContext(), lang);
        else LocaleUtils.initialize(getBaseContext(), LocaleUtils.ENGLISH);

        //Check Os Ver For Set Status Bar
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

    }

//    private void channgeLanguage() {
//
//        languageDialog = new LanguageDialog(BaseActivity.this, new LanguageDialog.languageDialogAction() {
//            @Override
//            public void onGetCode(boolean langStatus) {
//                if (langStatus){
//
//                    Paper.book().write(Common.language, LocaleUtils.ARABIC);
//                    restartApp(BaseActivity.this, BaseActivity.this);
//                }else {
//
//                    Paper.book().write(Common.language, LocaleUtils.ENGLISH);
//                    restartApp(BaseActivity.this, BaseActivity.this);
//                }
//            }
//        });
//        languageDialog.show();
//        languageDialog.setCancelable(false);
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();

   /*     if (!isNetworkAvailable()) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.customView(R.layout.network_custom_dialog, false);
            builder.backgroundColor(getResources().getColor(R.color.transparent));
            builder.show();
            return;
        }
*/
    }

    @SuppressLint("PrivateResource")
    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("PrivateResource")
    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("PrivateResource")
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @SuppressLint("ResourceType")
    public FragmentTransaction getTransaction() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        return transaction;
    }


    public static void restartApp(Context context, Activity activity) {
        Intent settingIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(settingIntent);
        activity.finish();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    boolean doubleBackToExitPressedOnce = false;

   /* @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.head_container);
        if (f instanceof MainFragment)

        {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.press_back_again), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }*/


    //Animate List
    public static void animate(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

}


