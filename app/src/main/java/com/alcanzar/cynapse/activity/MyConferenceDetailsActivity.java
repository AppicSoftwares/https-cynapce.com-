package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ConferenceChargeAdapter;
import com.alcanzar.cynapse.adapter.MyConferenceAdapter;
import com.alcanzar.cynapse.adapter.PdfDelConf;
import com.alcanzar.cynapse.adapter.SaveConferenceDetailsImageDisplayAdapter;
import com.alcanzar.cynapse.adapter.TicketsPackageAdapter;
import com.alcanzar.cynapse.adapter.TicketsPackageAdapter2;
import com.alcanzar.cynapse.adapter.TicketsPackageForgeinAdapter;
import com.alcanzar.cynapse.adapter.TicketsPackageForgeinAdapter2;
import com.alcanzar.cynapse.api.ChangeLikeApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.MapFragment;
import com.alcanzar.cynapse.model.AddForgeinPackageMYConferenceModel;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;
import com.alcanzar.cynapse.model.ImageModel;
import com.alcanzar.cynapse.model.PackageSavedConferenceModel;
import com.alcanzar.cynapse.model.PdfDelModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Constants;
import com.android.volley.VolleyError;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.ProviderError;
import fr.quentinklein.slt.TrackerSettings;
import me.relex.circleindicator.CircleIndicator;

public class MyConferenceDetailsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {


    public double latitude = 0.0, longitude = 0.0;

    private GoogleMap mMap;
    private RecyclerView recycleView;
    private LinearLayoutManager linearLayoutManager;
    private CardView normalPackCV;
    private CardView packageForgeinCV;

    ImageView titleIcon, wishlist, share, img_broucher, btnBack, likedHeartImg;
    TextView title, conferenceTitle, conTpeTv, txt_frm_date, timeTv, discountTv, creaditPointsTv, eventHostTv1, eventHostTv2,
            eventHostTv3, keyNotesTv, freeevent, txt_medical_select, target_audience_speciality, target_audience_DepartmentTv2;

    RecyclerView reclyclernormalticketspakages, reclyclerforeignticketspakages;


    public ArrayList<PackageSavedConferenceModel> conferencePackageModels;
    public ArrayList<AddForgeinPackageMYConferenceModel> conferencePackageModelsForgein = new ArrayList<>();

    ProgressBar tkt_progressBar;
    boolean showLike = false;

    String conference_id = "", conference_name = "", brochuers_file = "", event_host_name = "", venue = "", speciality = "",
            add_date = "", contact = "", from_date = "", from_time = "", to_date = "", to_time = "",
            registration_days = "", registration_fee = "", location = "",
            conference_type_id = "", credit_earnings = "", total_days_price = "", accomdation = "", member_concessions = "", student_concessions = "",
            price_hike_after_date = "", price_hike_after_percentage = "",
            broucher_image = "", available_seat = "",
            event_sponcer = "", conference_description = "", payment_mode = "", changLike = "";
    String changeLike = "", medical_profile_id = "";


    ArrayList aList;

    Button ticketsbook;
//    RecyclerView.LayoutManager LayoutManagernormal , LayoutManagerforeign;

    DatabaseHelper handler;
    HashMap<String, String> hashMap = new HashMap<>();

