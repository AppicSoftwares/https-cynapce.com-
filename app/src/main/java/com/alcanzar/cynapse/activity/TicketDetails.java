package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ConferenceChargeAdapter;
import com.alcanzar.cynapse.adapter.ImageShowAdapter;
import com.alcanzar.cynapse.adapter.ImageShowAdapterConf;
import com.alcanzar.cynapse.adapter.MyAdapter;

import com.alcanzar.cynapse.adapter.TicketsConferenceAdapter;
import com.alcanzar.cynapse.api.ChangeLikeApi;
import com.alcanzar.cynapse.app.AppController;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.MapFragment;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ImageModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.AppEnvironment;
import com.alcanzar.cynapse.utils.MyToast;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.barteksc.pdfviewer.PDFView;
//import com.payumoney.core.PayUmoneyConfig;
//import com.payumoney.core.PayUmoneyConstants;
//import com.payumoney.core.PayUmoneySdkInitializer;
//import com.payumoney.sdkui.ui.utils.PPConfig;
//import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.ProviderError;
import fr.quentinklein.slt.TrackerSettings;
import me.relex.circleindicator.CircleIndicator;

public class TicketDetails extends AppCompatActivity implements View.OnClickListener {
    //TODO : header views
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManagerCharge;
    RelativeLayout lnr_get_pdf;
    RecyclerView recycleView, recycleViewCharge;
    TextView title,
            date, time, guestName, degree, locationDetails, conferenceTitle, txt_frm_date, txt_t_date, txt_fm_time, txt_t_time,
            txt_regs_day, txt_charg, txt_conctact, locationAdres, txt_brouchername,
            txt_no_of_seat, txt_host_name, txt_accomd_pck_chrge, txt_payment_mode, txt_consession_member, txt_medi_stud_dis;
    ImageView btnBack, titleIcon, img_broucher, wishlist;
   // private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    Button btnBookReg;
    LocationTracker tracker;
    TrackerSettings settings;
    static String x = "0";
    boolean showLike = false;
    String root = "";
    private File imgFile;
    Context context = this;
    AppCustomPreferenceClass mAppPreference;
    ProgressDialog mProgressDialog;
    private ProgressDialog pDialog;
    private String downloadUrl = "", downloadFileName = "";
    public static final int progress_bar_type = 0;
    private static ViewPager mPager;
    private static int currentPage = 0;
    boolean mapShow = false, seatShow = false;
    public static final int REQUEST_PLACE_PICKER = 1;
    ImageView share;
    public double latitude = 0.0, longitude = 0.0;
    public double current_latitude, current_longitude;
    ArrayList<ConferenceDetailsModel> arrayList;
    ArrayList<ConferenceDetailsModel> arrayList1;
    Fragment myf;
    PDFView pdfView;
    DatabaseHelper handler;
    String conference_id = "", conference_name = "", brochuers_file = "", event_host_name = "", venue = "", speciality = "",
            add_date = "", contact = "", from_date = "", from_time = "", to_date = "", to_time = "",
            registration_days = "", registration_fee = "", location = "",
            conference_type_id = "", credit_earnings = "", total_days_price = "", accomdation = "", member_concessions = "", student_concessions = "",
            price_hike_after_date = "", price_hike_after_percentage = "",
            broucher_image = "", available_seat = "",
            event_sponcer = "", conference_description = "", payment_mode = "";
    String changeLike = "", medical_profile_id = "";
    HashMap<String, String> hashMap = new HashMap<>();
    ArrayList aList;
    double packCharge = 0.0;
    private ArrayAdapter<String> listAdapter;
    ArrayList<String> pdfList = new ArrayList<String>();
    ArrayList<ImageModel> pdfImage = new ArrayList<>();
    ArrayList<ConferencePackageModel> conferencePackageList = new ArrayList<>();
    TextView txt_total_pck_chrge;
    ProgressBar tkt_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);

        mAppPreference = new AppCustomPreferenceClass();
        txt_total_pck_chrge = findViewById(R.id.txt_total_pck_chrge);
        linearLayoutManager = new LinearLayoutManager(TicketDetails.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManagerCharge = new LinearLayoutManager(TicketDetails.this);
        linearLayoutManagerCharge.setOrientation(LinearLayoutManager.VERTICAL);
        lnr_get_pdf = findViewById(R.id.lnr_get_pdf);
        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(linearLayoutManager);
        pdfView = findViewById(R.id.pdfView);
        tkt_progressBar = findViewById(R.id.tkt_progressBar);
        recycleViewCharge = findViewById(R.id.recycleViewCharge);
        recycleViewCharge.setLayoutManager(linearLayoutManagerCharge);
        txt_consession_member = findViewById(R.id.txt_consession_member);
        txt_medi_stud_dis = findViewById(R.id.txt_medi_stud_dis);
        txt_accomd_pck_chrge = findViewById(R.id.txt_accomd_pck_chrge);
        txt_payment_mode = findViewById(R.id.txt_payment_mode);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        txt_fm_time = findViewById(R.id.txt_fm_time);
        txt_t_date = findViewById(R.id.txt_t_date);
        txt_frm_date = findViewById(R.id.txt_frm_date);
        txt_t_time = findViewById(R.id.txt_t_time);
        titleIcon = findViewById(R.id.titleIcon);
        share = findViewById(R.id.share);
        txt_conctact = findViewById(R.id.txt_conctact);
        titleIcon.setImageResource(R.drawable.img_title_adconf);
        title = findViewById(R.id.title);
        locationAdres = findViewById(R.id.locationAdres);
        conferenceTitle = findViewById(R.id.conferenceTitle);
        date = findViewById(R.id.date);
        txt_no_of_seat = findViewById(R.id.txt_no_of_seat);
        guestName = findViewById(R.id.guestName);
//      txt_brouchername = findViewById(R.id.txt_brouchername);
//      txt_brouchername.setOnClickListener(this);
        time = findViewById(R.id.time);
        degree = findViewById(R.id.degree);
        txt_host_name = findViewById(R.id.txt_host_name);
        txt_regs_day = findViewById(R.id.txt_regs_day);
        txt_charg = findViewById(R.id.txt_charg);
        locationDetails = findViewById(R.id.locationDetails);
        btnBookReg = findViewById(R.id.btnBookReg);
        mPager = (ViewPager) findViewById(R.id.pager);
        wishlist = findViewById(R.id.wishlist);
        handler = new DatabaseHelper(this);


        img_broucher = findViewById(R.id.img_broucher);
        img_broucher.setOnClickListener(this);
        share.setOnClickListener(this);

        try {
            latitude = Double.parseDouble(AppCustomPreferenceClass.readString(TicketDetails.this, AppCustomPreferenceClass.Latitude, ""));
            longitude = Double.parseDouble(AppCustomPreferenceClass.readString(TicketDetails.this, AppCustomPreferenceClass.Longitude, ""));

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
            medical_profile_id = getIntent().getStringExtra("medical_profile_id");
//          registration_fee = getIntent().getStringExtra("registration_fee");
            brochuers_file = getIntent().getStringExtra("brochuers_file");
            changeLike = getIntent().getStringExtra("changeLike");
            available_seat = getIntent().getStringExtra("available_seat");
            conference_description = getIntent().getStringExtra("conference_description");
            to_date = getIntent().getStringExtra("to_date");
            to_time = getIntent().getStringExtra("to_time");
            event_sponcer = getIntent().getStringExtra("event_sponcer");
            conference_type_id = getIntent().getStringExtra("conference_type_id");
            credit_earnings = getIntent().getStringExtra("credit_earnings");
            total_days_price = getIntent().getStringExtra("total_days_price");
            accomdation = getIntent().getStringExtra("accomdation");
            member_concessions = getIntent().getStringExtra("member_concessions");
            student_concessions = getIntent().getStringExtra("student_concessions");
            price_hike_after_date = getIntent().getStringExtra("price_hike_after_date");
            price_hike_after_percentage = getIntent().getStringExtra("price_hike_after_percentage");
            payment_mode = getIntent().getStringExtra("payment_mode");

            //  pdfList=getIntent().getStringArrayListExtra("pdfList");
            //showLike = getIntent().getBooleanExtra("showLike", false);
            //  Log.d("PDFLSITTTT", pdfList.toString()+"");
        }

        txt_accomd_pck_chrge.setText(accomdation);
        txt_total_pck_chrge.setText(total_days_price);

        Log.d("2323233333", accomdation);
        Log.d("6765544566", total_days_price);

        if (changeLike.equalsIgnoreCase("1")) {

            wishlist.setImageResource(R.drawable.ic_like);
            showLike = true;
        } else {

            wishlist.setImageResource(R.drawable.heart_icon);
            showLike = false;
        }

        if (payment_mode.equalsIgnoreCase("1")) {
            txt_payment_mode.setText("Online");
        } else {

            txt_payment_mode.setText("AtVenue");
        }

        aList = new ArrayList(Arrays.asList(brochuers_file.split(",")));


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

        try {

            Log.d("BROCHURESTICKETWILL", aList + "");

            ImageShowAdapterConf requestPostJobAdapter = new ImageShowAdapterConf(this, R.layout.image_row, aList, pdfView,tkt_progressBar,conference_id);
            recycleView.setAdapter(requestPostJobAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            conferencePackageList = handler.getConfPackCharge(DatabaseHelper.TABLE_CONFERENCES_PACK_CHARGE, conference_id);
            if (conferencePackageList.size() > 0) {
                for (int i = 0; i < conferencePackageList.size(); i++) {
                    Log.d("Dayconfpackage", conferencePackageList.get(i).getConference_pack_day());
                    Log.d("CONFPCKCHERRconfpackage", conferencePackageList.get(i).getConference_pack_charge());
                    packCharge = packCharge + Double.parseDouble(conferencePackageList.get(i).getConference_pack_charge());

                }
                // txt_total_pck_chrge.setText(String.valueOf(packCharge));
                ConferenceChargeAdapter requestPostchrgAdapter = new ConferenceChargeAdapter(this, R.layout.pack_charg, conferencePackageList);
                recycleViewCharge.setAdapter(requestPostchrgAdapter);
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showLike) {
                    wishlist.setImageResource(R.drawable.heart_icon);
                    Log.d("232323", conference_id);
                    changeLike = "0";
                    showLike = false;
                    hashMap.put("show_like", changeLike);

                } else {
                    wishlist.setImageResource(R.drawable.ic_like);
                    Log.d("242424", conference_id);
                    changeLike = "1";
                    showLike = true;
                    hashMap.put("show_like", changeLike);
                }

                handler.updateData(hashMap, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);
                try {
                    ChangeLikeApi(conference_id, changeLike);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        ArrayList aList = new ArrayList(Arrays.asList(brochuers_file.split(",")));
        initSlider(aList);
        for (int i = 0; i < aList.size(); i++) {
            System.out.println("KKKKKKK-->" + aList.get(i));
            broucher_image = String.valueOf(aList.get(0));
        }
        Log.d("BROUCHER_FILEWELL", broucher_image);
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
        //  txt_brouchername.setText(brochuers_file.replace("http://162.243.205.148/cynapce/app/webroot/files/brochures/", ""));
        txt_regs_day.setText(available_seat);
        txt_charg.setText(registration_fee);
        Log.d("BROUDCHIFLE", brochuers_file);
        txt_conctact.setText(contact);
          locationAdres.setText(venue);
        txt_no_of_seat.setText(available_seat);
        txt_host_name.setText(event_host_name);
        txt_frm_date.setText(from_date);
        txt_t_date.setText(to_date);
        txt_fm_time.setText(from_time);
        txt_t_time.setText(to_time);

        try {
            getLocation();
        }catch (Exception e)
        {
            e.printStackTrace();
        }



//        Glide.with(getApplicationContext()).load(brochuers_file)
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.whey_protien)
//                .into(img_broucher);
        if(payment_mode.equalsIgnoreCase("2"))
        {
            btnBookReg.setVisibility(View.GONE);
        }else {

            btnBookReg.setVisibility(View.VISIBLE);
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


        Button btnSelect = dialog.findViewById(R.id.btnSelectSeat);
        dialog.show();
        //TODO : dismiss the on btn click and close click
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO : opening new review activity


                Log.d("SEATITITI22", txt_seat.getText().toString());
                try
                {
                    if (available_seat.equalsIgnoreCase(txt_seat.getText().toString())) {

                        Log.d("SEATITSEATWILL", AppCustomPreferenceClass.readString(TicketDetails.this, AppCustomPreferenceClass.phoneNumber, ""));
                        Log.d("SEATITEMAILWILL", AppCustomPreferenceClass.readString(TicketDetails.this, AppCustomPreferenceClass.email, ""));

                        Intent it = new Intent(TicketDetails.this, ReviewBooking.class);
                        it.putExtra("seat", txt_seat.getText().toString());
                        it.putExtra("conference_id", conference_id);
                        it.putExtra("total_days_price", total_days_price);
                        it.putExtra("accomdation", accomdation);
                        it.putExtra("savebool", false);
                        it.putExtra("price_hike_after_date", price_hike_after_date);
                        it.putExtra("price_hike_after_percentage", price_hike_after_percentage);
                        it.putExtra("medical_profile_id", medical_profile_id);
                        it.putExtra("member_concessions", member_concessions);
                        it.putExtra("student_concessions", student_concessions);
                        it.putExtra("payment_mode",payment_mode);

                        startActivity(it);


                    } else if (Integer.parseInt(available_seat) - Integer.parseInt(txt_seat.getText().toString()) > 0) {
                        Log.d("SEATITSEATWILL11", AppCustomPreferenceClass.readString(TicketDetails.this, AppCustomPreferenceClass.phoneNumber, ""));
                        Log.d("SEATITEMAILWILL22", AppCustomPreferenceClass.readString(TicketDetails.this, AppCustomPreferenceClass.email, ""));

                        Intent it = new Intent(TicketDetails.this, ReviewBooking.class);
                        it.putExtra("seat", txt_seat.getText().toString());
                        it.putExtra("conference_id", conference_id);
                        it.putExtra("total_days_price", total_days_price);
                        it.putExtra("accomdation", accomdation);
                        it.putExtra("savebool", false);
                        it.putExtra("price_hike_after_date", price_hike_after_date);
                        it.putExtra("price_hike_after_percentage", price_hike_after_percentage);
                        it.putExtra("medical_profile_id", medical_profile_id);
                        it.putExtra("member_concessions", member_concessions);
                        it.putExtra("student_concessions", student_concessions);
                        it.putExtra("payment_mode",payment_mode);

                        startActivity(it);


                    } else if (Integer.parseInt(txt_seat.getText().toString()) - Integer.parseInt(available_seat) > 0) {
                        Log.d("SEATITITI", "333333");
                        MyToast.toastLong(TicketDetails.this, "Selected no. of seat is not available !");
                    }else if(available_seat.equalsIgnoreCase("0"))
                    {
                        MyToast.toastLong(TicketDetails.this, "No. of seat is not available for booking !");

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


        mPager.setAdapter(new MyAdapter(TicketDetails.this, images));

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

//
//                HashMap<String, String> hashMap1 = new HashMap<>();
//                Log.d("CHECKLIKE44", conference_id);
//                Log.d("CHAGNDELIKEEXX2", changeLike);
//                hashMap1.put("show_like", changeLike);
//                handler.updateData(hashMap1, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);
//
//                arrayList1 = handler.getConferenceDetails(DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS, "1");
//
//                for (int k = 0; k < arrayList1.size(); k++) {
//                    Log.d("TIKEDETAILKLIKE11", arrayList1.get(k).getShow_like());
//                    Log.d("TIKEDETAILKLIKE22", arrayList1.get(k).getConference_name());
//                }
//
//                Intent intent = new Intent(TicketDetails.this, MainActivity.class);
//                intent.putExtra("changeLike", "1");
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                finish();
                break;

            case R.id.share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Conference Name: " + conference_name + "\n" + "Conference Date:" + from_date
                        + "\n" + "Conference Time:" + from_time
                        + "\n" + "Conference Venue:" + venue + "\n"
                        + "Conference Images:" + broucher_image);
                startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));

                break;
            case R.id.img_broucher:

//                if (ActivityCompat.checkSelfPermission(TicketDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(TicketDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) TicketDetails.this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
//                    return;
//
//                } else {
//
//                    downloadFileName = brochuers_file.replace("http://162.243.205.148/cynapce/app/webroot/files/brochures/", "");
//                    new DownloadFileFromURL().execute(brochuers_file);
//
//                }
//                Intent it = new Intent(TicketDetails.this, ShowImageTicket.class);
//                it.putExtra("brochuers_file", brochuers_file);
//                startActivity(it);
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ChangeLikeApi(String s, String status) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(TicketDetails.this, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("status", "1");

        // params.put("device_id", getDeviceID(context));
        header.put("Cynapse", params);
        new ChangeLikeApi(TicketDetails.this, header) {
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

//        HashMap<String, String> hashMap1 = new HashMap<>();
//
//        Log.d("CHAGNDELIKEEXX2", changeLike);
//        hashMap1.put("show_like", changeLike);
//        handler.updateData(hashMap1, DatabaseHelper.conference_id, conference_id, DatabaseHelper.TABLE_CONFERENCES_SHOW_DETAILS);
//
//        Intent intent = new Intent(TicketDetails.this, MainActivity.class);
//        intent.putExtra("changeLike", "1");
//        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TicketDetails.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();

            (TicketDetails.this).showDialog(progress_bar_type);
            root = Environment.getExternalStorageDirectory().toString();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

//                File file = new File(Environment.DIRECTORY_DOCUMENTS /);
                root = Environment.getExternalStorageDirectory().toString();
                File rootFile = new File(root, "Cynapse");
                if (!rootFile.exists()) {
                    rootFile.mkdirs();
                }

                String imagePath = rootFile + "/" + downloadFileName;
                System.out.println("imagePath==" + imagePath);

                imgFile = new File(imagePath);


                try {
                    imgFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // Output stream to write file
                OutputStream output = new FileOutputStream(imgFile.getAbsolutePath());


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);
            pDialog.dismiss();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);

            Log.d("IMGFILEOLIEIIE", imgFile + "");

            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext1 = imgFile.getName().substring(imgFile.getName().indexOf(".") + 1).toLowerCase();
            String type = mime.getMimeTypeFromExtension(ext1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.alcanzar.cynapse.provider", imgFile);
                intent.setDataAndType(contentUri, type);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            } else {
                intent.setDataAndType(Uri.fromFile(imgFile), type);
            }

            startActivity(intent);

        }

        void openFile(Context context, File url) throws IOException {

            Uri uri = Uri.fromFile(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.getAbsolutePath().contains(".pdf")) {
                intent.setDataAndType(uri, "application/pdf");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            //CustomPreference.pdf_boolean=true;
//            c.finish();
        }

        public void galleryAddPic(final File file, final Context context) {
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, (String[]) null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, final Uri uri) {
                            try {
                                Log.e("separated", "fileNameee" + file.getAbsolutePath());

                                openFile(TicketDetails.this, file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    //
//




    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }


}
