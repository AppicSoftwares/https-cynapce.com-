package com.alcanzar.cynapse.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);
        //genertaeHashKey();

        getPackageHash();
        //TODO : calling the handler method for the delay in load
        int SPLASH_DISPLAY_LENGTH = 3000;
        gen();
//        enableLoc();
//        displayLocationSettingsRequest(SplashActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO : loading the start activity this is only for the first time
                if(!AppCustomPreferenceClass.readString(SplashActivity.this,AppCustomPreferenceClass.UserId,"").equals("")){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this,StartActivity.class));
                    //startActivity(new Intent(SplashActivity.this, FBLoginDemo.class));
                    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void getPackageHash() {
        try {

            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.alcanzar.cynapse",//give your package name here
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d("SplashScreen", "Hash  : " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));//Key hash is printing in Log
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("SplashScreen", e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d("SplashScreen", e.getMessage(), e);
        }
    }

    private void gen() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.alcanzar.cynapse",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

//    private void genertaeHashKey() {
//        Log.d("DEBUG", "Generating Hash Key");
//        try
//        {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.e("KeyHash:", "" + e);
//            Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//        } catch (NoSuchAlgorithmException e)
//
//        {
//            Log.e("KeyHash:", "" + e);
//        }
//    }


}