    Activity activity;
    private static ViewPager mPager;
    private static int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_article_details);

        initialize();
        initSlider(aList);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void initialize() {

        activity = MyConferenceDetailsActivity.this;

        handler = new DatabaseHelper(this);

        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        title = findViewById(R.id.title);

        btnBack = findViewById(R.id.btnBack);
        conferenceTitle = findViewById(R.id.conferenceTitle);
        conTpeTv = findViewById(R.id.conTpeTv);
        txt_frm_date = findViewById(R.id.txt_frm_date);
        timeTv = findViewById(R.id.timeTv);
        discountTv = findViewById(R.id.discountTv);
        creaditPointsTv = findViewById(R.id.creaditPointsTv);
        eventHostTv1 = findViewById(R.id.eventHostTv1);
        eventHostTv2 = findViewById(R.id.eventHostTv2);
        eventHostTv3 = findViewById(R.id.eventHostTv3);
        keyNotesTv = findViewById(R.id.keyNotesTv);
        wishlist = findViewById(R.id.wishlist);
        freeevent = findViewById(R.id.freeevent);
        share = findViewById(R.id.share);
        img_broucher = findViewById(R.id.img_broucher);
        likedHeartImg = findViewById(R.id.likedHeartImg);
        ticketsbook = findViewById(R.id.ticketsbook);
        mPager = findViewById(R.id.pager);
        normalPackCV = findViewById(R.id.normalPackCV);
        packageForgeinCV = findViewById(R.id.packageForgeinCV);

        txt_medical_select = findViewById(R.id.txt_medical_select);
        target_audience_speciality = findViewById(R.id.target_audience_speciality);
        target_audience_DepartmentTv2 = findViewById(R.id.target_audience_DepartmentTv2);

        /*init setOnClickListener*/
        ticketsbook.setOnClickListener(this);
        share.setOnClickListener(this);
        wishlist.setOnClickListener(this);
        /*end of  setOnClickListener*/


        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        recycleView = findViewById(R.id.recycleView);
//        recycleView.setLayoutManager(linearLayoutManager);


        tkt_progressBar = findViewById(R.id.tkt_progressBar);

        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }


        reclyclernormalticketspakages = findViewById(R.id.recycle_normal_package);
        reclyclernormalticketspakages.setLayoutManager(new LinearLayoutManager(this));
        reclyclernormalticketspakages.setHasFixedSize(true);
        reclyclernormalticketspakages.setNestedScrollingEnabled(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reclyclernormalticketspakages.getContext(), DividerItemDecoration.HORIZONTAL);
        reclyclernormalticketspakages.addItemDecoration(dividerItemDecoration);
        reclyclernormalticketspakages.setItemAnimator(new DefaultItemAnimator());


        reclyclerforeignticketspakages = findViewById(R.id.recycle_foreign_package);
        reclyclerforeignticketspakages.setLayoutManager(new LinearLayoutManager(this));
        reclyclerforeignticketspakages.setHasFixedSize(true);
        reclyclerforeignticketspakages.setNestedScrollingEnabled(false);
        reclyclerforeignticketspakages.setItemAnimator(new DefaultItemAnimator());

        latitude = 26.8467;
        longitude = 80.9462;

        if (getIntent() != null) {

            try {
                conference_id = getIntent().getStringExtra("conference_id");
                conference_name = getIntent().getStringExtra("conference_name");
                conTpeTv.setText(getIntent().getStringExtra("conference_type_name"));
                brochuers_file = getIntent().getStringExtra("brochuers_file");
                from_date = getIntent().getStringExtra("from_date");
                from_time = getIntent().getStringExtra("from_time");
                to_date = getIntent().getStringExtra("to_date");
                to_time = getIntent().getStringExtra("to_time");
                venue = getIntent().getStringExtra("venue");
                event_host_name = getIntent().getStringExtra("event_host_name");

                String medical_profile_name = getIntent().getStringExtra("medical_profile_name");
                String special_name = getIntent().getStringExtra("special_name");
                String department_id = getIntent().getStringExtra("department_id");
                String department_name = getIntent().getStringExtra("department_name");

                txt_medical_select.setText(medical_profile_name);
                target_audience_speciality.setText(special_name);
                target_audience_DepartmentTv2.setText(department_name);


                if (conference_name.length() > 30)
                    title.setText(conference_name.substring(0,30) + "...");
                else
                    title.setText(conference_name);

                conferenceTitle.setText(conference_name);

                aList = new ArrayList(Arrays.asList(brochuers_file.split(",")));

                timeTv.setText(getIntent().getStringExtra("from_time") + " - " + getIntent().getStringExtra("to_time"));
                txt_frm_date.setText(getIntent().getStringExtra("from_date") + " - " + getIntent().getStringExtra("to_date"));
                discountTv.setText(getIntent().getStringExtra("discount_percentage") + "% " + getIntent().getStringExtra("discount_description"));
                creaditPointsTv.setText(getIntent().getStringExtra("credit_earnings"));
                eventHostTv1.setText(event_host_name);
                keyNotesTv.setText(getIntent().getStringExtra("keynote_speakers"));
                available_seat = getIntent().getStringExtra("available_seat");


                if (TextUtils.isEmpty(getIntent().getStringExtra("discount_percentage"))) {
                    findViewById(R.id.discountCv).setVisibility(View.GONE);
                }

                if (getIntent().hasExtra("like_status")) {
                    changeLike = getIntent().getStringExtra("like_status");
                }
                //displayFile(brochuers_file);

                if (changeLike.equalsIgnoreCase("1")) {
                    wishlist.setVisibility(View.GONE);
                    likedHeartImg.setVisibility(View.VISIBLE);
                    showLike = true;
                } else {
                    wishlist.setVisibility(View.VISIBLE);
                    likedHeartImg.setVisibility(View.GONE);
                    showLike = false;
                }

                if (getIntent().getStringExtra("payment_mode").equals("1")) {
                    freeevent.setText("Online");
                } else if (getIntent().getStringExtra("payment_mode").equals("2")) {
                    freeevent.setText("AtVenue");
                } else if (getIntent().getStringExtra("payment_mode").equals("3")) {
                    freeevent.setText("Free Event");
                }

//            latitude = Double.parseDouble(AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.Latitude, ""));
//            longitude = Double.parseDouble(AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.Longitude, ""));

                latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
                longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

                Log.d("Latitiude", latitude + "");
                Log.d("LOGITUDEEE", longitude + "");


                conferencePackageModels = handler.getPackageMYConference(getIntent().getStringExtra("conference_id"));
                conferencePackageModelsForgein = handler.getForgeinPackageMyConference(getIntent().getStringExtra("conference_id"));

                try {
                    if (conferencePackageModels.size() < 1) {
                        normalPackCV.setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {
                    normalPackCV.setVisibility(View.GONE);
                }

                try {
                    if (conferencePackageModelsForgein.size() < 1) {
                        findViewById(R.id.packageForgeinCV).setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {
                    findViewById(R.id.packageForgeinCV).setVisibility(View.GONE);
                }


                reclyclernormalticketspakages.setAdapter(new TicketsPackageAdapter2(this, conferencePackageModels, false));
                reclyclerforeignticketspakages.setAdapter(new TicketsPackageForgeinAdapter2(this, conferencePackageModelsForgein, false));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void ChangeLikeApi(String s, String status) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", "1");

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeLikeApi(activity, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
                    if (res_code.equals("1")) {
                        handler.updateData(hashMap, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
                        // MyToast.toastLong((Activity) context, res_msg);
                    } else {
                        // MyToast.toastLong(context, res_msg);
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

    private void initSlider(final ArrayList<String> images) {

        mPager.setAdapter(new SaveConferenceDetailsImageDisplayAdapter(activity, images));

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, Constants.imgDelay, Constants.imgPeriod);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticketsbook:
                Intent intent = new Intent(activity, MyConferenceDetails2.class);
                intent.putExtra("conference_id", conference_id);
                startActivity(intent);
                break;

            case R.id.wishlist:
                if (showLike) {
                    wishlist.setImageResource(R.drawable.ic_like);
                    changLike = "1";
                    showLike = false;
                } else {
                    wishlist.setImageResource(R.drawable.heart_icon);
                    changLike = "1";
                    showLike = true;
                }
                Log.d("CHANGELIKE", changLike);
                hashMap.put("show_like", changLike);
                Log.d("CHANGELIKE", changLike);

                try {
                    ChangeLikeApi(conference_id, changLike);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.share:
                shareDetails();
                break;
        }
    }

    private void shareDetails() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Conference Name: " + conference_name +
                "\n\n" + "Conference Type:" + conTpeTv.getText().toString()
                + "\n\n" + "Conference Date:" + from_date + "-" + to_date
                + "\n\n" + "Conference Time:" + from_time + "-" + to_time
                + "\n\n" + "Conference Venue:" + venue
                + "\n\n" + "Keynotes Speakers:" + keyNotesTv.getText().toString() + "\n\n"
                + "App Download Links:" + "\n"
                + Constants.playStoreUrl + "\n\n" + Constants.appStoreUrl);
        startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        googleMap.getUiSettings().setScrollGesturesEnabled(false);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    11);
            return;
        }

        mMap.setMyLocationEnabled(true);
        LatLng user = new LatLng(latitude, longitude);
        Log.e("maplatlng", latitude + "" + longitude);
        googleMap.addMarker(new MarkerOptions().position(user));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
        CameraUpdate updateZoom = CameraUpdateFactory.newLatLngZoom(user, 15);
        mMap.animateCamera(updateZoom);

    }

}
