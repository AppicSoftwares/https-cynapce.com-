package com.alcanzar.cynapse.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.ConferenceBookApi;
import com.alcanzar.cynapse.api.PostPaymentApi;
import com.alcanzar.cynapse.api.PostRSAApi;
import com.alcanzar.cynapse.api.ShortListofJobPostApi;
import com.alcanzar.cynapse.api.Update_Package_StatusAPI;
import com.alcanzar.cynapse.api.applyForRequestJobApi;
import com.alcanzar.cynapse.api.job_payment_statusAPI;
import com.alcanzar.cynapse.fragments.ActionDialog;
import com.alcanzar.cynapse.fragments.ApproveOTPFragment;
import com.alcanzar.cynapse.fragments.CityBankFragment;
import com.alcanzar.cynapse.fragments.OtpFragment;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.CustomPreference;
import com.alcanzar.cynapse.utils.LoadingDialog;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.RSAUtility;
import com.alcanzar.cynapse.utils.ServiceUtility;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import static com.alcanzar.cynapse.fragments.BookTicketFragmentNew.allUsersList;


public class WebViewActivity extends AppCompatActivity implements Communicator {

    WebView myBrowser;
    WebSettings webSettings;
    private BroadcastReceiver mIntentReceiver;
    String bankUrl = "", totalAmount = "";
    FragmentManager manager;
    ActionDialog actionDialog = new ActionDialog();
    Timer timer = new Timer();
    TimerTask timerTask;
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    public int loadCounter = 0;
    ImageView btnBack;
    TextView title;
    boolean book_ticket = false;
    boolean job_bool = false;
    boolean reviewBookingNewPay = false;


    String status = "", days = "", extra_charges = "", accomadation_charges = "", job_id = "", total_jobs = "", plan_type = "", medical_profile_id = "", title_id = "";
    Intent mainIntent;
    String html, order_ID, encVal, no_of_seats;
    int MyDeviceAPI;
    Integer randomNum = ServiceUtility.randInt(0, 9999999);
    RelativeLayout rel_header;

    private String uuid = "";
    private String allUserList = "";
    private String noOFSeats = "";
    private String promocode_id = "";
    private String date_to_notify_user = "";

    //String merchantId = "185659", accessCode = "AVUR80FJ99AW68RUWA", seat, conference_id;
    //String merchantId = "185659", accessCode = "AVSG82GA60AA54GSAA", seat, conference_id;//For IP  = 35...
    String merchantId = "185659", accessCode = "AVAQ79FG53CN55QANC", seat, conference_id;// cynapce.com/
    //String merchantId = "2", accessCode = "4YRUXLSRO20O8NIH";

