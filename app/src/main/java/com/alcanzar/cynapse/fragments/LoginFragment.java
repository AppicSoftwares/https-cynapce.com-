package com.alcanzar.cynapse.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.GoogleOtp.AppSignatureHashHelper;
import com.alcanzar.cynapse.LinkedInWebView.LinkedInLoginWebView;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.LoginSignUpActivity;
import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.activity.NameEntryActivity;
import com.alcanzar.cynapse.activity.OtpActivity;
import com.alcanzar.cynapse.api.ForgotPassWordApi;
import com.alcanzar.cynapse.api.LoginApi;
import com.alcanzar.cynapse.api.PostDeviceDataApi;
import com.alcanzar.cynapse.app.AppController;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.GPSTracker;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.PostImage;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //TODO: views of the layout
    Button btnLogIn;
    TextView createAccount;
    LinearLayout linOne;
    EditText userName, passWord;
    View viewOne, viewTwo;
    TextView forgotPassword;
    String setEmail;
    ImageView google_login, fb_login, linkedin_login;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    String emailId = "", name = "";
    public GPSTracker tracker;
    private String firebaseToken = "";

    private AppSignatureHashHelper appSignatureHashHelper;

    private LoginManager loginManager;
    private  AccessTokenTracker accessTokenTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getKeyHash(getActivity());
        initFb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: Inflate the layout for this fragment
        firebaseToken(getActivity());
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: called to initialize all the views

        initializeViews(view);

        //TODO: username and password watcher and validations
        final String userStr = userName.getText().toString().trim();
        //TODO: these two are used to validate the email
        final String emailStrOne = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String emailStrTwo = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
        //TODO: also use
