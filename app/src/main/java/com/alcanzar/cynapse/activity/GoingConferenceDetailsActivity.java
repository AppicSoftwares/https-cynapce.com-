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

public class GoingConferenceDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO : header views
    TextView title, txt_frm_date, txt_t_date, txt_fm_time, txt_t_time,
            date, time, guestName, degree, locationDetails, conferenceTitle,txt_total_pck_chrge, txt_no_of_seat, txt_conctact_num, txt_host_name, txt_regs_day, txt_charg, txt_conctact, locationAdres, txt_regs_price;
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
    double packCharge = 0.0;
    boolean showLike = false;

    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManagerCharge;
    RecyclerView recycleView, recycleViewCharge;
    TextView txt_consession_member,txt_medi_stud_dis,txt_payment_mode,txt_accomd_charge;
    //String changLike = "";

    String conference_type_id = "", credit_earnings = "", total_days_price = "", accomdation = "", member_concessions = "", student_concessions = "",
            price_hike_after_date = "", price_hike_after_percentage = "",
            broucher_image = "", payment_mode = "", medical_profile_id = "", conference_description = "";
    String conference_id = "", conference_name = "", brochuers_file = "", event_host_name = "", venue = "", speciality = "",
            add_date = "", contact = "", from_date = "", from_time = "", registration_days = "", registration_fee = "", location = "",
            brochures_charge = "", brochures_days = "", event_sponcer = "",
            particular_country_id = "", cost = "", duration = "", available_seat = "",
            particular_country_name = "", particular_state_id = "", particular_state_name = "", status = "", modify_date = "", to_date = "", to_time = "", like_status = "";
    static String changeLike = "";
    HashMap<String, String> hashMap = new HashMap<>();
    DatabaseHelper handler;
    ArrayList aList;
    PDFView pdfView;
    ArrayList<ConferencePackageModel> conferencePackageList = new ArrayList<>();
    ProgressBar tkt_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going_article_details);
        linearLayoutManager = new LinearLayoutManager(GoingConferenceDetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        linearLayoutManagerCharge = new LinearLayoutManager(GoingConferenceDetailsActivity.this);
        linearLayoutManagerCharge.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleViewCharge = findViewById(R.id.recycleViewCharge);
        recycleViewCharge.setLayoutManager(linearLayoutManagerCharge);
        txt_consession_member=findViewById(R.id.txt_consession_member);
        txt_medi_stud_dis=findViewById(R.id.txt_medi_stud_dis);
        txt_payment_mode=findViewById(R.id.txt_payment_mode);
        pdfView = findViewById(R.id.pdfView);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleIcon = findViewById(R.id.titleIcon);
        tkt_progressBar=findViewById(R.id.tkt_progressBar);
        txt_total_pck_chrge=findViewById(R.id.txt_total_pck_chrge);
        txt_accomd_charge=findViewById(R.id.txt_accomd_charge);
        share = findViewById(R.id.share);
        txt_conctact = findViewById(R.id.txt_conctact);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        title = findViewById(R.id.title);
        btnEdit = findViewById(R.id.btnEdit);
        locationAdres = findViewById(R.id.locationAdres);
        conferenceTitle = findViewById(R.id.conferenceTitle);
        txt_no_of_seat = findViewById(R.id.txt_no_of_seat);
        txt_conctact_num = findViewById(R.id.txt_conctact_num);
        txt_host_name = findViewById(R.id.txt_host_name);
        guestName = findViewById(R.id.guestName);
        txt_fm_time = findViewById(R.id.txt_fm_time);
        txt_t_date = findViewById(R.id.txt_t_date);
        txt_frm_date = findViewById(R.id.txt_frm_date);
        txt_t_time = findViewById(R.id.txt_t_time);
        degree = findViewById(R.id.degree);
        wishlist = findViewById(R.id.wishlist);
        txt_regs_price = findViewById(R.id.txt_regs_price);
        txt_charg = findViewById(R.id.txt_charg);
        locationDetails = findViewById(R.id.locationDetails);
        btnBookReg = findViewById(R.id.btnBookReg);
        btnEdit.setVisibility(View.GONE);
        btnEdit.setOnClickListener(this);
        //  wishlist.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        handler = new DatabaseHelper(this);
        share.setOnClickListener(this);
        try {
            latitude = Double.parseDouble(AppCustomPreferenceClass.readString(GoingConferenceDetailsActivity.this, AppCustomPreferenceClass.Latitude, ""));
            longitude = Double.parseDouble(AppCustomPreferenceClass.readString(GoingConferenceDetailsActivity.this, AppCustomPreferenceClass.Longitude, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Latitiude", latitude + "");
        Log.d("LOGITUDEEE", longitude + "");

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
            brochuers_file = getIntent().getStringExtra("brochuers_file");

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
            changeLike = getIntent().getStringExtra("changeLike");
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

        Log.d("SHOWLIKEKE", showLike + "");
        aList = new ArrayList(Arrays.asList(brochuers_file.split(",")));
        initSlider(aList);



        try {
            //pdfImage = handler.getImage(DatabaseHelper.TABLE_CONFERENCES_SHOW_IMAGES, conference_id);
            Log.d("BROCHURESTICKETWILL", member_concessions + "");

            ImageShowAdapterConf requestPostJobAdapter = new ImageShowAdapterConf(this, R.layout.image_row, aList, pdfView,tkt_progressBar,conference_id);
            recycleView.setAdapter(requestPostJobAdapter);

//            } else {
//
//            }

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
        txt_total_pck_chrge.setText(total_days_price);

        try {
            conferencePackageList = handler.getConfSavePackCharge(DatabaseHelper.TABLE_GOING_CONFERENCES_PACK_CHARGE, conference_id);
            Log.d("CONFERPACKAELISTTT",conferencePackageList.size()+"");
            if (conferencePackageList.size() > 0) {
                for (int i = 0; i < conferencePackageList.size(); i++) {
                    Log.d("Packagechageee",conferencePackageList.get(i).getConference_pack_charge());
                    packCharge = packCharge + Double.parseDouble(conferencePackageList.get(i).getConference_pack_charge());

                }

                ConferenceChargeAdapter requestPostchrgAdapter = new ConferenceChargeAdapter(this, R.layout.pack_charg, conferencePackageList);
                recycleViewCharge.setAdapter(requestPostchrgAdapter);
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (payment_mode.equalsIgnoreCase("1")) {
            txt_payment_mode.setText("Online");
        } else {

            txt_payment_mode.setText("AtVenue");
        }

//        txt_payment_mode.setText(payment_mode);


        guestName.setText(event_sponcer);
        degree.setText(speciality);
        locationDetails.setText(venue);
//        txt_regs_price.setText(registration_fee);
//        txt_charg.setText(registration_fee);
        Log.d("BROUDCHIFLE", brochuers_file);
        txt_conctact_num.setText(contact);
        locationAdres.setText(location);
        txt_frm_date.setText(from_date);
        txt_t_date.setText(to_date);
        txt_fm_time.setText(from_time);
        txt_t_time.setText(to_time);
        txt_no_of_seat.setText(available_seat);
        txt_host_name.setText(event_host_name);
        txt_accomd_charge.setText(accomdation);


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



        getLocation();

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showLike) {
                    wishlist.setImageResource(R.drawable.ic_like);

                    changeLike = "1";
                    showLike = false;
                } else {
                    wishlist.setImageResource(R.drawable.heart_icon);

                    changeLike = "0";
                    showLike = true;
                }

                hashMap.put("show_like", changeLike);
                Log.d("CHANGELIKE", changeLike);

                // handler.updateData(hashMap, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);

                try {
                    ChangeLikeApi(conference_id, changeLike);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        Log.d("2323233333", changeLike);
        if (changeLike.equalsIgnoreCase("1")) {

            Log.d("232323333344", changeLike);
            wishlist.setImageResource(R.drawable.ic_like);
        } else {
            Log.d("2323233333", "changeLike");
            wishlist.setImageResource(R.drawable.heart_icon);
        }

//        if (showLike) {
//            wishlist.setImageResource(R.drawable.heart_icon);
//            // showLike = false;
//
//        } else {
//            wishlist.setImageResource(R.drawable.ic_like);
//
//            //showLike = true;
//
//        }

        btnBookReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO  : opening the tickets pop up
                //  bookSeatPopUp();
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
                dialog.dismiss();
                Log.d("SEATITITI", String.valueOf(x));
                //TODO : opening new review activity


                Intent it = new Intent(GoingConferenceDetailsActivity.this, ReviewBookingActivity.class);
                it.putExtra("seat", txt_seat.getText().toString());
                it.putExtra("conference_id", conference_id);
                it.putExtra("conference_name", conference_name);
                it.putExtra("event_host_name", event_host_name);
                it.putExtra("speciality", speciality);
                it.putExtra("contact", contact);
                it.putExtra("registration_fee", registration_fee);
                it.putExtra("location", location);
                it.putExtra("from_date", from_date);
                it.putExtra("from_time", from_time);
                // startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    private void initSlider(final ArrayList<String> images) {


        mPager.setAdapter(new MyConferenceAdapter(GoingConferenceDetailsActivity.this, images));

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
//                ConferenceDetailsModel model = new ConferenceDetailsModel();
//                model.setShow_like(changeLike);
//                Log.d("CHAGNDELIKEEXX", changeLike);
//
//                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id, conference_id)) {
//
//                    handler.AddGoingConferenceDetails(model, true);
//
//                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//                } else {
//                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
//                    handler.AddGoingConferenceDetails(model, false);
//                }
                HashMap<String, String> hashMap1 = new HashMap<>();

                hashMap1.put("show_like", changeLike);
                handler.updateData(hashMap1, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);
                Intent intent = new Intent(GoingConferenceDetailsActivity.this, MyConferencesActivity.class);
                intent.putExtra("changeLike", "2");
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;

            case R.id.btnEdit:

                Intent its = new Intent(GoingConferenceDetailsActivity.this, AddConference.class);
                its.putExtra("conference_id", conference_id);
                its.putExtra("conference_name", conference_name);
                its.putExtra("event_host_name", event_host_name);
                its.putExtra("speciality", speciality);
                its.putExtra("contact", contact);
                its.putExtra("registration_fee", registration_fee);
                its.putExtra("location", location);
                its.putExtra("from_date", from_date);
                its.putExtra("from_time", from_time);
                its.putExtra("venue", venue);
                its.putExtra("add_date", add_date);
                its.putExtra("registration_days", registration_days);
                its.putExtra("particular_country_id", particular_country_id);
                its.putExtra("particular_country_name", particular_country_name);
                its.putExtra("particular_state_id", particular_state_id);
                its.putExtra("particular_state_name", particular_state_name);
                its.putExtra("brochures_charge", brochures_charge);
                its.putExtra("brochures_days", brochures_days);
                its.putExtra("status", status);
                its.putExtra("modify_date", modify_date);
                its.putExtra("to_date", to_date);
                its.putExtra("to_time", to_time);
                its.putExtra("show_details", true);
                its.putExtra("cost", cost);
                its.putExtra("duration", duration);
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
                Intent it = new Intent(GoingConferenceDetailsActivity.this, ShowImageTicket.class);
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
                        try {
                            buildAlertMessageNoGpsCnf();
                            show = false;
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

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
                        try {
                            buildAlertMessageNoGpsCnf();
                            show = false;
                        }catch (Exception e)
                        {
                           e.printStackTrace();
                        }

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
        params.put("uuid", AppCustomPreferenceClass.readString(GoingConferenceDetailsActivity.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", status);

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeLikeApi(GoingConferenceDetailsActivity.this, header) {
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

        HashMap<String, String> hashMap1 = new HashMap<>();

        hashMap1.put("show_like", changeLike);
        handler.updateData(hashMap1, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS);
//        ConferenceDetailsModel model = new ConferenceDetailsModel();
//        model.setShow_like(changeLike);
//        Log.d("CHAGNDELIKEEXX", changeLike);
//
//        if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_GOING_CONFERENCES_SHOW_DETAILS, DatabaseHelper.conference_id, conference_id)) {
//
//            handler.AddGoingConferenceDetails(model, true);
//
//            //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
//        } else {
//            //   Log.e("UPDATED", true + " " + model.getProduct_id());
//            handler.AddGoingConferenceDetails(model, false);
//        }
        Intent intent = new Intent(GoingConferenceDetailsActivity.this, MyConferencesActivity.class);
        intent.putExtra("changeLike", "2");
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
import com.alcanzar.cynapse.adapter.ConferenceChargeAdapter;
import com.alcanzar.cynapse.adapter.ImageShowAdapterConf;
import com.alcanzar.cynapse.adapter.MyConferenceAdapter;
import com.alcanzar.cynapse.adapter.SaveConferenceDetailsImageDisplayAdapter;
import com.alcanzar.cynapse.adapter.TicketsPackageAdapter;
import com.alcanzar.cynapse.adapter.TicketsPackageForgeinAdapter;
import com.alcanzar.cynapse.api.ChangeLikeApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.MapFragment;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;
import com.alcanzar.cynapse.model.PackageSavedConferenceModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Constants;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Utils;
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

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.ProviderError;
import fr.quentinklein.slt.TrackerSettings;
import me.relex.circleindicator.CircleIndicator;

public class GoingConferenceDetailsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    public double latitude = 0.0, longitude = 0.0;

    private GoogleMap mMap;
    private RecyclerView recycleView;
    private LinearLayoutManager linearLayoutManager;

    ImageView titleIcon, wishlist, share, img_broucher, btnBack, likedHeartImg;
    TextView title, conferenceTitle, conTpeTv, txt_frm_date, timeTv, discountTv, creaditPointsTv, eventHostTv1, eventHostTv2, eventHostTv3, keyNotesTv, freeevent,
            txt_medical_select, target_audience_speciality, target_audience_DepartmentTv2;
    RecyclerView reclyclernormalticketspakages, reclyclerforeignticketspakages;


    public ArrayList<ConferencePackageModel> conferencePackageModels = null;
    public ArrayList<ConferencePackageModelForgein> conferencePackageModelsForgein = null;

    ProgressBar tkt_progressBar;
    boolean showLike = false;

    String conference_id = "", conference_name = "", brochuers_file = "", event_host_name = "", venue = "", speciality = "",
            add_date = "", contact = "", from_date = "", from_time = "", to_date = "", to_time = "",
            registration_days = "", registration_fee = "", location = "",
            conference_type_id = "", credit_earnings = "", total_days_price = "", accomdation = "", member_concessions = "", student_concessions = "",
            price_hike_after_date = "", price_hike_after_percentage = "", changLike = "",
            broucher_image = "", available_seat = "",
            event_sponcer = "", conference_description = "", payment_mode = "";
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
        setContentView(R.layout.activity_going_article_details);

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

        activity = GoingConferenceDetailsActivity.this;

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

        txt_medical_select = findViewById(R.id.txt_medical_select);
        target_audience_speciality = findViewById(R.id.target_audience_speciality);
        target_audience_DepartmentTv2 = findViewById(R.id.target_audience_DepartmentTv2);

        mPager = findViewById(R.id.pager);

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

                timeTv.setText(from_time + " - " + to_time);
                txt_frm_date.setText(from_date + " - " + to_date);
                discountTv.setText(getIntent().getStringExtra("discount_percentage") + "% " + getIntent().getStringExtra("discount_description"));
                creaditPointsTv.setText(getIntent().getStringExtra("credit_earnings"));
                eventHostTv1.setText(event_host_name);
                keyNotesTv.setText(getIntent().getStringExtra("keynote_speakers"));
                changeLike = getIntent().getStringExtra("like_status");
                available_seat = getIntent().getStringExtra("available_seat");
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

                conferencePackageModels = handler.getPackageGoingConference(conference_id);
                conferencePackageModelsForgein = handler.getForgeinPackageGoingConference(conference_id);

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
                Utils.sop("exxcex" + e.getMessage());
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
            case R.id.wishlist:
                if (showLike) {
                    likedHeartImg.setVisibility(View.VISIBLE);
                    wishlist.setVisibility(View.GONE);
                    //wishlist.setImageResource(R.drawable.ic_like);
                    changLike = "1";
                    showLike = false;
                } else {
                    //wishlist.setImageResource(R.drawable.heart_icon);
                    likedHeartImg.setVisibility(View.GONE);
                    wishlist.setVisibility(View.VISIBLE);
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