    /**
     * Async task class to get json by making HTTP call
     */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (!ServiceUtility.chkNull(vResponse).equals("")
                        && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                    StringBuffer vEncVal = new StringBuffer("");
                    vEncVal.append(ServiceUtility.addToPostParams(AppCustomPreferenceClass.AMOUNT, totalAmount));//////COMMENTTT
                    vEncVal.append(ServiceUtility.addToPostParams(AppCustomPreferenceClass.CURRENCY, "INR"));//////COMMENTTT
                    Log.e("jdubvadv", vEncVal.toString());
                    encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
                    Log.e("dvadvad", encVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            LoadingDialog.cancelLoading();
            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    try {
                        // process the html source code to get final status of transaction
                        Log.v("Logs", "-------------- Process HTML11 : " + html);

//                        String status = null;
                        Log.v("Logs", "-------------- Process HTML22 : " + status);

                        if (html.indexOf("failure") != -1) {

                            Log.d("FAILUREEE", "111111");
                            status = "Failure";
                            try {
                                Log.d("STAUTSSSS22", status);
                                if (job_bool) {
                                    try {
                                        job_payment_api(job_id, plan_type, totalAmount, "2", order_ID);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

//                                    try {
//                                        Update_Package_StatusAPI(total_jobs, plan_type, medical_profile_id, title_id);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }

                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_code);
                                    CustomPreference.writeString(WebViewActivity.this, CustomPreference.CheckAddKey, "3");

                                    if (interstitialAd.isLoaded() || interstitialAd.isLoading()) {
                                        interstitialAd.show();
//                                       Intent  intent = new Intent(this, AboutActivity.class);
                                        showInterstitialAds();
                                    } else {
                                        Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
                                        intent1.putExtra("tabPress", "2");
                                        startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        finish();
                                    }
                                } else if (reviewBookingNewPay) {

                                    PostPaymentTicketApi("1", "2");
                                    conferenceBookSeatApi(conference_id, noOFSeats, totalAmount);
                                    Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                                    intent.putExtra("paymentsucess", "1");
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_code);
//                                  AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price3);
                                    Toast.makeText(WebViewActivity.this, "Transaction Failed", Toast.LENGTH_LONG).show();
                                } else {
                                    PostPaymentTicketApi("2", totalAmount, order_ID, "2", promocode_id, date_to_notify_user);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_code);
//
                                    Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
                                    intent1.putExtra("tabPress", "2");
                                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            finish();
                        } else if (html.indexOf("success") != -1) {
                            Log.d("SUCCESSS", "222222");
                            status = "Success";
                            try {
                                Log.d("STAUTSSSS22", status);
                                if (book_ticket)
                                {
                                    PostPaymentTicketApi("1");
                                    conferenceBookSeatApi(conference_id, seat, totalAmount);
                                    Intent intent = new Intent(WebViewActivity.this, MyConferencesActivity.class);
                                    intent.putExtra("paymentsucess", "1");


                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_code);
//                                  AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price3);


                                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                } else if (job_bool) {

                                    try {
                                        job_payment_api(job_id, plan_type, totalAmount, "1", order_ID);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        Update_Package_StatusAPI(total_jobs, plan_type, medical_profile_id, title_id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Intent intent1 = new Intent(WebViewActivity.this, MyJobActivity.class);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_code);
                                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                } else if (reviewBookingNewPay) {
                                    PostPaymentTicketApi("1", "1");
                                    conferenceBookSeatApi(conference_id, noOFSeats, totalAmount);
                                    Intent intent = new Intent(WebViewActivity.this, MyConferencesActivity.class);
                                    intent.putExtra("paymentsucess", "1");
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_code);
//                                  AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price3);
                                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                } else {

                                    PostPaymentTicketApi("2", totalAmount, order_ID, "1", promocode_id, date_to_notify_user);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_price);
                                    AppCustomPreferenceClass.removeKey(WebViewActivity.this, AppCustomPreferenceClass.promo_code);
//
                                    Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
                                    intent1.putExtra("tabPress", "2");
                                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                            finish();
                        } else if (html.indexOf("aborted") != -1) {

                            try {
                                if (job_bool) {

                                    try {
                                        job_payment_api(job_id, plan_type, totalAmount, "3", order_ID);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
                                    intent1.putExtra("tabPress", "2");
                                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                } else if (reviewBookingNewPay) {
                                    Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
                                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    Toast.makeText(WebViewActivity.this, "Transaction Failed", Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            finish();
                        } else {
                            Log.d("DEDFAULTT", "444444");
                            status = "Status Not Known!";

                            try {
                                Log.d("STAUT77777", status);
                                if (job_bool) {

                                    try {
                                        job_payment_api(job_id, plan_type, totalAmount, "2", order_ID);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
                                    intent1.putExtra("tabPress", "2");
                                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    finish();
                                } else if (reviewBookingNewPay) {
                                    Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
                                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    Toast.makeText(WebViewActivity.this, "Transaction Failed", Toast.LENGTH_LONG).show();
                                }
//                                else {
//
//                                    PostPaymentTicketApi("2");
//                                    Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
//                                    intent1.putExtra("tabPress", "2");
//                                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                                }
                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                            finish();
                        }
                        //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.putExtra("transStatus", status);
//                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("Logs", "-------------- Error : " + e);
                    }
                }
            }

            //final WebView webview = (WebView) findViewById(R.id.webView);
            //myBrowser.getSettings().setJavaScriptEnabled(true);
            myBrowser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            myBrowser.setWebViewClient(new WebViewClient() {
                /*@Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    bankUrl = url;
                    return false;
                }*/


                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    bankUrl = url;
                    System.out.println(">>>>>>>>>>>>>>shouldOverrideUrlLoading11>>>>>>>>>>>>>>>>>>" + bankUrl);
                    return false;
                }

                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    bankUrl = url;
                    System.out.println(">>>>>>>>>>>>>>shouldOverrideUrlLoading22>>>>>>>>>>>>>>>>>>" + bankUrl);
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(myBrowser, url);

                    try
                    {
                        LoadingDialog.cancelLoading();

                        if (url.indexOf("/ccavResponseHandler") != -1) {
                            rel_header.setVisibility(View.VISIBLE);
                            myBrowser.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                        }

                        // calling load Waiting for otp fragment
                        if (loadCounter < 1) {
                            if (MyDeviceAPI >= 19) {
                                loadCitiBankAuthenticateOption(url);
                                loadWaitingFragment(url);
                            }
                        }
                        bankUrl = url;
                        Log.d("BANKURLLL", bankUrl);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    try {
                        LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            try {

                Log.d("ACCCODE", URLEncoder.encode(accessCode, "UTF-8"));
//                http://cynapce.com/Admins/ccavResponseHandler
                //String postData = AppCustomPreferenceClass.ACCESS_CODE + "=" + URLEncoder.encode(accessCode, "UTF-8") + "&" + AppCustomPreferenceClass.MERCHANT_ID + "=" + URLEncoder.encode(merchantId, "UTF-8") + "&" + AppCustomPreferenceClass.ORDER_ID + "=" + URLEncoder.encode(order_ID, "UTF-8") + "&" + AppCustomPreferenceClass.REDIRECT_URL + "=" + URLEncoder.encode("http://13.232.58.92/cynapce/Admins/ccavResponseHandler", "UTF-8") + "&" + AppCustomPreferenceClass.CANCEL_URL + "=" + URLEncoder.encode("http://cynapce.com/Admins/ccavResponseHandler", "UTF-8") + "&" + AppCustomPreferenceClass.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
                String postData = AppCustomPreferenceClass.ACCESS_CODE + "=" + URLEncoder.encode(accessCode, "UTF-8") + "&" + AppCustomPreferenceClass.MERCHANT_ID + "=" + URLEncoder.encode(merchantId, "UTF-8") + "&" + AppCustomPreferenceClass.ORDER_ID + "=" + URLEncoder.encode(order_ID, "UTF-8") + "&" + AppCustomPreferenceClass.REDIRECT_URL + "=" + URLEncoder.encode("https://cynapce.com/Admins/ccavResponseHandler", "UTF-8") + "&" + AppCustomPreferenceClass.CANCEL_URL + "=" + URLEncoder.encode("https://cynapce.com/Admins/ccavResponseHandler", "UTF-8") + "&" + AppCustomPreferenceClass.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
                myBrowser.postUrl(AppConstantClass.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        btnBack = findViewById(R.id.btnBack);
        title = findViewById(R.id.title);
        title = findViewById(R.id.title);
        title.setText("Payment Status");
        rel_header = findViewById(R.id.rel_header);

        showInterstitialAds();

        if (getIntent() != null)
        {
            book_ticket = getIntent().getBooleanExtra("book_ticket", false);
            job_bool = getIntent().getBooleanExtra("job_bool", false);
            reviewBookingNewPay = getIntent().getBooleanExtra("ReviewBookingNewPay", false);

            if (book_ticket) {
                seat = getIntent().getStringExtra("seat");
                conference_id = getIntent().getStringExtra("conference_id");
                days = getIntent().getStringExtra("days");
                extra_charges = getIntent().getStringExtra("extra_charges");
                accomadation_charges = getIntent().getStringExtra("accomadation_charges");
                totalAmount = getIntent().getStringExtra("totalAmount");
                order_ID = getIntent().getStringExtra("order_id");

                Log.d("seatilll", seat);
                Log.d("conderenceidwill", conference_id);
                Log.d("accomadation_charges", accomadation_charges);


            } else if (job_bool) {
                order_ID = getIntent().getStringExtra("order_id");
                job_id = getIntent().getStringExtra("job_id");
                totalAmount = getIntent().getStringExtra("totalAmount");
                //totalAmount = "1";
                total_jobs = getIntent().getStringExtra("total_jobs");
                plan_type = getIntent().getStringExtra("plan_type");
                medical_profile_id = getIntent().getStringExtra("medical_profile_id");
                title_id = getIntent().getStringExtra("title_id");

            } else if (reviewBookingNewPay) {
                totalAmount = getIntent().getStringExtra("totalAmount");
                //totalAmount = "1";
                order_ID = getIntent().getStringExtra("order_id");
                uuid = getIntent().getStringExtra("uuid");
                conference_id = getIntent().getStringExtra("conference_id");
                allUserList = getIntent().getStringExtra("AllUserList");
                noOFSeats = getIntent().getStringExtra("noOFSeats");
                promocode_id = getIntent().getStringExtra("promocode_id");
                date_to_notify_user = getIntent().getStringExtra("date_to_notify_user");

            } else {
                totalAmount = getIntent().getStringExtra("totalAmount");
                //totalAmount = "1";
                order_ID = getIntent().getStringExtra("order_id");
                conference_id = getIntent().getStringExtra("conference_id");
                promocode_id = getIntent().getStringExtra("promocode_id");
                date_to_notify_user = getIntent().getStringExtra("date_to_notify_user");

                if (getIntent().hasExtra("uuid")) {
                    uuid = getIntent().getStringExtra("uuid");
                }
                if (getIntent().hasExtra("no_of_seats")) {
                    no_of_seats = getIntent().getStringExtra("no_of_seats");
                }
            }

            Log.d("medical_profile_id", medical_profile_id);
            Log.d("TOTALAMONUNT", totalAmount);
            Log.d("title_id", title_id);
            Log.d("job_id", job_id);
            Log.d("total_jobs", total_jobs);
            Log.d("plan_type", plan_type);

        }

//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (status.equals("Failure") || status.equals("Cancelled") || status.equals("")) {
//
////                    try {
////                        Log.d("STAUTSSSS11", status);
////                        if (book_ticket) {
////                            PostPaymentTicketApi();
////                        }
////                        conferenceBookSeatApi(conference_id,seat,totalAmount);
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//                } else if (status.equals("Success")) {
//                    try {
//                        Log.d("STAUTSSSS22", status);
//                        if (book_ticket) {
//                            PostPaymentTicketApi();
//                            conferenceBookSeatApi(conference_id, seat, totalAmount);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        mainIntent = getIntent();
        manager = getFragmentManager();

        myBrowser = (WebView) findViewById(R.id.webView);
        webSettings = myBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        MyDeviceAPI = Build.VERSION.SDK_INT;
        // get rsa key method
        // get_RSA_key(mainIntent.getStringExtra(AppCustomPreferenceClass.ACCESS_CODE), mainIntent.getStringExtra(AppCustomPreferenceClass.ORDER_ID));

        // order_ID = randomNum.toString();
        Log.d("ORDERODEID", order_ID);
        try {
            getRSA(accessCode, order_ID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //get_RSA_key(accessCode, order_ID);
    }

    // Method to start Timer for 30 sec. delay
    public void startTimer() {
        try {
            //set a new Timer
            if (timer == null) {
                timer = new Timer();
            }
            //initialize the TimerTask's job
            initializeTimerTask();

            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer.schedule(timerTask, 30000, 30000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to Initialize Task
    public void initializeTimerTask() {
        try {
            timerTask = new TimerTask() {
                public void run() {

                    //use a handler to run a toast that shows the current timestamp
                    handler.post(new Runnable() {
                        public void run() {
                        /*int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), "I M Called ..", duration);
                        toast.show();*/
                            loadActionDialog();
                        }
                    });
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to stop timer
    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void loadCitiBankAuthenticateOption(String url) {
        if (url.contains("https://www.citibank.co.in/acspage/cap_nsapi.so")) {
            CityBankFragment citiFrag = new CityBankFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, citiFrag, "CitiBankAuthFrag");
            transaction.commit();
            loadCounter++;
        }
    }

    public void removeCitiBankAuthOption() {
        CityBankFragment cityFrag = (CityBankFragment) manager.findFragmentByTag("CitiBankAuthFrag");
        FragmentTransaction transaction = manager.beginTransaction();
        if (cityFrag != null) {
            transaction.remove(cityFrag);
            transaction.commit();
        }
    }

    // Method to load Waiting for OTP fragment
    public void loadWaitingFragment(String url) {

        // SBI Debit Card
        if (url.contains("https://acs.onlinesbi.com/sbi/")) {
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }

        // Kotak Bank Visa Debit card
        else if (url.contains("https://cardsecurity.enstage.com/ACSWeb/")) {
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        // For SBI and All its Asscocites Net Banking
        else if (url.contains("https://merchant.onlinesbi.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.onlinesbi.com/merchant/resendsmsotp.htm") || url.contains("https://m.onlinesbi.com/mmerchant/smsenablehighsecurity.htm")
                || url.contains("https://merchant.onlinesbh.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.onlinesbh.com/merchant/resendsmsotp.htm")
                || url.contains("https://merchant.sbbjonline.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.sbbjonline.com/merchant/resendsmsotp.htm")
                || url.contains("https://merchant.onlinesbm.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.onlinesbm.com/merchant/resendsmsotp.htm")
                || url.contains("https://merchant.onlinesbp.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.onlinesbp.com/merchant/resendsmsotp.htm")
                || url.contains("https://merchant.sbtonline.in/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.sbtonline.in/merchant/resendsmsotp.htm")) {
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }

        // For ICICI Credit Card
        else if (url.contains("https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/OtpServer")) {
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        // City bank Debit card
        else if (url.equals("cityBankAuthPage")) {
            removeCitiBankAuthOption();
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        // HDFC Debit Card and Credit Card
        else if (url.contains("https://netsafe.hdfcbank.com/ACSWeb/jsp/dynamicAuth.jsp?transType=payerAuth")) {
            //removeCitiBankAuthOption();
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        // For SBI  Visa credit Card
        else if (url.contains("https://secure4.arcot.com/acspage/cap")) {
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }

        // For Kotak Bank Visa Credit Card
        else if (url.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer")) {
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        } else {
            removeWaitingFragment();
            removeApprovalFragment();
            stopTimerTask();
        }
    }

    InterstitialAd interstitialAd;

    private void showInterstitialAds() {

        Log.e("interstitialAddShow", "showAdd");
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        MobileAds.initialize(this, getString(R.string.googleAdsAppID));
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.googleAdsInterstitialAdId));
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                String keyClose = CustomPreference.readString(WebViewActivity.this, CustomPreference.CheckAddKey, "");
                Log.e("vnkdnv", "sdgvbswdrbv");
                if (keyClose.equals("3")) {

                    Intent intent1 = new Intent(WebViewActivity.this, MainActivity.class);
                    intent1.putExtra("tabPress", "2");
                    startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else if (keyClose.equals("4")) {
                    finish();

                } else if (keyClose.equals("6")) {
                    finish();
                }

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();

            }
        });


//
    }


    // Method to remove Waiting fragment
    public void removeWaitingFragment() {
        OtpFragment waitingFragment = (OtpFragment) manager.findFragmentByTag("OTPWaitingFrag");
        if (waitingFragment != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(waitingFragment);
            transaction.commit();
        } else {
            // DO nothing
            //Toast.makeText(this," --test-- ",Toast.LENGTH_SHORT).show();
        }
    }

    // Method to load Approve Otp Fragment
    public void loadApproveOTP(String otpText, String senderNo) {
        try {
            Integer vTemp = Integer.parseInt(otpText);

            if (bankUrl.contains("https://acs.onlinesbi.com/sbi/") && senderNo.contains("SBI") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For Kotak bank Debit Card
            else if (bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/") && senderNo.contains("KOTAK") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // for SBI Net Banking
            else if ((((bankUrl.contains("https://merchant.onlinesbi.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbi.com/merchant/resendsmsotp.htm") || bankUrl.contains("https://m.onlinesbi.com/mmerchant/smsenablehighsecurity.htm")) && senderNo.contains("SBI"))
                    || ((bankUrl.contains("https://merchant.onlinesbh.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbh.com/merchant/resendsmsotp.htm")) && senderNo.contains("SBH"))
                    || ((bankUrl.contains("https://merchant.sbbjonline.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.sbbjonline.com/merchant/resendsmsotp.htm")) && senderNo.contains("SBBJ"))
                    || ((bankUrl.contains("https://merchant.onlinesbm.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbm.com/merchant/resendsmsotp.htm")) && senderNo.contains("SBM"))
                    || ((bankUrl.contains("https://merchant.onlinesbp.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbp.com/merchant/resendsmsotp.htm")) && senderNo.contains("SBP"))
                    || ((bankUrl.contains("https://merchant.sbtonline.in/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.sbtonline.in/merchant/resendsmsotp.htm")) && senderNo.contains("SBT"))) && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For ICICI Visa Credit Card
            else if (bankUrl.contains("https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/OtpServer") && senderNo.contains("ICICI") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For ICICI Debit card
            else if (bankUrl.contains("https://acs.icicibank.com/acspage/cap?") && senderNo.contains("ICICI") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For CITI bank Debit card
            else if (bankUrl.contains("https://www.citibank.co.in/acspage/cap_nsapi.so") && senderNo.contains("CITI") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For HDFC bank debit card and Credit Card
            else if (bankUrl.contains("https://netsafe.hdfcbank.com/ACSWeb/jsp/dynamicAuth.jsp?transType=payerAuth") && senderNo.contains("HDFC") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For HDFC Netbanking
            else if (bankUrl.contains("https://netbanking.hdfcbank.com/netbanking/entry") && senderNo.contains("HDFC") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For SBI Visa credit Card
            else if (bankUrl.contains("https://secure4.arcot.com/acspage/cap") && senderNo.contains("SBI") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            } else if (bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer") && senderNo.contains("KOTAK") && (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            } else {
                removeApprovalFragment();
                stopTimerTask();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeApprovalFragment() {
        ApproveOTPFragment approveOTPFragment = (ApproveOTPFragment) manager.findFragmentByTag("OTPApproveFrag");
        if (approveOTPFragment != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(approveOTPFragment);
            transaction.commit();
        }
    }

    public void loadActionDialog() {

        try {
            actionDialog.show(getFragmentManager(), "ActionDialog");
            stopTimerTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    //removeWaitingFragment();
                    removeApprovalFragment();
                    ///////////////////////////////////////
                    String msgText = intent.getStringExtra("get_otp");
                    String otp = msgText.split("\\|")[0];
                    String senderNo = msgText.split("\\|")[1];
                    if (MyDeviceAPI >= 19) {
                        loadApproveOTP(otp, senderNo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Exception :" + e, Toast.LENGTH_SHORT).show();
                }
            }
        };
        this.registerReceiver(mIntentReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mIntentReceiver);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    // On click of Approve button
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void respond(String otpText) {

        String data = otpText;
        try {
            // For SBI and all the associates
            if (bankUrl.contains("https://acs.onlinesbi.com/sbi/")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('otp').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For Kotak Bank Debit card
            else if (bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('txtOtp').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For SBI Visa credit card
            else if (bankUrl.contains("https://secure4.arcot.com/acspage/cap")) {
                myBrowser.evaluateJavascript("javascript:document.getElementsByName('pin1')[0].value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For SBI and associates banks Net Banking
            else if (bankUrl.contains("https://merchant.onlinesbi.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbi.com/merchant/resendsmsotp.htm") || bankUrl.contains("https://m.onlinesbi.com/mmerchant/smsenablehighsecurity.htm")
                    || bankUrl.contains("https://merchant.onlinesbh.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbh.com/merchant/resendsmsotp.htm")
                    || bankUrl.contains("https://merchant.sbbjonline.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.sbbjonline.com/merchant/resendsmsotp.htm")
                    || bankUrl.contains("https://merchant.onlinesbm.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbm.com/merchant/resendsmsotp.htm")
                    || bankUrl.contains("https://merchant.onlinesbp.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbp.com/merchant/resendsmsotp.htm")
                    || bankUrl.contains("https://merchant.sbtonline.in/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.sbtonline.in/merchant/resendsmsotp.htm")) {
                myBrowser.evaluateJavascript("javascript:document.getElementsByName('securityPassword')[0].value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For ICICI credit card
            else if (bankUrl.contains("https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/OtpServer")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('txtAutoOtp').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For ICICI bank Debit card
            else if (bankUrl.contains("https://acs.icicibank.com/acspage/cap?")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('txtAutoOtp').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For Citi Bank debit card
            else if (bankUrl.contains("https://www.citibank.co.in/acspage/cap_nsapi.so")) {
                myBrowser.evaluateJavascript("javascript:document.getElementsByName('otp')[0].value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For HDFC Debit card and Credit card
            else if (bankUrl.contains("https://netsafe.hdfcbank.com/ACSWeb/jsp/dynamicAuth.jsp?transType=payerAuth")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('txtOtpPassword').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // HDFC Net Banking
            else if (bankUrl.contains("https://netbanking.hdfcbank.com/netbanking/entry")) {
                myBrowser.evaluateJavascript("javascript:document.getElementsByName('fldOtpToken')[0].value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For Kotak Band visa Credit Card
            else if (bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('otpValue').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // for CITI Bank Authenticate with option selection
            if (data.equals("password")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('uid_tb_r').click();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            if (data.equals("smsOtp")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('otp_tb_r').click();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
                loadWaitingFragment("cityBankAuthPage");
            }
            loadCounter++;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void actionSelected(String data) {
        try {
            if (data.equals("ResendOTP")) {
                stopTimerTask();
                removeWaitingFragment();
                if (bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank")) {
                    myBrowser.evaluateJavascript("javascript:reSendOtp();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                // For HDFC Credit and Debit Card
                else if (bankUrl.contains("https://netsafe.hdfcbank.com/ACSWeb/jsp/dynamicAuth.jsp?transType=payerAuth")) {
                    myBrowser.evaluateJavascript("javascript:generateOTP();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                // SBI Visa Credit Card
                else if (bankUrl.contains("https://secure4.arcot.com/acspage/cap")) {
                    myBrowser.evaluateJavascript("javascript:OnSubmitHandlerResend();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                // For Kotak Visa Credit Card
                else if (bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer")) {
                    myBrowser.evaluateJavascript("javascript:doSendOTP();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                // For ICICI Credit Card
                else if (bankUrl.contains("https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/OtpServer")) {
                    myBrowser.evaluateJavascript("javascript:resend_otp();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                } else {
                    myBrowser.evaluateJavascript("javascript:resendOTP();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                //loadCounter=0;
            } else if (data.equals("EnterOTPManually")) {
                stopTimerTask();
                removeWaitingFragment();
            } else if (data.equals("Cancel")) {
                stopTimerTask();
                removeWaitingFragment();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Action not available for this Payment Option !", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void getRSA(String AccessCode, String order_id) throws JSONException {
//                {
//                    "Cynapse": {
//                    "uuid":"S3994",
//                            "medical_profile_id":"1",
//                            "job_title":"Doctor",
//                            "job_specilization_id":"2",
//                            "sub_specilization":"test",
//                            "location":"lucknow",
//                            "skill_required":"operation expert",
//                            "preffered_for":"lko",
//                            "ctc":"40000",
//                            "no_of_vaccancies":"10",
//                            "job_description" :"It hiering for doctor requierment"
//
//                }
//                }

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        // params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));

        params.put("order_id", order_id);
        params.put("access_code", AccessCode);

        header.put("Cynapse", params);
        new PostRSAApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {

                        vResponse = header.getString("data");
                        try {
                            if (vResponse.contains("!ERROR!")) {

                                show_alert(vResponse);

                            } else {
                                new RenderView().execute();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.d("VRESPONSE", vResponse);
                    } else {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
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

//    public void get_RSA_key(final String ac, final String od) {
//        LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
//
//        //StringRequest stringRequest = new StringRequest(Request.Method.POST, mainIntent.getStringExtra(AppCustomPreferenceClass.RSA_KEY_URL),/////COMMENTTTT
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://cynapce.com/cases/getCcevnue",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();
//                        Log.d("RESPONSEEEE", response);
//                        LoadingDialog.cancelLoading();
//                        vResponse = response;
//
//                        if (vResponse.contains("!ERROR!")) {
//
//                            show_alert(vResponse);
//
//                        } else {
//                            new RenderView().execute();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        LoadingDialog.cancelLoading();
//                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put(AppCustomPreferenceClass.ACCESS_CODE, ac);
//                params.put(AppCustomPreferenceClass.ORDER_ID, od);
//                Log.d("PARMADSS",params+"");
//                return params;
//            }
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }


    String vResponse;


    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                WebViewActivity.this).create();


        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);


        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog.show();
    }


    private void PostPaymentTicketApi(String type_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("amount", totalAmount);
        params.put("days", days);
        params.put("accomadation_charges", accomadation_charges);
        params.put("extra_charges", extra_charges);
        params.put("order_id", order_ID);
        params.put("type_id", type_id);

        //params.put("uuid", "S75f3");
        // params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.sync_time,""));
        //params.put("sync_time", "");
        header.put("Cynapse", params);
        Log.d("parmass", params + "");
        new PostPaymentApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    //AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.sync_time,sync_time);
                    Log.d("JOBSPONSE", response.toString());

                    if (res_code.equals("1")) {
                        MyToast.toastLong(WebViewActivity.this, res_msg);


                    } else {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
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

    private void PostPaymentTicketApi(String type_id, String amount, String orderID, String paymentStatus, String promocode_id, String date_to_notify_user) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();

//        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
//        params.put("conference_id", conference_id);
//        params.put("no_of_seats", "0");
//        params.put("total_amount", amount);
//        params.put("order_id", orderID);
//        params.put("payment_status", "0");
//        params.put("type_id", type_id);
//        params.put("user_details", "");

//        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
//        params.put("conference_id", conference_id);
//        params.put("amount", amount);
//        params.put("days", "0");
//        params.put("accomadation_charges", "");
//        params.put("extra_charges", "");
//        params.put("order_id", orderID);
//        params.put("type_id", type_id);

        //params.put("uuid", "S75f3");
        // params.put("sync_time", AppCustomPreferenceClass.readString(getActivity(),AppCustomPreferenceClass.sync_time,""));
        //params.put("sync_time", "");

        params.put("uuid", uuid);
        params.put("conference_id", conference_id);
        params.put("no_of_seats", no_of_seats);// with notification =1 , without notification = 2;
        params.put("total_amount", amount);
        params.put("order_id", orderID);
        params.put("payment_status", paymentStatus);
        params.put("type_id", type_id);
        params.put("user_details", "");
        params.put("promocode_id", promocode_id);
        params.put("date_to_notify_user", date_to_notify_user);
        header.put("Cynapse", params);

        Log.d("paymentRequest", header.toString());

        new PostPaymentApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    //AppCustomPreferenceClass.writeString(getActivity(),AppCustomPreferenceClass.sync_time,sync_time);
                    Log.d("JOBSPONSE", response.toString());

                    if (res_code.equals("1")) {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
                    } else {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
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


    private void PostPaymentTicketApi(String type_id, String paymentStatus) throws JSONException {

        JSONArray array = new JSONArray();

        for (int i = 0; i < allUsersList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", allUsersList.get(i).optString("name"));
            jsonObject.put("email", allUsersList.get(i).optString("email"));
            jsonObject.put("phone_no", allUsersList.get(i).optString("phoneNO"));
            array.put(jsonObject);
        }

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", uuid);
        params.put("conference_id", conference_id);
        params.put("no_of_seats", allUsersList.size());
        params.put("total_amount", totalAmount);
        params.put("order_id", randomNum);
        params.put("payment_status", paymentStatus);
        params.put("type_id", type_id);
        params.put("user_details", array);
        params.put("promocode_id", promocode_id);
        params.put("date_to_notify_user", date_to_notify_user);
        header.put("Cynapse", params);
        Log.d("headerBOB", header + "");

        new PostPaymentApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("JOBSPONSE", response.toString());

                    if (res_code.equals("1")) {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
                    } else {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
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

    private void conferenceBookSeatApi(String conference_id, String seat, String total_amount) throws JSONException {
//                {
//                    "Cynapse": {
//                    "uuid":"S3994",
//                            "medical_profile_id":"1",
//                            "job_title":"Doctor",
//                            "job_specilization_id":"2",
//                            "sub_specilization":"test",
//                            "location":"lucknow",
//                            "skill_required":"operation expert",
//                            "preffered_for":"lko",
//                            "ctc":"40000",
//                            "no_of_vaccancies":"10",
//                            "job_description" :"It hiering for doctor requierment"
//
//                }
//                }
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("seats", seat);
        params.put("amount", total_amount);
        //params.put("amount", txt_subtotal.getText().toString());

        Log.d("POSTSEATFRAAG", params + "");
        header.put("Cynapse", params);
        new ConferenceBookApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    Log.d("RESPONSEBOOK", response.toString());
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
                        //  startActivity(new Intent(WebViewActivity.this,MyConferencesActivity.class));
                        //finish();
                    } else {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
                        // finish();
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

    public void job_payment_api(final String job_id, final String plan_type_, String amount, final String payment_status, String order_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", job_id);
        params.put("order_id", order_id);
        params.put("amount", amount);
        params.put("job_type", plan_type_);
        params.put("payment_status", payment_status);
        params.put("promocode", AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.promo_code, ""));
        params.put("promocode_price", AppCustomPreferenceClass.readString(getApplicationContext(), AppCustomPreferenceClass.promo_price, ""));

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new job_payment_statusAPI(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    Log.d("Promo9899878", response.toString());
                    Log.d("plan_type_", plan_type_);

                    if (res_code.equals("1")) {
//                        if (plan_type_.equalsIgnoreCase("1")) {
//                            applyForRequestJobApi(job_id);
//                        } else {
//                            ShortListofJobPostApi(job_id);
//                        }


                    } else {

                        MyToast.toastLong(WebViewActivity.this, res_msg);
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

    public void Update_Package_StatusAPI(String tot_jobs, final String plan_type_, String medical_profile_id, final String job_title_id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("total_jobs", tot_jobs);
        params.put("plan_type", plan_type_);
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_id);

        // params.put("sync_time", AppCustomPreferenceClass.readString(RecommendedJobsActivity.this,AppCustomPreferenceClass.pay_plan_sync_time,""));

        header.put("Cynapse", params);
        new Update_Package_StatusAPI(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    Log.d("Promocode77777", response.toString());
                    Log.d("plan_type_", plan_type_);

                    if (res_code.equals("1")) {
                        if (plan_type_.equalsIgnoreCase("1")) {
                            applyForRequestJobApi(job_title_id);
                        } else {
                            ShortListofJobPostApi(job_title_id);
                        }


                    } else {

                        MyToast.toastLong(WebViewActivity.this, res_msg);
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

    private void applyForRequestJobApi(String job_title_ids) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(WebViewActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", job_id);
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_ids);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new applyForRequestJobApi(WebViewActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("appliedddd", response.toString());
                    if (res_code.equals("1")) {

                        // Intent intent = new Intent(CheckOutActivity.this, MyJobActivity.class);
//                        startActivity(intent);
//                        MyToast.toastLong(CheckOutActivity.this, res_msg);
//                        finish();
//                        //ActivityCompat.finishAffinity(CheckOutActivity.this);
//                        try {
//                            //   JobDetailsActivity.fas.finish();
//                            ////   Basic_Premimum_Activity.fa.finish();
//                            //JobDetailActivity.fass.finish();
//                            //   NotificationsActivity.noti.finish();
//                            //RecommendedJobsActivity.recommend_.finish();
//
//                        } catch (NullPointerException ne) {
//                            ne.printStackTrace();
//                        }
                        CustomPreference.writeString(WebViewActivity.this, CustomPreference.CheckAddKey, "6");
                        if (interstitialAd.isLoading() || interstitialAd.isLoaded()) {
                            interstitialAd.show();
//                                       Intent  intent=new Intent(this, AboutActivity.class);
                            showInterstitialAds();
                        } else {
                            finish();
                        }
                    } else {
                        MyToast.toastLong(WebViewActivity.this, res_msg);
                        finish();
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

    private void ShortListofJobPostApi(String job_title_ids) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(WebViewActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("job_id", job_id);
        params.put("medical_profile_id", medical_profile_id);
        params.put("title_id", job_title_ids);
        // params.put("uuid","S75f3");
        // params.put("sync_time", "");
        header.put("Cynapse", params);
        new ShortListofJobPostApi(WebViewActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    Log.d("shortlisttttt", response.toString());
                    if (res_code.equals("1")) {

//                        Intent intent = new Intent(CheckOutActivity.this, MyJobActivity.class);
//                        startActivity(intent);
//                        MyToast.toastLong(CheckOutActivity.this, res_msg);
////                        finish();
//                        try {
////                            JobDetailsActivity.fas.finish();
////                            Basic_Premimum_Activity.fa.finish();
////                          //  JobDetailActivity.fass.finish();
////                            NotificationsActivity.noti.finish();
////                            RecommendedJobsActivity.recommend_.finish();
//                            Intent intent = new Intent(CheckOutActivity.this, WebViewActivity.class);
//
//                            intent.putExtra("order_id",  randomNum.toString());
//                            intent.putExtra("job_id",job_id);
//                            intent.putExtra("job_bool",true);
//                            intent.putExtra("totalAmount",txt_totamnt.getText().toString());
//                            startActivity(intent);
//                            finish();
//                        } catch (NullPointerException ne) {
//                            ne.printStackTrace();
//                        }

                        CustomPreference.writeString(WebViewActivity.this, CustomPreference.CheckAddKey, "4");
                        // ActivityCompat.finishAffinity(CheckOutActivity.this);
                        if (interstitialAd.isLoaded() || interstitialAd.isLoading()) {
                            interstitialAd.show();
//                         Intent  intent = new Intent(this, AboutActivity.class);
                            showInterstitialAds();
                        } else {
                            finish();
                        }
                    } else {

                        MyToast.toastLong(WebViewActivity.this, res_msg);
                        finish();
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
