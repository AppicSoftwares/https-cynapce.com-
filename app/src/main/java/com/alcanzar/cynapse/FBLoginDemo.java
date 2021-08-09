package com.alcanzar.cynapse;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;

import com.alcanzar.cynapse.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.facebook.AccessToken.refreshCurrentAccessTokenAsync;

public class FBLoginDemo extends AppCompatActivity {

    CallbackManager callbackManager;
    private static final String EMAIL = "email";

    LoginButton loginButton;
    AccessTokenTracker  accessTokenTracker;

    ProfileTracker profileTracker;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//       FirebaseApp.initializeApp(getApplicationContext());
//       FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
//       FacebookSdk.fullyInitialize();
//       AppEventsLogger.activateApp(getApplication());

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        if (FacebookSdk.isInitialized())
        {
            Log.d("isInitialized","true");
        }

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.

                Log.d("AccessToken1",oldAccessToken.getToken());
                Log.d("AccessToken2",currentAccessToken.getToken());
            }
        };


        Utils.getKeyHash(this);

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_fblogin_demo);
        //FacebookSdk.sdkInitialize(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);


        //loginManager = LoginManager.getInstance();
        //loginManager.setLoginBehavior(LoginBehavior.WEB_ONLY);
        //loginManager.setLoginBehavior(LoginBehavior.KATANA_ONLY);
        //loginManager.logInWithReadPermissions(this, Arrays.asList("email","public_profile"));

        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("loginButton:", "success");
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("loginButton", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("loginButton", exception.getMessage());
            }
        });

        if (AccessToken.getCurrentAccessToken()!=null)
        {
            Log.d("loginButton..", AccessToken.getCurrentAccessToken().getUserId());
        }

//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("res","onActivityResult");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //accessTokenTracker.stopTracking();
        //profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
//        loginManager.getInstance().logOut();
//        clearCookis();
        accessTokenTracker.startTracking();
        Log.d("resume","resume");
    }

    private void clearCookis() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    //Removed?
                }
            });
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(this);
            cookieManager.removeAllCookie();
        }
    }
}
