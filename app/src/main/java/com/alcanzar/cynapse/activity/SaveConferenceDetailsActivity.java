/*
package com.alcanzar.cynapse.activity;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.alcanzar.cynapse.adapter.ImageShowAdapterConf;
import com.alcanzar.cynapse.adapter.MyConferenceAdapter;
import com.alcanzar.cynapse.api.ChangeLikeApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.MapFragment;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;
import com.github.barteksc.pdfviewer.PDFView;

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

public class SaveConferenceDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO : header views
    TextView title, txt_frm_date, txt_t_date, txt_fm_time, txt_t_time,
            date, time, guestName, degree, locationDetails, conferenceTitle,
            txt_regs_day, txt_charg, txt_conctact, locationAdres, txt_conctact_num,
            txt_total_pck_chrge, txt_regs_price, txt_accomd_pck_chrge, txt_host_name, txt_no_of_seat, txt_payment_mode, txt_accom_charg;
    ImageView btnBack, titleIcon, img_broucher, wishlist;
    Button btnBookReg, btnEdit;
    LocationTracker tracker;
    TrackerSettings settings;
    static String x = "0";
    private static ViewPager mPager;
    private static int currentPage = 0;
    boolean mapShow = false, seatShow = false;
    public static final int REQUEST_PLACE_PICKER = 1;
    ImageView share;
    public double latitude = 0.0, longitude = 0.0;
    public double current_latitude, current_longitude;
    Fragment myf;
    boolean showLike = false;
    String changLike = "";
    double packCharge = 0.0;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManagerCharge;
    RecyclerView recycleView, recycleViewCharge;
    String conference_id = "", conference_name = "", brochuers_file = "", event_host_name = "", venue = "", speciality = "",
            add_date = "", contact = "", from_date = "", from_time = "", registration_days = "", registration_fee = "", location = "",
            brochures_charge = "", brochures_days = "", event_sponcer = "",
            particular_country_id = "", cost = "", duration = "", available_seat = "",
            particular_country_name = "", particular_state_id = "", particular_state_name = "", status = "", modify_date = "", to_date = "", to_time = "";

    String conference_type_id = "", credit_earnings = "", total_days_price = "", accomdation = "", member_concessions = "", student_concessions = "",
            price_hike_after_date = "", price_hike_after_percentage = "",
            broucher_image = "", payment_mode = "", medical_profile_id = "", conference_description = "";
    HashMap<String, String> hashMap = new HashMap<>();
    TextView txt_consession_member, txt_medi_stud_dis;
    DatabaseHelper handler;
    ArrayList aList;
    ArrayList<ConferencePackageModel> conferencePackageList = new ArrayList<>();
    String showBrocherName = "";
    PDFView pdfView;
    ProgressBar tkt_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details_new);
        setContentView(R.layout.activity_save_article_details);
        txt_total_pck_chrge = findViewById(R.id.txt_total_pck_chrge);
        linearLayoutManager = new LinearLayoutManager(SaveConferenceDetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManagerCharge = new LinearLayoutManager(SaveConferenceDetailsActivity.this);
        linearLayoutManagerCharge.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleViewCharge = findViewById(R.id.recycleViewCharge);
        recycleViewCharge.setLayoutManager(linearLayoutManagerCharge);
        pdfView = findViewById(R.id.pdfView);
        txt_accom_charg = findViewById(R.id.txt_accom_charg);
        txt_consession_member = findViewById(R.id.txt_consession_member);
        txt_medi_stud_dis = findViewById(R.id.txt_medi_stud_dis);
        txt_no_of_seat = findViewById(R.id.txt_no_of_seat);
        txt_accomd_pck_chrge = findViewById(R.id.txt_accomd_pck_chrge);
        txt_payment_mode = findViewById(R.id.txt_payment_mode);
        txt_host_name = findViewById(R.id.txt_host_name);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        share = findViewById(R.id.share);
        txt_conctact_num = findViewById(R.id.txt_conctact_num);
        txt_conctact = findViewById(R.id.txt_conctact);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        tkt_progressBar = findViewById(R.id.tkt_progressBar);
        title = findViewById(R.id.title);
        btnEdit = findViewById(R.id.btnEdit);
        locationAdres = findViewById(R.id.locationAdres);
        conferenceTitle = findViewById(R.id.conferenceTitle);
        date = findViewById(R.id.date);
        guestName = findViewById(R.id.guestName);
        time = findViewById(R.id.time);
        degree = findViewById(R.id.degree);
        wishlist = findViewById(R.id.wishlist);
        txt_regs_price = findViewById(R.id.txt_regs_price);
        txt_charg = findViewById(R.id.txt_charg);
        locationDetails = findViewById(R.id.locationDetails);
        btnBookReg = findViewById(R.id.btnBookReg);
        txt_fm_time = findViewById(R.id.txt_fm_time);
        txt_t_date = findViewById(R.id.txt_t_date);
        txt_frm_date = findViewById(R.id.txt_frm_date);
        txt_t_time = findViewById(R.id.txt_t_time);
        btnEdit.setVisibility(View.GONE);
        btnEdit.setOnClickListener(this);
        //  wishlist.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        handler = new DatabaseHelper(this);
        share.setOnClickListener(this);




        try {
            latitude = Double.parseDouble(AppCustomPreferenceClass.readString(SaveConferenceDetailsActivity.this, AppCustomPreferenceClass.Latitude, ""));
            longitude = Double.parseDouble(AppCustomPreferenceClass.readString(SaveConferenceDetailsActivity.this, AppCustomPreferenceClass.Longitude, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getIntent() != null) {
            conference_id = getIntent().getStringExtra("conference_id");
            conference_name = getIntent().getStringExtra("conference_name");
            event_host_name = getIntent().getStringExtra("event_host_name");
            venue = getIntent().getStringExtra("venue");
            speciality = getIntent().getStringExtra("speciality");
            add_date = getIntent().getStringExtra("add_date");
            contact = getIntent().getStringExtra("contact");
            from_date = getIntent().getStringExtra("from_date");
            from_time = getIntent().getStringExtra("from_time");
//          registration_days = getIntent().getStringExtra("registration_days");
//          registration_fee = getIntent().getStringExtra("registration_fee");
            brochuers_file = getIntent().getStringExtra("brochuers_file");
//          brochures_charge = getIntent().getStringExtra("brochures_charge");
//          brochures_days = getIntent().getStringExtra("brochures_days");
            event_sponcer = getIntent().getStringExtra("event_sponcer");
            particular_country_id = getIntent().getStringExtra("particular_country_id");
            particular_country_name = getIntent().getStringExtra("particular_country_name");
            particular_state_id = getIntent().getStringExtra("particular_state_id");
            particular_state_name = getIntent().getStringExtra("particular_state_name");
            status = getIntent().getStringExtra("status");
            modify_date = getIntent().getStringExtra("modify_date");
            to_date = getIntent().getStringExtra("to_date");
            to_time = getIntent().getStringExtra("to_time");
            location = getIntent().getStringExtra("location");
            cost = getIntent().getStringExtra("cost");
            duration = getIntent().getStringExtra("duration");
            showLike = getIntent().getBooleanExtra("showLike", false);
            available_seat = getIntent().getStringExtra("available_seat");
            medical_profile_id = getIntent().getStringExtra("medical_profile_id");
            conference_description = getIntent().getStringExtra("conference_description");
            conference_type_id = getIntent().getStringExtra("conference_type_id");
            credit_earnings = getIntent().getStringExtra("credit_earnings");
            total_days_price = getIntent().getStringExtra("total_days_price");
            accomdation = getIntent().getStringExtra("accomdation");
            member_concessions = getIntent().getStringExtra("member_concessions");
            student_concessions = getIntent().getStringExtra("student_concessions");
            price_hike_after_date = getIntent().getStringExtra("price_hike_after_date");
            price_hike_after_percentage = getIntent().getStringExtra("price_hike_after_percentage");
            payment_mode = getIntent().getStringExtra("payment_mode");

        }

        if(payment_mode.equalsIgnoreCase("2"))
        {
            btnBookReg.setVisibility(View.GONE);
        }else {

            btnBookReg.setVisibility(View.VISIBLE);
        }
        Log.d("VENUEEE", venue);
        aList = new ArrayList(Arrays.asList(brochuers_file.split(",")));


        initSlider(aList);

        try {

            Log.d("BROCHURESTICKETWILL", aList + "");

            ImageShowAdapterConf requestPostJobAdapter = new ImageShowAdapterConf(this, R.layout.image_row, aList, pdfView, tkt_progressBar,conference_id);
            recycleView.setAdapter(requestPostJobAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        txt_total_pck_chrge.setText(total_days_price);
        try {
            conferencePackageList = handler.getConfSavePackCharge(DatabaseHelper.TABLE_SAVE_CONFERENCES_PACK_CHARGE, conference_id);
            if (conferencePackageList.size() > 0) {
                for (int i = 0; i < conferencePackageList.size(); i++) {
                    packCharge = packCharge + Double.parseDouble(conferencePackageList.get(i).getConference_pack_charge());

                }

                ConferenceChargeAdapter requestPostchrgAdapter = new ConferenceChargeAdapter(this, R.layout.pack_charg, conferencePackageList);
                recycleViewCharge.setAdapter(requestPostchrgAdapter);
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        for (int i = 0; i < aList.size(); i++) {
            System.out.println("KKKKKKK-->" + aList.get(i));
        }

        if (!mapShow && latitude != 0.0) {
            mapShow = true;
            myf = new MapFragment();
            Bundle b = new Bundle();
            b.putDouble("lat", latitude);
            b.putDouble("log", longitude);
            myf.setArguments(b);
            fragment(myf);
        }

        title.setText(conference_name);
        if (conference_type_id.equalsIgnoreCase("1")) {
            conferenceTitle.setText(conference_name + "(" + "" + "Conference" + "" + ")");//conference name with type----

        } else if (conference_type_id.equalsIgnoreCase("2")) {
            conferenceTitle.setText(conference_name + "(" + "" + "Exhibition" + "" + ")");

        } else if (conference_type_id.equalsIgnoreCase("3")) {
            conferenceTitle.setText(conference_name + "(" + "" + "CME" + "" + ")");

        } else if (conference_type_id.equalsIgnoreCase("4")) {
            conferenceTitle.setText(conference_name + "(" + "" + "Training Courses" + "" + ")");

        } else if (conference_type_id.equalsIgnoreCase("5")) {
            conferenceTitle.setText(conference_name + "(" + "" + "Seminar" + "" + ")");


        } else if (conference_type_id.equalsIgnoreCase("other_conference_type")) {

            conferenceTitle.setText(conference_name + "(" + "" + "Other" + "" + ")");

        }
        guestName.setText(event_sponcer);
        degree.setText(speciality);
        locationDetails.setText(venue);
        if(member_concessions.equalsIgnoreCase(""))
        {
            txt_consession_member.setText("0");
        }else {

            txt_consession_member.setText(member_concessions);
        }

        if(student_concessions.equalsIgnoreCase(""))
        {
            txt_medi_stud_dis.setText("0");
        }else {
            txt_medi_stud_dis.setText(student_concessions);

        }


//        txt_regs_price.setText(registration_fee);
//        txt_charg.setText(registration_fee);
        Log.d("BROUDCHIFLE", brochuers_file);
        txt_no_of_seat.setText(available_seat);
        txt_host_name.setText(event_host_name);
        txt_conctact_num.setText(contact);
        locationAdres.setText(location);
        txt_frm_date.setText(from_date);
        txt_t_date.setText(to_date);
        txt_fm_time.setText(from_time);
        txt_t_time.setText(to_time);
        txt_accom_charg.setText(accomdation);

        getLocation();

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showLike) {
                    wishlist.setImageResource(R.drawable.ic_like);

                    changLike = "1";
                    showLike = false;
                } else {
                    wishlist.setImageResource(R.drawable.heart_icon);

                    changLike = "0";
                    showLike = true;
                }
                Log.d("CHANGELIKE", changLike);
                hashMap.put("show_like", changLike);
                Log.d("CHANGELIKE", changLike);

                handler.updateData(hashMap, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
                try {
                    ChangeLikeApi(conference_id, changLike);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        if (showLike) {
            wishlist.setImageResource(R.drawable.heart_icon);
            // showLike = false;

        } else {
            wishlist.setImageResource(R.drawable.ic_like);

            //showLike = true;

        }

        if (payment_mode.equalsIgnoreCase("1")) {
            txt_payment_mode.setText("Online");
        } else {

            txt_payment_mode.setText("AtVenue");
        }

        btnBookReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO  : opening the tickets pop up
                bookSeatPopUp();
            }
        });
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

                try
                {
                    if (available_seat.equalsIgnoreCase(txt_seat.getText().toString())) {

//                    int total = Integer.parseInt(registration_fee) * Integer.parseInt(txt_seat.getText().toString());
//                    String money = String.valueOf(total);
//                    Log.d("TICKETMONEYYY111", money);
//                    launchPayUMoneyFlow(money);
//                    Intent it = new Intent(TicketDetails.this, ConferencePaymentActivity.class);
                        Intent it = new Intent(SaveConferenceDetailsActivity.this, ReviewBooking.class);
                        it.putExtra("seat", txt_seat.getText().toString());
                        it.putExtra("conference_id", conference_id);
                        it.putExtra("total_days_price", total_days_price);
                        it.putExtra("accomdation", accomdation);

                        it.putExtra("price_hike_after_date", price_hike_after_date);
                        it.putExtra("price_hike_after_percentage", price_hike_after_percentage);
                        it.putExtra("savebool", true);
                        it.putExtra("medical_profile_id", medical_profile_id);
                        it.putExtra("member_concessions", member_concessions);
                        it.putExtra("student_concessions", student_concessions);
//
                    it.putExtra("payment_mode", payment_mode);
//                    it.putExtra("from_date", from_date);
//                    it.putExtra("from_time", from_time);
//                    it.putExtra("to_date", to_date);
//                    it.putExtra("to_time", to_time);
                        // startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        startActivity(it);


                    } else if (Integer.parseInt(available_seat) - Integer.parseInt(txt_seat.getText().toString()) > 0) {

//                    launchPayUMoneyFlow(money);
                        Intent it = new Intent(SaveConferenceDetailsActivity.this, ReviewBooking.class);
                        it.putExtra("seat", txt_seat.getText().toString());
                        it.putExtra("conference_id", conference_id);
                        it.putExtra("total_days_price", total_days_price);
                        it.putExtra("accomdation", accomdation);
                        it.putExtra("savebool", true);

                        it.putExtra("price_hike_after_date", price_hike_after_date);
                        it.putExtra("price_hike_after_percentage", price_hike_after_percentage);
                        it.putExtra("medical_profile_id", medical_profile_id);
                        it.putExtra("member_concessions", member_concessions);
                        it.putExtra("student_concessions", student_concessions);
                        // Intent it = new Intent(TicketDetails.this, ConferencePaymentActivity.class);
                        it.putExtra("payment_mode", payment_mode);
//                    it.putExtra("conference_id", conference_id);
//                    it.putExtra("conference_name", conference_name);
//                    it.putExtra("event_host_name", event_host_name);
//                    it.putExtra("speciality", speciality);
//                    it.putExtra("contact", contact);
//
//                    it.putExtra("location", venue);
//                    it.putExtra("from_date", from_date);
//                    it.putExtra("from_time", from_time);
//                    it.putExtra("to_date", to_date);
//                    it.putExtra("to_time", to_time);
                        // startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        startActivity(it);


                    } else if (Integer.parseInt(txt_seat.getText().toString()) - Integer.parseInt(available_seat) > 0) {
                        Log.d("SEATITITI", "333333");
                        MyToast.toastLong(SaveConferenceDetailsActivity.this, "Selected no. of seat is not available !");
                    }else if(available_seat.equalsIgnoreCase("0"))
                    {
                        MyToast.toastLong(SaveConferenceDetailsActivity.this, "No. of seat is not available for booking !");

                    }
                    dialog.dismiss();
                    finish();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

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

    private void initSlider(final ArrayList<String> images) {


        mPager.setAdapter(new MyConferenceAdapter(SaveConferenceDetailsActivity.this, images));

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
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
        }, 7000, 25000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent(SaveConferenceDetailsActivity.this, MyConferencesActivity.class);
                intent.putExtra("changeLike", "3");
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;

            case R.id.btnEdit:

                Intent its = new Intent(SaveConferenceDetailsActivity.this, AddConference.class);
                its.putExtra("conference_id", conference_id);
                its.putExtra("conference_name", conference_name);
                its.putExtra("event_host_name", event_host_name);
                its.putExtra("speciality", speciality);
                its.putExtra("contact", contact);
//                its.putExtra("registration_fee", registration_fee);
                its.putExtra("location", location);
                its.putExtra("from_date", from_date);
                its.putExtra("from_time", from_time);
                its.putExtra("venue", venue);
                its.putExtra("add_date", add_date);
//                its.putExtra("registration_days", registration_days);
                its.putExtra("particular_country_id", particular_country_id);
                its.putExtra("particular_country_name", particular_country_name);
                its.putExtra("particular_state_id", particular_state_id);
                its.putExtra("particular_state_name", particular_state_name);
//                its.putExtra("brochures_charge", brochures_charge);
//                its.putExtra("brochures_days", brochures_days);
                its.putExtra("status", status);
                its.putExtra("modify_date", modify_date);
                its.putExtra("to_date", to_date);
                its.putExtra("to_time", to_time);
                its.putExtra("show_details", true);
//                its.putExtra("cost", cost);
//                its.putExtra("duration", duration);
                its.putExtra("event_sponcer", event_sponcer);
                startActivity(its);
                break;

            case R.id.share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, conference_name);
                startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));

//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                Uri screenshotUri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + "ID OF IMAGES");
//
//                sharingIntent.setType("image/jpeg");
//                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                break;
            case R.id.img_broucher:
                Intent it = new Intent(SaveConferenceDetailsActivity.this, ShowImageTicket.class);
                it.putExtra("URL", brochuers_file);
                startActivity(it);
                break;

            default:
                break;
        }
    }

    void fragment(Fragment myf) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.map, myf);
        ft.commitAllowingStateLoss();
    }

    boolean show = true;

    void getLocation() {

        settings = new TrackerSettings()
                .setUseGPS(true)
                .setUseNetwork(false)
                .setUsePassive(true)
                .setTimeBetweenUpdates(30 * 60 * 1000)
                .setMetersBetweenUpdates(500);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    11);

        } else {
            tracker = new LocationTracker(this, settings) {
                @Override
                public void onLocationFound(@NonNull Location location) {

                    Log.e("LOCATION", "Lat" + location.getLatitude() + " log " + location.getLongitude());

                    if (!mapShow && latitude == 0.0) {
                        mapShow = true;
                        myf = new MapFragment();
                        Bundle b = new Bundle();
                        b.putDouble("lat", location.getLatitude());
                        b.putDouble("log", location.getLongitude());
                        myf.setArguments(b);
                        fragment(myf);
                    }

                    current_latitude = location.getLatitude();
                    current_longitude = location.getLongitude();

                }

                @Override
                public void onTimeout() {

                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    super.onProviderDisabled(provider);
                    if (show) {
                        buildAlertMessageNoGpsCnf();
                        show = false;
                    }
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    super.onProviderEnabled(provider);

                }

                @Override
                public void onProviderError(@NonNull ProviderError providerError) {
                    super.onProviderError(providerError);
                    if (show) {
                        buildAlertMessageNoGpsCnf();
                        show = false;
                    }
                }
            };
            tracker.startListening();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 11:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    Log.d("===granted", "granted");
                    //getLocation();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myf.onActivityResult(requestCode, resultCode, data);
    }

    private void buildAlertMessageNoGpsCnf() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location related activities might not be working, please allow gps to access your location")
                .setCancelable(false)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                        show = true;
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void ChangeLikeApi(String s, String status) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(SaveConferenceDetailsActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", status);

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeLikeApi(SaveConferenceDetailsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
                    if (res_code.equals("1")) {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SaveConferenceDetailsActivity.this, MyConferencesActivity.class);
        intent.putExtra("changeLike", "3");
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        //  AppCustomPreferenceClass.writeString(TicketDetails.this, AppCustomPreferenceClass.like_change, changLike);
    }
}
*/

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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class SaveConferenceDetailsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    public double latitude = 0.0, longitude = 0.0;

    private GoogleMap mMap;
    private RecyclerView recycleView;
    private LinearLayoutManager linearLayoutManager;


    ImageView titleIcon, wishlist, share, img_broucher, btnBack, likedHeartImg;
    TextView title, conferenceTitle, conTpeTv, txt_frm_date, timeTv, discountTv, creaditPointsTv, eventHostTv1, eventHostTv2, eventHostTv3, keyNotesTv, freeevent,txt_medical_select
            ,target_audience_speciality,target_audience_DepartmentTv2;
    RecyclerView reclyclernormalticketspakages, reclyclerforeignticketspakages;


    public ArrayList<ConferencePackageModel> conferencePackageModels = null;
    public ArrayList<ConferencePackageModelForgein> conferencePackageModelsForgein = null;

    ProgressBar tkt_progressBar;
    boolean showLike = false;

    String conference_id = "", conference_name = "", brochuers_file = "", event_host_name = "", venue = "", speciality = "",
            add_date = "", contact = "", from_date = "", from_time = "", to_date = "", to_time = "",
            registration_days = "", registration_fee = "", location = "",
            conference_type_id = "", credit_earnings = "", total_days_price = "", accomdation = "", member_concessions = "", student_concessions = "",
            price_hike_after_date = "", price_hike_after_percentage = "",
            broucher_image = "", available_seat = "",booking_stopped="",
            event_sponcer = "", conference_description = "", payment_mode = "", changLike = "";
    String changeLike = "", medical_profile_id = "", medical_profile_name = "", special_name = "", department_id = "", department_name = "";


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
        setContentView(R.layout.save_conference_layout);

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

        activity = SaveConferenceDetailsActivity.this;

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

        txt_medical_select = findViewById(R.id.txt_medical_select);
        target_audience_speciality = findViewById(R.id.target_audience_speciality);
        target_audience_DepartmentTv2 = findViewById(R.id.target_audience_DepartmentTv2);

        /*init setOnClickListener*/
        ticketsbook.setOnClickListener(this);
        share.setOnClickListener(this);
        wishlist.setOnClickListener(this);
        likedHeartImg.setOnClickListener(this);
        /*end of  setOnClickListener*/


        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        recycleView = findViewById(R.id.recycleView);