//        public final static boolean isValidEmail(CharSequence target) {
//            return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
//        }


        tracker = new GPSTracker(getActivity()) {
            @Override
            public void setLoc() {

            }
        };

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if ((str.matches(emailStrOne) || str.matches(emailStrTwo)) && s.length() > 0) {
                    Log.d("user1", "enter");
                    viewOne.setBackgroundColor(getResources().getColor(R.color.green));
                    //userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login, 0, R.drawable.green_tick, 0);
                    //2bb673
                } else if (s.length() == 0) {
                    Log.d("user2", "enter");
                    viewOne.setBackgroundColor(getResources().getColor(R.color.view_color));
                    //userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login, 0, 0, 0);
                } else {
                    Log.d("user3", "enter");
                    viewOne.setBackgroundColor(getResources().getColor(R.color.red));
                    //userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login, 0, R.drawable.exclaimtionmark, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("user4", "enter");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("user5", "enter");
            }
        });

        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5) {
                    Log.d("pass1", "enter");
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.green));
                    passWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.green_tick, 0);
                } else if (s.length() == 0) {
                    Log.d("pass2", "enter");
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.view_color));
                    passWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
                } else {
                    Log.d("pass3", "enter");
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.red));
                    passWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.exclaimtionmark, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("pass4", "enter");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("pass4", "enter");
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        fbLogin();
    }


    void initFb()
    {


//        AccessToken.setCurrentAccessToken(null);
//        Profile.setCurrentProfile(null);

        if (FacebookSdk.isInitialized())
        {
            System.out.println("isInitialized"+"yes");
        }

//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(
//                    AccessToken oldAccessToken,
//                    AccessToken currentAccessToken) {
//
//                Log.e("AccessToken",oldAccessToken.getToken()+"---"+currentAccessToken.getToken());
//
//                // Set the access token using
//                // currentAccessToken when it's loaded or set.
//            }
//        };

        // If the access token is available already assign it.
    }

    void fbLogin()
    {
        LoginManager.getInstance().registerCallback(((LoginSignUpActivity)getActivity()).callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest reuest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.e("LoginActivity", object.toString() + " <-> " + response.toString());
                                getFacebookResponse(object);
                            }
                        });
                Bundle parameters = new Bundle();
                //parameters.putString("fields", "id,name,first_name,last_name,email,gender,picture");
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,picture.width(200)");
                reuest.setParameters(parameters);
                reuest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("onCancel", "onCancelonCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("onError", "onError" + error.getMessage());
            }
        });
    }

    void firebaseToken(Activity activity) {
        FirebaseApp.initializeApp(activity);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Zsvadsv", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        firebaseToken = task.getResult().getToken();

                        // Log and toast
                        Log.e("TokenCheck", firebaseToken);
//                        Toast.makeText(MainActivity.this, "asdggadegads", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void getFacebookResponse(JSONObject response) {
        try {
            String social_media_id = response.getString("id");
            String email = response.getString("email");
            String first_name = response.getString("first_name");
            String last_name = response.getString("last_name");
//            ((Dashboard) getActivity()).logoutText.setText("Log out");
            String dob = "";
            if (response.has("birthday"))
                dob = response.getString("birthday");

            String gender;

            //String profile = "https://graph.facebook.com/" + social_media_id + "/picture?type=normal";

            // CustomPreference.writeString(getActivity(), CustomPreference.EmailRemContactnew, email);
            // CustomPreference.writeString(getActivity(), CustomPreference.Email, email);

            //CustomPreference.readString(getActivity(), CustomPreference.PicURL, profile);
            //  Log.e("profile", "profile" + profile);

//            if (response.getString("gender").equals("male"))
//                gender = "male";
//            else
//                gender = "female";

            //login(email, "", "2", social_media_id, first_name + " " + last_name);

            image = response.getJSONObject("picture").getJSONObject("data").getString("url");

            //uploadProfileImage(image, email, social_media_id, first_name, last_name);

            simpleloginSocial(image, email, "", "2", social_media_id, first_name + " " + last_name);

            // socialLogin(first_name, email, userType, profile, social_media_id, "1");
//            signUpp(first_name, last_name, gender, email, "", social_media_id, dob);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews(View v) {

        appSignatureHashHelper = new AppSignatureHashHelper(getActivity());

        //TODO: initializing all the views
        btnLogIn = v.findViewById(R.id.btnLogIn);
        createAccount = v.findViewById(R.id.createAccount);
        linOne = v.findViewById(R.id.linOne);
        userName = v.findViewById(R.id.userName);
        viewOne = v.findViewById(R.id.viewOne);
        passWord = v.findViewById(R.id.passWord);
        viewTwo = v.findViewById(R.id.viewTwo);
        google_login = v.findViewById(R.id.google_login);
        fb_login = v.findViewById(R.id.fb_login);
        linkedin_login = v.findViewById(R.id.linkedin_login);
        forgotPassword = v.findViewById(R.id.forgotPassword);
        //TODO: these below two lines are used to disable the focus of the edit text on launch
        linOne.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        linOne.setFocusableInTouchMode(true);
        btnLogIn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        createAccount.setOnClickListener(this);
        google_login.setOnClickListener(this);
        fb_login.setOnClickListener(this);
        linkedin_login.setOnClickListener(this);
    }


    //TODO: clicking checks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccount:
                //TODO: calling the signUp fragment here
                ((LoginSignUpActivity) getActivity()).replaceFragment(new SignUpFragment());
                break;
            case R.id.forgotPassword:

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.forgotpassword_alert);
                //TODO: used to make the background transparent
                Window window = dialog.getWindow();
                window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //TODO : initializing different views for the dialog
                final EditText editEmail = dialog.findViewById(R.id.ed_email);
                ImageView close = dialog.findViewById(R.id.close);
                Button btnSave = dialog.findViewById(R.id.btnSave);

                //TODO :setting different views
                dialog.show();
                //TODO : dismiss the on btn click and close click
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setEmail = editEmail.getText().toString();

                        try {
                            forgotPassword(setEmail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                        //TODO : finishing the activity
                        //finish();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //finish();
                    }
                });

                break;
            case R.id.google_login:
                mGoogleApiClient.connect();
                try {
                    signInWithGplus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("Riz", "Google Click");
                break;
            case R.id.fb_login:
                if (Util.isNetConnected(getActivity())) {
                    loginManager = LoginManager.getInstance();
                    loginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile","email"));
                    loginManager.setLoginBehavior(LoginBehavior.WEB_ONLY);
                    //loginManager.setLoginBehavior(LoginBehavior.KATANA_ONLY);
                } else
                    MyToast.toastShort(getActivity(), "No Internet Connection Enable First");
                break;
            case R.id.linkedin_login:
                //HandleLogin();
                Intent intent = new Intent(getActivity(), LinkedInLoginWebView.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.btnLogIn:
                //TODO: validations check
                if (isValid_()) {
                    //TODO: calling the loginApi
                    try {
                        //login(userName.getText().toString(), passWord.getText().toString(),"1","","");
                        simplelogin("", userName.getText().toString(), "1", "", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                if (!TextUtils.isEmpty(userName.getText().toString()) && !TextUtils.isEmpty(passWord.getText().toString())) {
//                    if (!Patterns.EMAIL_ADDRESS.matcher(userName.getText().toString()).matches()) {
//                        MyToast.toastLong(getActivity(), "Invalid Email");
//                    } else if (passWord.getText().toString().trim().length() < 5) {
//                        MyToast.toastLong(getActivity(), "Password must contain 5 characters");
//                    } else {
//                        //TODO: calling the loginApi
//                        try {
//                            login(userName.getText().toString(), passWord.getText().toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//                    MyToast.toastLong(getActivity(), "Please enter the login details");
//                }
                break;
        }
    }

    private void signInWithGplus() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(userName.getText().toString())) {
            MyToast.toastShort(getActivity(), "Please enter email ID");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userName.getText().toString()).matches()) {
            MyToast.toastShort(getActivity(), "Please enter valid email");
            return false;
        } else if (TextUtils.isEmpty(passWord.getText().toString())) {

            MyToast.toastShort(getActivity(), "Please enter password");
            return false;
        }
//        else if (passWord.getText().toString().trim().length() < 6) {
//            MyToast.toastLong(getActivity(), "Password must contain atleast 6 characters");
//            return false;
//        }else if(!isValidPassword(passWord.getText().toString()))
//        {
//            MyToast.toastShort(getActivity(), "Password should contain alphabets,numbers and special characters");
//            return false;
//        }
        return true;
    }

    private boolean isValid_() {
        if (TextUtils.isEmpty(userName.getText().toString())) {
            MyToast.toastShort(getActivity(), "Please enter mobile number");
            return false;
        }
//        if (userName.getText().toString().length()>0)) {
//            MyToast.toastShort(getActivity(), "Please enter mobile number");
//            return false;
//        }
        return true;
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%&*()_+=|<>?{}\\\\[\\\\]~-])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static String getDeviceID(Context ctx) {
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mSignInClicked = false;
        //Constant.ToastLong(getActivity(), "User is connected!");
        // Get user's information
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.GET_ACCOUNTS}, 100);
//            return;
//        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), 0).show();
            return;
        }
        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
