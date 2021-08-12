package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ImageShowAdapterConf;
import com.alcanzar.cynapse.adapter.SaveConferenceDetailsImageDisplayAdapter;
import com.alcanzar.cynapse.adapter.TicketsPackageAdapter;
import com.alcanzar.cynapse.adapter.TicketsPackageForgeinAdapter;
import com.alcanzar.cynapse.api.ChangeLikeApi;
import com.alcanzar.cynapse.api.ConferenceBookApi;
import com.alcanzar.cynapse.api.PostPaymentApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Constants;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.ServiceUtility;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class TicketDetailsNew extends AppCompatActivity implements OnMapReadyCallback {

    public double latitude = 0.0, longitude = 0.0;

    private GoogleMap mMap;
    private RecyclerView recycleView;
    private LinearLayoutManager linearLayoutManager;

    ImageView titleIcon, wishlist, share, img_broucher, btnBack, likedHeartImg;
    TextView title, conferenceTitle, conTpeTv, txt_frm_date, timeTv, discountTv, creaditPointsTv, eventHostTv1, eventHostTv2, eventHostTv3, keyNotesTv, freeevent, specialityTv, txt_medical_select,
            target_audience_speciality, target_audience_DepartmentTv2,confrenceDesTv;


    RecyclerView reclyclernormalticketspakages, reclyclerforeignticketspakages;
    private CardView discountCV;


    public ArrayList<ConferencePackageModel> conferencePackageModels = null;
    public ArrayList<ConferencePackageModelForgein> conferencePackageModelsForgein = null;

    ProgressBar tkt_progressBar;
    boolean showLike = false;

    String conference_id = "", conference_name = "", brochuers_file = "", event_host_name = "", venue = "", speciality = "",
            add_date = "", contact = "", from_date = "", from_time = "", to_date = "", to_time = "",
            registration_days = "", registration_fee = "", location = "",
            conference_type_id = "", credit_earnings = "", total_days_price = "", accomdation = "", member_concessions = "", student_concessions = "",
            price_hike_after_date = "", price_hike_after_percentage = "",
            broucher_image = "", available_seat = "",
            event_sponcer = "", conference_description = "", payment_mode = "";
    String changeLike = "", medical_profile_id = "", booking_stopped;


    ArrayList aList;

    Button ticketsbook;
//    RecyclerView.LayoutManager LayoutManagernormal , LayoutManagerforeign;

    DatabaseHelper handler;
    HashMap<String, String> hashMap = new HashMap<>();

    private static ViewPager mPager;
    private static int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details_new);

        initialize();
        initSlider(aList);
        setOnClickListener();
    }

    private void setOnClickListener() {

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hashMap.clear();

                if (showLike) {
                    wishlist.setVisibility(View.VISIBLE);
                    likedHeartImg.setVisibility(View.GONE);
                    Log.d("232323", conference_id);
                    changeLike = "0";
                    showLike = false;
                    hashMap.put("show_like", changeLike);

                } else {
                    wishlist.setVisibility(View.GONE);
                    likedHeartImg.setVisibility(View.VISIBLE);
                    Log.d("242424", conference_id);
                    changeLike = "1";
                    showLike = true;
                    hashMap.put("show_like", changeLike);
                }

                try {
                    ChangeLikeApi(conference_id, changeLike);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        likedHeartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hashMap.clear();

                if (showLike) {
                    wishlist.setVisibility(View.VISIBLE);
                    likedHeartImg.setVisibility(View.GONE);
                    Log.d("232323", conference_id);
                    changeLike = "0";
                    showLike = false;
                    hashMap.put("show_like", changeLike);

                } else {
                    wishlist.setVisibility(View.GONE);
                    likedHeartImg.setVisibility(View.VISIBLE);
                    Log.d("242424", conference_id);
                    changeLike = "1";
                    showLike = true;
                    hashMap.put("show_like", changeLike);
                }

                try {
                    ChangeLikeApi(conference_id, changeLike);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareDetails();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ticketsbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ticketsbook.setEnabled(false);

                if (Util.isVerifiedProfile(TicketDetailsNew.this))
                {
                    if (AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase(""))
                    {
                        //b = false;
                        //showDialog(activity);

                        try {
                            if (Util.isVerifiyEMailPHoneNO((Activity) TicketDetailsNew.this)) {
                                openDialog();
                            }
                            else
                            {
                                ticketsbook.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        openDialog();
                    }
                }
                else
                {
                    ticketsbook.setEnabled(true);
                }
            }
        });
    }
//7007279338
    private void openDialog() {
        final Dialog dialog = new Dialog(TicketDetailsNew.this);
        dialog.setContentView(R.layout.alert_dialog);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);

        TextView messageTv = dialog.findViewById(R.id.messageTv);
        Button okBtn = dialog.findViewById(R.id.okBtn);
        okBtn.setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.btnContainerLl).setVisibility(View.GONE);

        if (freeevent.getText().toString().equals("Free Event")) {
//            messageTv.setText("This is Free Event");
//            dialog.show();
            if (Integer.parseInt(available_seat) < 1) {
                MyToast.toastLong(TicketDetailsNew.this, "Available Seats are " + available_seat + " " + "\n" + "Booking Seats should be less than or equal to Available Seats");
            } else {
                try {
                    PostPaymentTicketApi("1", "4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (freeevent.getText().toString().equals("AtVenue")) {
            if (Integer.parseInt(available_seat) < 1) {
                MyToast.toastLong(TicketDetailsNew.this, "Available Seats are " + available_seat + " " + "\n" + "Booking Seats should be less than or equal to Available Seats");
            } else {
                try {
                    PostPaymentTicketApi("1", "4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            messageTv.setText("Booking available at Venue only");
//            dialog.show();

        } else {

            bookSeatPopUp();
            //startActivity(new Intent(TicketDetailsNew.this, ReviewBookingNewPay.class));
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void PostPaymentTicketApi(String type_id, String paymentStatus) throws JSONException {

        JSONArray array = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.name, ""));
        jsonObject.put("email", AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.email, ""));
        jsonObject.put("phone_no", AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.phoneNumber, ""));
        array.put(jsonObject);


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", conference_id);
        params.put("no_of_seats", "1");
        params.put("total_amount", "0.00");
        params.put("order_id", ServiceUtility.randInt(0, 9999999));
        params.put("payment_status", paymentStatus);
        params.put("type_id", type_id);
        params.put("user_details", array);
        params.put("promocode_id", "");
        params.put("date_to_notify_user", "");
        header.put("Cynapse", params);
        Log.d("headerBOB", header + "");

        new PostPaymentApi(this, header,true) {
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
                       // MyToast.toastLong(TicketDetailsNew.this, res_msg);
                        conferenceBookSeatApi(conference_id, "1", "0.00");
                    } else {
                        MyToast.toastLong(TicketDetailsNew.this, res_msg);
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
                try
                {
                    JSONObject header = response.getJSONObject("Cynapse");
                    Log.d("RESPONSEBOOK", response.toString());
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");

                    if (res_code.equals("1"))
                    {
                        //MyToast.toastLong(TicketDetailsNew.this, res_msg);
                        final Dialog dialog = new Dialog(TicketDetailsNew.this);
                        dialog.setContentView(R.layout.alert_dialog);
                        dialog.setCanceledOnTouchOutside(false);
                        //TODO: used to make the background transparent
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.CENTER);
                        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);

                        TextView messageTv = dialog.findViewById(R.id.messageTv);
                        Button okBtn = dialog.findViewById(R.id.okBtn);
                        okBtn.setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.btnContainerLl).setVisibility(View.GONE);
                        messageTv.setText("Your registration for this event has been done successfully");

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                dialog.dismiss();
                                Intent intent = new Intent(TicketDetailsNew.this, MainActivity.class);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(TicketDetailsNew.this);
                            }
                        });

                        dialog.show();
                        //  startActivity(new Intent(WebViewActivity.this,MyConferencesActivity.class));
                        //finish();
                    } else {
                        MyToast.toastLong(TicketDetailsNew.this, res_msg);
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

    private void bookSeatPopUp() {
        //TODO :opening the success pop up
        final Dialog dialog = new Dialog(this, Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ticket_two);
        //TODO: used to make the background transparent
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TODO : initializing dfferent views for the dialog
        ImageView close = dialog.findViewById(R.id.close);
        final EditText txt_seat = dialog.findViewById(R.id.txt_seat);

//        TextView seatOne = dialog.findViewById(R.id.seatOne);
//        TextView seatTwo = dialog.findViewById(R.id.seatTwo);
//        TextView seatThree = dialog.findViewById(R.id.seatThree);
//        TextView seatFour = dialog.findViewById(R.id.seatFour);
//        TextView seatFive = dialog.findViewById(R.id.seatFive);
//        TextView seatSix = dialog.findViewById(R.id.seatSix);
//        TextView seatSeven = dialog.findViewById(R.id.seatSeven);
//        TextView seatEight = dialog.findViewById(R.id.seatEight);
//        TextView seatNine = dialog.findViewById(R.id.seatNine);
//        TextView seatTen = dialog.findViewById(R.id.seatTen);
//        seatOne.setOnClickListener(this);
//        seatTwo.setOnClickListener(this);
//        seatThree.setOnClickListener(this);
//        seatFour.setOnClickListener(this);
//        seatFive.setOnClickListener(this);
//
//        seatSix.setOnClickListener(this);
//        seatSeven.setOnClickListener(this);
//        seatEight.setOnClickListener(this);
//        seatNine.setOnClickListener(this);
//        seatTen.setOnClickListener(this);

        Button btnSelect = dialog.findViewById(R.id.btnSelectSeat);
        dialog.show();

        //TODO : dismiss the on btn click and close click
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txt_seat.getText().toString().length() == 0) {
                    MyToast.toastShort(TicketDetailsNew.this, getString(R.string.enter_the_seat));
                    return;
                }

                if (Integer.parseInt(txt_seat.getText().toString()) > Integer.parseInt(available_seat)) {
                    MyToast.toastLong(TicketDetailsNew.this, "Available Seats are " + available_seat + " " + "\n" + "Booking Seats should be less than or equal to Available Seats");
                    txt_seat.setText("");
                    txt_seat.requestFocus();
                    return;
                }

                Intent it = new Intent(TicketDetailsNew.this, ReviewBookingNew.class);
                it.putExtra("totalSits", txt_seat.getText().toString());
                it.putExtra("conferenceID", conference_id);
                it.putExtra("discount_percentage", getIntent().getStringExtra("discount_percentage"));
                it.putExtra("discount_description", getIntent().getStringExtra("discount_description"));
                it.putExtra("conference_name", getIntent().getStringExtra("conference_name"));
                it.putExtra("address", getIntent().getStringExtra("venue"));
                it.putExtra("gst", getIntent().getStringExtra("gst"));
                it.putExtra("price_hike_after_date", price_hike_after_date);
                it.putExtra("price_hike_after_percentage", price_hike_after_percentage);
                it.putExtra("from_date", from_date);
                it.putExtra("to_date", to_date);
                it.putExtra("from_time", from_time);
                startActivity(it);
                dialog.dismiss();
                finish();
//                try {
//                    SeatAvailability(txt_seat.getText().toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
    }

    private void ChangeLikeApi(String s, final String status) throws JSONException {

        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", "1");

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);

        new ChangeLikeApi(TicketDetailsNew.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
                    if (res_code.equals("1")) {

                        handler.updateData(hashMap, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);

                        if (status.equals("1")) {
                            wishlist.setVisibility(View.GONE);
                            likedHeartImg.setVisibility(View.VISIBLE);
                        } else {
                            wishlist.setVisibility(View.VISIBLE);
                            likedHeartImg.setVisibility(View.GONE);
                        }
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

    private void initialize() {

        handler = new DatabaseHelper(this);

        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        title = findViewById(R.id.title);
        title.setText("Health Care Conference");
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
        mPager = findViewById(R.id.pager);
        discountCV = findViewById(R.id.discountCV);
        specialityTv = findViewById(R.id.specialityTv);
        confrenceDesTv = findViewById(R.id.confrenceDesTv);

        txt_medical_select = findViewById(R.id.txt_medical_select);
        target_audience_speciality = findViewById(R.id.target_audience_speciality);
        target_audience_DepartmentTv2 = findViewById(R.id.target_audience_DepartmentTv2);

        linearLayoutManager = new LinearLayoutManager(TicketDetailsNew.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        recycleView = findViewById(R.id.recycleView);
//        recycleView.setLayoutManager(linearLayoutManager);

        pdfView = findViewById(R.id.pdfView);
        tkt_progressBar = findViewById(R.id.tkt_progressBar);


        if (mMap == null) {

            ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMapAsync(this);
//            mapFragment.getMapAsync(this);

        }


        reclyclernormalticketspakages = findViewById(R.id.recycle_normal_package);
        reclyclernormalticketspakages.setLayoutManager(new LinearLayoutManager(this));
        reclyclernormalticketspakages.setHasFixedSize(true);
        reclyclernormalticketspakages.setNestedScrollingEnabled(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reclyclernormalticketspakages.getContext(), DividerItemDecoration.HORIZONTAL);
        reclyclernormalticketspakages.addItemDecoration(dividerItemDecoration);
        reclyclernormalticketspakages.setItemAnimator(new DefaultItemAnimator());
        ticketsbook = findViewById(R.id.ticketsbook);

        reclyclerforeignticketspakages = findViewById(R.id.recycle_foreign_package);
        reclyclerforeignticketspakages.setLayoutManager(new LinearLayoutManager(this));
        reclyclerforeignticketspakages.setHasFixedSize(true);
        reclyclerforeignticketspakages.setNestedScrollingEnabled(false);
        reclyclerforeignticketspakages.setItemAnimator(new DefaultItemAnimator());


        latitude = 26.8467;
        longitude = 80.9462;


        if (getIntent() != null) {

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
            booking_stopped = getIntent().getStringExtra("booking_stopped");
            price_hike_after_percentage = getIntent().getStringExtra("price_hike_after_percentage");
            price_hike_after_date = getIntent().getStringExtra("price_hike_after_date");

            String medical_profile_name = getIntent().getStringExtra("medical_profile_name");
            speciality = getIntent().getStringExtra("speciality_name");
            String department_id = getIntent().getStringExtra("department_id");
            String department_name = getIntent().getStringExtra("department_name");
            conference_description = getIntent().getStringExtra("conference_description");

            txt_medical_select.setText(medical_profile_name);
            target_audience_speciality.setText(speciality);
            target_audience_DepartmentTv2.setText(department_name);
            confrenceDesTv.setText(conference_description);

            conferenceTitle.setText(conference_name);
            timeTv.setText(getIntent().getStringExtra("from_time") + " - " + getIntent().getStringExtra("to_time"));
            txt_frm_date.setText(getIntent().getStringExtra("from_date") + " - " + getIntent().getStringExtra("to_date"));
            discountTv.setText(getIntent().getStringExtra("discount_percentage") + "% " + getIntent().getStringExtra("discount_description"));
            creaditPointsTv.setText(getIntent().getStringExtra("credit_earnings"));
            eventHostTv1.setText(event_host_name);
            keyNotesTv.setText(getIntent().getStringExtra("keynote_speakers"));

            changeLike = getIntent().getStringExtra("like_status");
            available_seat = getIntent().getStringExtra("available_seat");

            //speciality = getIntent().getStringExtra("speciality_name");
            //specialityTv.setText(speciality);

            //displayFile(brochuers_file);
            aList = new ArrayList(Arrays.asList(brochuers_file.split(",")));

            if (TextUtils.isEmpty(getIntent().getStringExtra("discount_percentage"))) {
                discountCV.setVisibility(View.GONE);
            }

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

            if (booking_stopped.equals("1")) {
                ticketsbook.setVisibility(View.GONE);
            }
            else if (Integer.parseInt(available_seat) < 1) {
                ticketsbook.setVisibility(View.GONE);
            }

            try {
//            latitude = Double.parseDouble(AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.Latitude, ""));
//            longitude = Double.parseDouble(AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.Longitude, ""));

                latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
                longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

                Log.d("Latitiude", latitude + "");
                Log.d("LOGITUDEEE", longitude + "");
            } catch (Exception e) {
                e.printStackTrace();
            }


            conferencePackageModels = handler.getNormalPackDetails(getIntent().getStringExtra("conference_id"));
            conferencePackageModelsForgein = handler.getForgeingPackDetails(getIntent().getStringExtra("conference_id"));


            if (conferencePackageModels.size() < 1) {
                findViewById(R.id.normalPackCV).setVisibility(View.GONE);
            }

            if (conferencePackageModelsForgein.size() < 1) {
                findViewById(R.id.packageForgeinCV).setVisibility(View.GONE);
            }

            reclyclernormalticketspakages.setAdapter(new TicketsPackageAdapter(this, conferencePackageModels, false));
            reclyclerforeignticketspakages.setAdapter(new TicketsPackageForgeinAdapter(this, conferencePackageModelsForgein, false));
        }
    }

    private void initSlider(final ArrayList<String> images) {

        mPager.setAdapter(new SaveConferenceDetailsImageDisplayAdapter(TicketDetailsNew.this, images));

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

    private void displayFile(String brochuers_fileStr) {
        try {

            aList = new ArrayList(Arrays.asList(brochuers_fileStr.split(",")));

            Log.d("BROCHURESTICKETWILL", aList + "");

            ImageShowAdapterConf requestPostJobAdapter = new ImageShowAdapterConf(this, R.layout.image_row, aList, pdfView, tkt_progressBar, conference_id);
            recycleView.setAdapter(requestPostJobAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addTicketsData() {

//        tickectsnumber.add(modal.setTicketnumber("1"));
//        tickectsname.add();
//        tickectsprice.add();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        googleMap.getUiSettings().setScrollGesturesEnabled(false);

        if (ActivityCompat.checkSelfPermission(TicketDetailsNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TicketDetailsNew.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TicketDetailsNew.this,
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

    PDFView pdfView;

}