//        recycleView.setLayoutManager(linearLayoutManager);


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
                medical_profile_name = getIntent().getStringExtra("medical_profile_name");
                special_name = getIntent().getStringExtra("special_name");
                department_id = getIntent().getStringExtra("department_id");
                department_name = getIntent().getStringExtra("department_name");
                booking_stopped = getIntent().getStringExtra("booking_stopped");


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

                changeLike = getIntent().getStringExtra("like_status");
                available_seat = getIntent().getStringExtra("available_seat");



                if (booking_stopped.equals("1")) {
                    ticketsbook.setVisibility(View.GONE);
                }
                else if (Integer.parseInt(available_seat) < 1) {
                    ticketsbook.setVisibility(View.GONE);
                }

                //displayFile(brochuers_file);

                if (TextUtils.isEmpty(getIntent().getStringExtra("discount_percentage"))) {
                    findViewById(R.id.discountCv).setVisibility(View.GONE);
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


//            latitude = Double.parseDouble(AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.Latitude, ""));
//            longitude = Double.parseDouble(AppCustomPreferenceClass.readString(TicketDetailsNew.this, AppCustomPreferenceClass.Longitude, ""));

                latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
                longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

                Log.d("Latitiude", latitude + "");
                Log.d("LOGITUDEEE", longitude + "");


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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.wishlist:
//                if (showLike) {
//                    //wishlist.setImageResource(R.drawable.ic_like);
//                    //wishlist.setColorFilter(ContextCompat.getColor(activity, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
////                    likedHeartImg.setVisibility(View.VISIBLE);
////                    wishlist.setVisibility(View.GONE);
//                    changLike = "1";
//                    showLike = false;
//                } else {
//                    //wishlist.setImageResource(R.drawable.heart_icon);
//                    //wishlist.setImageResource(R.drawable.ic_like);
////                    likedHeartImg.setVisibility(View.GONE);
////                    wishlist.setVisibility(View.VISIBLE);
//                    changLike = "0";
//                    showLike = true;
//                }

                changLike = "1";

                hashMap.put("show_like", changLike);
               //Log.d("CHANGELIKE", changLike);

                try {
                    ChangeLikeApi(conference_id, changLike);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.likedHeartImg:
//                if (showLike) {
//                    //wishlist.setImageResource(R.drawable.ic_like);
//                    //wishlist.setColorFilter(ContextCompat.getColor(activity, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
//                    changLike = "1";
//                    showLike = false;
//                } else {
//                    //wishlist.setImageResource(R.drawable.heart_icon);
//                    //wishlist.setImageResource(R.drawable.ic_like);
//
//                    changLike = "0";
//                    showLike = true;
//                }
                changLike = "0";
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

            case R.id.ticketsbook:
                ticketsbook.setEnabled(false);
                if (Util.isVerifiedProfile(activity)) {
                    if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.email_verified, "").equalsIgnoreCase("0") ||
                            AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.phoneNumber, "").equalsIgnoreCase("")) {
                        //b = false;
                        //showDialog(activity);
                        try {
                            if (Util.isVerifiyEMailPHoneNO(activity)) {
                                showDialog();
                            }
                            else
                            {
                                ticketsbook.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        showDialog();
                    }
                }
                else {
                    ticketsbook.setEnabled(true);
                }

                break;
        }
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(activity);
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
                MyToast.toastLong(activity, "Available Seats are " + available_seat + " " + "\n" + "Booking Seats should be less than or equal to Available Seats");
            } else {
                try {
                    PostPaymentTicketApi("1", "4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else if (freeevent.getText().toString().equals("AtVenue")) {
//            messageTv.setText("Booking should be available at Vanue only");
//            dialog.show();
            if (Integer.parseInt(available_seat) < 1) {
                MyToast.toastLong(activity, "Available Seats are " + available_seat + " " + "\n" + "Booking Seats should be less than or equal to Available Seats");
            } else {
                try {
                    PostPaymentTicketApi("1", "4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            bookSeatPopUp();
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
        jsonObject.put("name", AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.name, ""));
        jsonObject.put("email", AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.email, ""));
        jsonObject.put("phone_no", AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.phoneNumber, ""));
        array.put(jsonObject);


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.UserId, ""));
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
                        MyToast.toastLong(activity, res_msg);
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
                        final Dialog dialog = new Dialog(activity);
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
                                Intent intent = new Intent(activity, MainActivity.class);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(activity);
                            }
                        });

                        dialog.show();
                        //  startActivity(new Intent(WebViewActivity.this,MyConferencesActivity.class));
                        //finish();
                    } else {
                        MyToast.toastLong(activity, res_msg);
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
                    MyToast.toastShort(activity, getString(R.string.enter_the_seat));
                    return;
                }

                if (available_seat.equals("")) {
                    MyToast.toastLong(activity, "Seat is not available");
                    return;
                }

                if (Integer.parseInt(txt_seat.getText().toString()) > Integer.parseInt(available_seat)) {
                    MyToast.toastLong(activity, "Available Seats are " + available_seat + " " + "\n" + "Booking Seats should be less than or equal to Available Seats");
                    txt_seat.setText("");
                    txt_seat.requestFocus();
                    return;
                }

                Intent it = new Intent(activity, ReviewBookingNew.class);
                it.putExtra("totalSits", txt_seat.getText().toString());
                it.putExtra("conferenceID", conference_id);
                it.putExtra("discount_percentage", getIntent().getStringExtra("discount_percentage"));
                it.putExtra("discount_description", getIntent().getStringExtra("discount_description"));
                it.putExtra("conference_name", getIntent().getStringExtra("conference_name"));
                it.putExtra("address", getIntent().getStringExtra("venue"));
                it.putExtra("gst", getIntent().getStringExtra("gst"));
                it.putExtra("from_date", from_date);
                it.putExtra("to_date", to_date);
                it.putExtra("from_time", from_time);
                startActivity(it);
                finish();
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
        params.put("uuid", AppCustomPreferenceClass.readString(SaveConferenceDetailsActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", "1");
        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeLikeApi(SaveConferenceDetailsActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSENOT", response.toString());
                    if (res_code.equals("1"))
                    {
                        if (res_msg.equals("Conference DisLike Successfully."))
                        {
                            likedHeartImg.setVisibility(View.GONE);
                            wishlist.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            likedHeartImg.setVisibility(View.VISIBLE);
                            wishlist.setVisibility(View.GONE);
                        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void initSlider(final ArrayList<String> images) {

        mPager.setAdapter(new SaveConferenceDetailsImageDisplayAdapter(SaveConferenceDetailsActivity.this, images));

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