//                Const.showLog("in3");
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
//                Const.showLog("in4");
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                Log.e("result ", "result:" + result.toString());
                handleSignInResult(result);

            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }

        } else if (requestCode == 100) {
            try {
                if (data != null) {
                    System.out.println("ResultBOb" + data.getStringExtra("res"));
                    String res = data.getStringExtra("res");

                    JSONObject jsonObject = new JSONObject(res);
                    emailId = jsonObject.getJSONArray("elements").getJSONObject(0).getJSONObject("handle~").getString("emailAddress");

                    if (!emailId.isEmpty()) {
                        //login(emailId, "", "4", "_", "_");
                        simpleloginSocial(profile_image, emailId, "", "4", "_", "");
                    }
                }
            } catch (JSONException | NullPointerException ee) {
                ee.printStackTrace();
            }
        }

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(AppController.TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount currentPerson = result.getSignInAccount();

            String personName = currentPerson.getDisplayName();
//            first_name = personName;
            emailId = currentPerson.getEmail();
            String social_media_id = currentPerson.getId();
//            if(currentPerson.getPhotoUrl() == null)
//            {
//                pic_url = "";
//            }
//            else
//            {
//                pic_url= String.valueOf(currentPerson.getPhotoUrl());
//            }
//            CustomPreference.writeString(getActivity(), CustomPreference.EmailRemContactnew, emailId);
//            CustomPreference.writeString(getActivity(), CustomPreference.Email, emailId);
//
//            socialLogin(first_name, emailId, userType, pic_url, social_media_id, "2");
            try {
                //login(emailId, "", "3", social_media_id, personName);
                simpleloginSocial(profile_image, emailId, "", "3", social_media_id, personName);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //      Log.e(AppController.TAG, "Name: " + personName + ", plusProfile: " + currentPerson + ", email: " + emailId+"image" + pic_url);

            // loginUser(userType);
        }
    }

    private void HandleLogin() {


        LISessionManager.getInstance(getApplicationContext()).init(getActivity(), buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
//                               Toast.makeText(getApplicationContext(),"success"+
//                        LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
                Log.e("linkedin_result", "<><" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString());
                FetchPublicInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                //  Toast.makeText(getApplicationContext(),"Failure"+error.toString(),Toast.LENGTH_LONG).show();
                Log.d("linkedin_result_Error", "<><" + error.toString());
            }
        }, true);
    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void FetchPublicInfo() {
        Log.e("asasvav", "Advadv");
        // String url = "https://api.linkedin.com/v1/people/~";

        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address)";

        //String url = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))";
        //String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,public-profile-url,picture-url,email-address,picture-urls::(original))";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(getContext(), url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                Log.e("apiResponse", "result:" + apiResponse.getResponseDataAsJson().toString());
                try {
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    emailId = jsonObject.getString("emailAddress");
                    name = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    String id = jsonObject.getString("id");
                    login(emailId, "", "4", id, name);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("anvknadvr", e.getMessage());
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                Log.e("anvknadv", liApiError.toString());

            }
        });
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Add this line to your existing onActivityResult() method
//        LISessionManager.getInstance(getApplicationContext()).onActivityResult(getActivity(), requestCode, resultCode, data);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((getActivity()));
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void forgotPassword(String s) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("email", s);

        // params.put("device_id", getDeviceID(getActivity()));
        header.put("Cynapse", params);
        new ForgotPassWordApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSEFORGOT", response.toString());
                    if (res_code.equals("1")) {
                        startActivity(new Intent(getActivity(), LoginSignUpActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        ActivityCompat.finishAffinity(getActivity());
                        getActivity().finish();
                        MyToast.toastLong(getActivity(), res_msg);
                    } else {
                        MyToast.toastLong(getActivity(), res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }

    private void login(String email, String password, String login_type, String social_network_id, String name) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("email", email);
        params.put("password", password);
        params.put("name", name);
        params.put("device_type", "android-mobile");
        params.put("platform_type", "Android");
        params.put("device_id", firebaseToken);
        params.put("social_network_id", social_network_id);
        params.put("login_type", login_type);
        params.put("hash_key", appSignatureHashHelper.getAppSignatures().get(0));
        header.put("Cynapse", params);

        new LoginApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    Log.e("login_responce", response.toString());
                    if (res_code.equals("1")) {
                        MyToast.toastLong(getActivity(), res_msg);
                        JSONObject item = header.getJSONObject("UserLogin");
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.UserId, item.getString("uuid"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.authToken, item.getString("auth_token"));


                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.name, item.getString("name"));

//                        Intent intent = new Intent(getActivity(), NameEntryActivity.class);
//                        intent.putExtra("uuid", uuid);
//                        startActivity(intent);
//                        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                        ActivityCompat.finishAffinity(getActivity());


                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.medical_profile_name, item.getString("medical_profile_name"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email, item.getString("email"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.phoneNumber, item.getString("phone_number"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.Country_name, item.getString("country_name"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.Country_id, item.getString("country_id"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.address, item.getString("address"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.dob, item.getString("dob"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.medical_profile_id, item.getString("medical_profile_id"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.occupation, item.getString("occupation"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.profile_image, item.getString("profile_image"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.title_id, item.getString("title_id"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.state_id, item.getString("state_id"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.city_id, item.getString("city_id"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.department_id, item.getString("department_id"));

                        if (item.has("sub_specialization_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sub_specialization_id, item.getString("sub_specialization_id"));
                        } else {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sub_specialization_id, "");
                        }

                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.specilization_id, item.getString("specilization_id"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.highest_degree_id, item.getString("highest_degree_id"));
                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.year_of_study, item.getString("year_of_study"));

                        //resume fields
                        AppCustomPreferenceClass.writeString(getActivity(), "pdf_name_code", item.getString("resume_url"));
                        AppCustomPreferenceClass.writeString(getActivity(), "pdf_name", item.getString("resume_name"));
                        postDeviceData();
                        // Log.e("uuid",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.UserId,""));
                        // Log.e("auth",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.authToken,""));
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        ActivityCompat.finishAffinity(getActivity());
                        getActivity().finish();

                    } else {
                        if (header.has("mobile_verified") && header.has("mobile_verified")) {

                            String mobile_verified = header.getString("mobile_verified");
                            String email_verified = header.getString("email_verified");
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email_verified, email_verified);
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.mobile_verified, mobile_verified);
                            //AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.UserId, header.getString("uuid"));

                            if (mobile_verified.equalsIgnoreCase("0")) {
                                Intent intent = new Intent(getActivity(), OtpActivity.class);
                                intent.putExtra("phone_no", header.getString("phone_number"));
                                intent.putExtra("uuid", header.getString("uuid"));
                                intent.putExtra("from", "login");
                                startActivity(intent);
                                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }

                        MyToast.toastLong(getActivity(), res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }

    private void simplelogin(String email, String password, String login_type, String social_network_id, String name) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("mobile_number", password);
        params.put("device_type", "android-mobile");
        params.put("platform_type", "Android");
        params.put("device_id", firebaseToken);
        //params.put("device_id", getDeviceID(getActivity()));
        params.put("social_network_id", social_network_id);
        params.put("email", email);
        params.put("login_type", login_type);
        params.put("username", "");
        params.put("profile_image", "");
        params.put("hash_key", appSignatureHashHelper.getAppSignatures().get(0));
        header.put("Cynapse", params);

        Log.i("headerbb", header.toString());

        new LoginApi(getActivity(), header, 0) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try
                {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.e("login_responce", response.toString());

                    if (res_code.equals("1")) {
                        // MyToast.toastLong(getActivity(), res_msg);
                        JSONObject item;
                        if (header.has("SignUp")) {
                            item = header.getJSONObject("SignUp");
                        } else {
                            item = header.getJSONObject("UserLogin");
                        }

                        if (item.has("uuid")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.UserId, item.getString("uuid"));
                        }

                        if (item.has("auth_token")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.authToken, item.getString("auth_token"));
                        }

                        if (item.has("medical_profile_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.medical_profile_id, item.getString("medical_profile_id"));
                        }

                        if (item.has("email")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email, item.getString("email"));
                        }

                        if (item.has("email_verified")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email_verified, item.getString("email_verified"));
                        }

                        if (item.has("phone_number")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.phoneNumber, item.getString("phone_number"));
                        }

                        if (item.has("name")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.name, item.getString("name"));
                        }

                        if (item.has("login_type")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.login_type, item.getString("login_type"));
                        }

                        if (item.has("country_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.Country_id, item.getString("country_id"));
                        }

                        if (item.has("state_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.state_id, item.getString("state_id"));
                        }

                        if (item.has("city_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.city_id, item.getString("city_id"));
                        }

                        if (item.has("address")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.address, item.getString("address"));
                        }

                        if (item.has("dob")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.dob, item.getString("dob"));
                        }

                        if (item.has("occupation")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.occupation, item.getString("occupation"));
                        }

                        if (item.has("profile_image")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.profile_image, item.getString("profile_image"));
                        }

                        if (header.has("profile_image")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.profile_image, header.getString("profile_image"));
                        }

                        if (item.has("title_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.title_id, item.getString("title_id"));
                        }

                        if (item.has("specilization_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.specilization_id, item.getString("specilization_id"));
                        }

                        if (item.has("department_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.department_id, item.getString("department_id"));
                        }

                        if (item.has("sub_specialization_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sub_specialization_id, item.getString("sub_specialization_id"));
                        }

                        if (item.has("highest_degree_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.highest_degree_id, item.getString("highest_degree_id"));
                        }

                        if (item.has("year_of_study")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.year_of_study, item.getString("year_of_study"));
                        }

                        if (item.has("resume_url")) {
                            AppCustomPreferenceClass.writeString(getActivity(), "pdf_name_code", item.getString("resume_url"));
                        }

                        if (item.has("resume_name")) {
                            AppCustomPreferenceClass.writeString(getActivity(), "pdf_name", item.getString("resume_name"));
                        }

                        //resume fields

                        postDeviceData();
                        // Log.e("uuid",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.UserId,""));
                        // Log.e("auth",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.authToken,""));
//                        startActivity(new Intent(getActivity(), MainActivity.class));
//                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        ActivityCompat.finishAffinity(getActivity());
//                        getActivity().finish();

                        Intent intent = new Intent(getActivity(), OtpActivity.class);
                        intent.putExtra("phone_no", item.getString("phone_number"));
                        intent.putExtra("uuid", item.getString("uuid"));
                        intent.putExtra("username", item.getString("name"));
                        intent.putExtra("from", "login");
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    } else {
//                        if (header.has("mobile_verified") && header.has("mobile_verified")) {
//                            String mobile_verified = header.getString("mobile_verified");
//                            String email_verified = header.getString("email_verified");
//                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email_verified, email_verified);
//                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.mobile_verified, mobile_verified);
//                            //AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.UserId, header.getString("uuid"));
//                            if (mobile_verified.equalsIgnoreCase("0")) {
//                                Intent intent = new Intent(getActivity(), OtpActivity.class);
//                                intent.putExtra("phone_no", header.getString("phone_number"));
//                                intent.putExtra("uuid", header.getString("uuid"));
//                                intent.putExtra("from", "login");
//                                startActivity(intent);
//                                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                            }
//                        }
                        MyToast.toastLong(getActivity(), res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }

    private void uploadProfileImage(String picturePath, final String email, final String social_media_id, final String first_name, final String last_name) {

        //final String fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
        final String fileName = picturePath;
        // final String fileName = Util.imageFile.getName();
//        String uuid = CustomPreference.readString(this, CustomPreference.USER_ID, "");

        String url = AppConstantClass.HOST + "fileUpload/productimage";
        PostImage post = new PostImage(Util.imageFile, url, fileName, getActivity(), "image") {
            @Override
            public void receiveData(String response) {
//                getProfileImage(response);
                try {
                    Log.e("profileimage", " = " + response);
                    JSONObject response1 = new JSONObject(response);
                    JSONObject data = response1.getJSONObject("Cynapse");
                    //MyToast.logMsg("jsonImage", data.toString());
                    String res = data.getString("res_code");
                    String res1 = data.getString("res_msg");
                    profile_image = data.getString("file_name");
                    System.out.println("multipleImagesAl" + profile_image);

                    //simpleloginSocial(email, "", "2", social_media_id, first_name + " " + last_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void receiveError() {
                Log.e("PROFILE", "ERROR");
            }
        };
        post.execute(url, null, null);
    }

    private void simpleloginSocial(String profile_image, String email, String password, String login_type, String social_network_id, String name) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("mobile_number", password);
        params.put("device_type", "android-mobile");
        params.put("platform_type", "Android");
        //params.put("device_id", getDeviceID(getActivity()));
        params.put("device_id", firebaseToken);
        params.put("social_network_id", social_network_id);
        params.put("email", email);
        params.put("login_type", login_type);
        params.put("username", name);
        params.put("profile_image", profile_image);
        params.put("hash_key", appSignatureHashHelper.getAppSignatures().get(0));
        header.put("Cynapse", params);

        Log.i("asdasd", AppConstantClass.signUpNew);

        new LoginApi(getActivity(), header, 0) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.e("login_responce", response.toString());

                    if (res_code.equals("1")) {
                        MyToast.toastLong(getActivity(), res_msg);
                        JSONObject item;
                        if (header.has("SignUp")) {
                            item = header.getJSONObject("SignUp");
                        } else {
                            item = header.getJSONObject("UserLogin");
                        }

                        if (item.has("uuid")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.UserId, item.getString("uuid"));
                        }

                        if (item.has("auth_token")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.authToken, item.getString("auth_token"));
                        }

                        if (item.has("medical_profile_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.medical_profile_id, item.getString("medical_profile_id"));
                        }

                        if (item.has("email")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email, item.getString("email"));
                        }

                        if (item.has("email_verified")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email_verified, item.getString("email_verified"));
                        }

                        if (item.has("phone_number")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.phoneNumber, item.getString("phone_number"));
                        }

                        if (item.has("name")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.name, item.getString("name"));
                        }

                        if (item.has("login_type")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.login_type, item.getString("login_type"));
                        }

                        if (item.has("country_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.Country_id, item.getString("country_id"));
                        }

                        if (item.has("state_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.state_id, item.getString("state_id"));
                        }

                        if (item.has("city_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.city_id, item.getString("city_id"));
                        }

                        if (item.has("address")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.address, item.getString("address"));
                        }

                        if (item.has("dob")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.dob, item.getString("dob"));
                        }

                        if (item.has("occupation")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.occupation, item.getString("occupation"));
                        }

                        if (item.has("profile_image")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.profile_image, item.getString("profile_image"));
                        }

                        if (header.has("profile_image")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.profile_image, header.getString("profile_image"));
                        }

                        if (item.has("title_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.title_id, item.getString("title_id"));
                        }

                        if (item.has("specilization_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.specilization_id, item.getString("specilization_id"));
                        }

                        if (item.has("department_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.department_id, item.getString("department_id"));
                        }

                        if (item.has("sub_specialization_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.sub_specialization_id, item.getString("sub_specialization_id"));
                        }

                        if (item.has("highest_degree_id")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.highest_degree_id, item.getString("highest_degree_id"));
                        }

                        if (item.has("year_of_study")) {
                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.year_of_study, item.getString("year_of_study"));
                        }

                        if (item.has("resume_url")) {
                            AppCustomPreferenceClass.writeString(getActivity(), "pdf_name_code", item.getString("resume_url"));
                        }

                        if (item.has("resume_name")) {
                            AppCustomPreferenceClass.writeString(getActivity(), "pdf_name", item.getString("resume_name"));
                        }

                        //resume fields

                        postDeviceData();
                        // Log.e("uuid",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.UserId,""));
                        // Log.e("auth",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.authToken,""));
//                        startActivity(new Intent(getActivity(), MainActivity.class));
//                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        ActivityCompat.finishAffinity(getActivity());
//                        getActivity().finish();

//                        Intent intent = new Intent(getActivity(), OtpActivity.class);
//                        intent.putExtra("phone_no", item.getString("phone_number"));
//                        intent.putExtra("uuid", item.getString("uuid"));
//                        intent.putExtra("username", item.getString("name"));
//                        intent.putExtra("from", "login");
//                        startActivity(intent);
//                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        ActivityCompat.finishAffinity(getActivity());
                        getActivity().finish();


                    } else {
//                        if (header.has("mobile_verified") && header.has("mobile_verified")) {
//                            String mobile_verified = header.getString("mobile_verified");
//                            String email_verified = header.getString("email_verified");
//                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email_verified, email_verified);
//                            AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.mobile_verified, mobile_verified);
//                            //AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.UserId, header.getString("uuid"));
//                            if (mobile_verified.equalsIgnoreCase("0")) {
//                                Intent intent = new Intent(getActivity(), OtpActivity.class);
//                                intent.putExtra("phone_no", header.getString("phone_number"));
//                                intent.putExtra("uuid", header.getString("uuid"));
//                                intent.putExtra("from", "login");
//                                startActivity(intent);
//                                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                            }
//                        }
                        MyToast.toastLong(getActivity(), res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }

    private void postDeviceData() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.UserId, ""));
        params.put("access_token", AppCustomPreferenceClass.readString(getActivity(), AppCustomPreferenceClass.authToken, ""));
        params.put("device_id", firebaseToken);
        params.put("device_type", "1");
        header.put("Cynapse", params);
        new PostDeviceDataApi(getActivity(), header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        // MyToast.toastLong(getActivity(), res_msg);
//                        JSONObject item = header.getJSONObject("UserLogin");
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.UserId, item.getString("uuid"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.authToken, item.getString("auth_token"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.name, item.getString("name"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.medical_profile_name, item.getString("medical_profile_name"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.email, item.getString("email"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.phoneNumber, item.getString("phone_number"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.Country_name, item.getString("country_name"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.Country_id, item.getString("country_id"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.address, item.getString("address"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.dob, item.getString("dob"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.medical_profile_id, item.getString("medical_profile_id"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.occupation, item.getString("occupation"));
//                        AppCustomPreferenceClass.writeString(getActivity(), AppCustomPreferenceClass.profile_image, item.getString("profile_image"));
//                        // Log.e("uuid",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.UserId,""));
//                        // Log.e("auth",AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.authToken,""));
//                        startActivity(new Intent(getActivity(), MainActivity.class));
//                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        ActivityCompat.finishAffinity(getActivity());
//                        getActivity().finish();
                    } else {
                        // MyToast.toastLong(getActivity(), res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }

    private String profile_image = "";
    private String image = "";

}
//0D9ysll5vk7gCuGB4LLgpiZodW8=