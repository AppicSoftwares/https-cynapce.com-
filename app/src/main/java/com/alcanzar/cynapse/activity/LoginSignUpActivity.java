package com.alcanzar.cynapse.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.fragments.LoginFragment;
import com.alcanzar.cynapse.fragments.SignUpFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

public class LoginSignUpActivity extends AppCompatActivity {

    public Button btnLogIn,btnSignUp;
    public Fragment currentFragment;
    public CallbackManager callbackManager;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //AppEventsLogger.activateApp(this);

        if(AccessToken.getCurrentAccessToken() == null) {
            Log.e("AccessToken","null");
        } else {
            Log.e("AccessToken",AccessToken.getCurrentAccessToken().getToken());
        }

        setContentView(R.layout.activity_login_sign_up);

        //TODO : initializing the views
        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        //TODO : first time setting the login fragment by default
        replaceFragment(new LoginFragment());
//        if (checkAndRequestPermissions()) {
//             carry on the normal flow, as the case of  permissions  granted.
//        }
        //TODO : calling the login fragment here
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LoginFragment());
                btnLogIn.setTextColor(Color.parseColor("#4B4B4B"));
                btnSignUp.setTextColor(Color.parseColor("#DEE0E4"));
            }
        });

        //TODO : calling the signUp fragment here
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SignUpFragment());
                btnLogIn.setTextColor(Color.parseColor("#DEE0E4"));
                btnSignUp.setTextColor(Color.parseColor("#4B4B4B"));
            }
        });


    }

    //TODO : this is used to replace the current fragment with the new one
    public void replaceFragment(Fragment fragment) {
    currentFragment = fragment;
    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).commit();/*commitAllowingStateLoss()*/;
    }
    //TODO : this is used to open the exit app alert dialog
    boolean doubleBackToExitPressedOnce = false;
    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        TextView text =  layout.findViewById(R.id.text);
        text.setText("Press again to exit");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        currentFragment.onActivityResult(requestCode, resultCode, data);
        Log.e("callbackManager","callbackManager11");

    }
//    private  boolean checkAndRequestPermissions() {
//        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.SEND_SMS);
//        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
//        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
//        }
//        if (readSMS != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
//        }
//        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this,
//                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
//                    REQUEST_ID_MULTIPLE_PERMISSIONS);
//            return false;
//        }
//        return true;
//    }

}
