package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.GoogleOtp.AppSignatureHashHelper;
import com.alcanzar.cynapse.GoogleOtp.SMSReceiver;
import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.ResendOTPApi;
import com.alcanzar.cynapse.api.ValidateOTPApi;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OtpActivity extends AppCompatActivity implements SMSReceiver.OTPReceiveListener {

    //TODO: views
    EditText one, two, three, four;
    Button btnVerify;
    String phone_no = "", uuid = "", from = "", username = "";
    TextView timer_txt, resend;
    RelativeLayout rel;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    private SMSReceiver smsReceiver;
    private AppSignatureHashHelper appSignatureHashHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        initializeViews();

//        if (checkAndRequestPermissions()) {
//            Log.e("asdasda",true+"v");
//        }else {
//            Log.e("asdasda",false+"v");
//        }


        //TODO: calling the dashboard screen here
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(one.getText().toString()) && !TextUtils.isEmpty(two.getText().toString())
                        && !TextUtils.isEmpty(three.getText().toString()) && !TextUtils.isEmpty(four.getText().toString())) {
                    //TODO: calling the verified otp here
                    verifyOtp(one.getText().toString(), two.getText().toString(), three.getText().toString(), four.getText().toString());
                } else {
                    MyToast.toastLong(OtpActivity.this, "Please enter 4 digit OTP");
                }
            }
        });

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        final int[] secondsLeft = {0};
//        new CountDownTimer(30000, 100) {
//            public void onTick(long ms) {
//                if (Math.round((float)ms / 1000.0f) != secondsLeft[0])
//                {
//                    secondsLeft[0] = Math.round((float)ms / 1000.0f);
//                    if(secondsLeft[0] < 10)
//                    {
//                        timer_txt.setText("00 : 0" + secondsLeft[0]);
//                    }else
//                    {
//                        timer_txt.setText("00 : " + secondsLeft[0]);
//                    }
//
//                    if(secondsLeft[0] < 1)
//                    {
//                        timer_txt.setVisibility(View.GONE);
//                        resend.setTextColor(ContextCompat.getColor(OtpActivity.this,R.color.colorPrimary));
//                        rel.setClickable(true);
//                    }else
//                    {
//                        timer_txt.setVisibility(View.VISIBLE);
//                        resend.setTextColor(ContextCompat.getColor(OtpActivity.this,R.color.fadeGrey));
//                        rel.setClickable(false);
//                    }
//
//                }
//                //Constant.Log("test","ms="+ms+" till finished="+ secondsLeft[0]);
//            }
//
//            public void onFinish() {
//                //timer_txt.setText("0");
//              //  dialog.dismiss();
//            }
//        }.start();
    }

    private void startSMSListener() {

        try {

            appSignatureHashHelper = new AppSignatureHashHelper(this);
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onOTPReceived(String otp) {

        fillAutoOTP(otp);
        unRegisterService();

    }

    private void unRegisterService() {
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
        }
    }

    private void fillAutoOTP(String otp) {
        if (otp.substring(0, 41).equals("<#> Your OTP for Cynapce registration is:")) {

            one.setText("" + otp.substring(41, 42));
            two.setText("" + otp.substring(42, 43));
            three.setText("" + otp.substring(43, 44));
            four.setText("" + otp.substring(44, 45));
            one.setSelection(one.getText().length());
            two.setSelection(two.getText().length());
            three.setSelection(three.getText().length());
            four.setSelection(four.getText().length());

        }
    }

    @Override
    public void onOTPTimeOut() {
        //MyToast.toastShort(OtpActivity.this, "OTP Time out");
    }

    @Override
    public void onOTPReceivedError(String error) {
        //MyToast.toastShort(OtpActivity.this, "OTP Time out error");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterService();
    }

    //TODO: initialization of all the views are done here
    private void initializeViews() {

        startSMSListener();

        timer_txt = findViewById(R.id.timer_txt);
        resend = findViewById(R.id.resend);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        btnVerify = findViewById(R.id.btnVerify);
        rel = findViewById(R.id.rel);

        if (getIntent() != null) {
            phone_no = getIntent().getStringExtra("phone_no");
            uuid = getIntent().getStringExtra("uuid");
            from = getIntent().getStringExtra("from");

            if (getIntent().hasExtra("username"))
                username = getIntent().getStringExtra("username");
            else {
                username = "fromEditProfile";
            }
        }

        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (one.getText().length() == 1) {
                    two.requestFocus();
                } else if (one.getText().length() == 0) {
                    one.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (two.getText().length() == 1) {
                    three.requestFocus();
                } else if (two.getText().length() == 0) {
                    one.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (three.getText().length() == 1) {
                    four.requestFocus();
                } else if (three.getText().length() == 0) {
                    two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (four.getText().length() == 1) {
                    four.requestFocus();
                } else if (four.getText().length() == 0) {
                    three.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        four.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(keyCode == KeyEvent.KEYCODE_DEL){
//                    //on backspace
//                    three.requestFocus();
//                }
//                return false;
//            }
//        });three.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(keyCode == KeyEvent.KEYCODE_DEL){
//                    //on backspace
//                    two.requestFocus();
//                }
//                return false;
//            }
//        });two.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(keyCode == KeyEvent.KEYCODE_DEL){
//                    //on backspace
//                    one.requestFocus();
//                }
//                return false;
//            }
//        });

        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    startSMSListener();

                    ResendOTPApi(phone_no, uuid, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equalsIgnoreCase("otp")) {
//                final String message = intent.getStringExtra("message");
//                final String sender = intent.getStringExtra("sender");
//                String a[] = message.split("(?!^)");
////                char[] a = message.toCharArray();
//
//
//                if(sender.equalsIgnoreCase("IG-CYNAPC"))
//                {
//                    one.setText(a[0]);
//                    two.setText(a[1]);
//                    three.setText(a[2]);
//                    four.setText(a[3]);
//                    one.setSelection(one.getText().length());
//                    two.setSelection(two.getText().length());
//                    three.setSelection(three.getText().length());
//                    four.setSelection(four.getText().length());
//                }
//
//
////                TextView tv = (TextView) findViewById(R.id.txtview);
////                tv.setText(message);
//            }
//        }
//    };

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
//        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
//        if (readSMS != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
//        }
//        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
//        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        //  LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    //TODO: verify otp process
    private void verifyOtp(String s, String s1, String s2, String s3) {
        String enteredOtp = s + s1 + s2 + s3;
        Log.d("enteredOtp", enteredOtp.trim());
        String testOtp = "1234";
        try {
            ValidateOTPApi(phone_no, enteredOtp, uuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (testOtp.trim().matches(enteredOtp.trim())) {
//            MyToast.toastLong(this, "Otp Verified Successfully");
//            startActivity(new Intent(OtpActivity.this, MainActivity.class));
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            ActivityCompat.finishAffinity(OtpActivity.this);
//            finish();
//        } else {
//            MyToast.toastLong(this, "Invalid Otp");
//        }
    }

    private void ValidateOTPApi(final String phone_no, String enteredOtp, final String uuid) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("mobile_number", phone_no);
        params.put("otp", enteredOtp);

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ValidateOTPApi(OtpActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
                    if (res_code.equals("1")) {
                        MyToast.toastLong(OtpActivity.this, res_msg);
//                        if (AppCustomPreferenceClass.readString(OtpActivity.this, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0")) {
//                            MyToast.toastLong(OtpActivity.this, "Please verify your email by clicking on verification link sent on your email Id");
//                        }

//                        if (from.equalsIgnoreCase("myprof")) {
//                            Intent intent = new Intent(OtpActivity.this, MyProfileActivity.class);
//                            intent.putExtra("edit_disable", "false");
//                            startActivity(intent);
//                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                            finish();
//                        }

                        if (username.equals("")) {
                            Intent intent = new Intent(OtpActivity.this, NameEntryActivity.class);
                            intent.putExtra("uuid", uuid);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            ActivityCompat.finishAffinity(OtpActivity.this);
                        } else if (username.equals("fromEditProfile")) {
                            Intent intent = new Intent(OtpActivity.this, MyProfileActivity.class);
                            startActivity(intent);
                            //do stuff
                        } else {
                            startActivity(new Intent(OtpActivity.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            ActivityCompat.finishAffinity(OtpActivity.this);
                        }
                        finish();
                    } else {
                        MyToast.toastLong(OtpActivity.this, res_msg);
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

    private void ResendOTPApi(String phone_no, String uuid, final boolean b) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("mobile_number", phone_no);
        params.put("hash_key", appSignatureHashHelper.getAppSignatures().get(0));

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ResendOTPApi(OtpActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
                    if (res_code.equals("1")) {
                        if (b)
                            MyToast.toastLong(OtpActivity.this, res_msg);
//                        startActivity(new Intent(OtpActivity.this, MainActivity.class));
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        ActivityCompat.finishAffinity(OtpActivity.this);
//                        finish();
                    } else {
                        MyToast.toastLong(OtpActivity.this, res_msg);
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
}


//336171