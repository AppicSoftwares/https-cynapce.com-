package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.adapter.ViewPagerAdapter;
import com.alcanzar.cynapse.api.GetCounterApi;
import com.alcanzar.cynapse.api.GetDepartmentApi;
import com.alcanzar.cynapse.api.GetMedicalProfileApi;
import com.alcanzar.cynapse.api.GetMyDealApi;
import com.alcanzar.cynapse.api.GetProfileApi;
import com.alcanzar.cynapse.api.GetSpecializationApi;
import com.alcanzar.cynapse.api.LogOutApi;
import com.alcanzar.cynapse.api.UpdateSocialMediaProfileApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.CaseStudiesFragment;
import com.alcanzar.cynapse.fragments.DashBoardFragment;
import com.alcanzar.cynapse.fragments.GettingPublishedFragment;
import com.alcanzar.cynapse.fragments.JobRequirementFragment;
import com.alcanzar.cynapse.fragments.MarketPlaceFragment;
import com.alcanzar.cynapse.fragments.TicketsFragment;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalProfileModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.CustomPreference;
import com.alcanzar.cynapse.utils.GPSTracker;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.Util;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.linkedin.platform.LISessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //TODO :these are used for the navigation drawer
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TelephonyManager telephonyManager;


    //TODO : these are used for the tab_layout
    //TODO: configure icons at the different tabs
    //private int[] imageResId = {R.drawable.noticegrey, R.drawable.micicon, R.drawable.addtocart, R.drawable.search_with_diary, R.drawable.identity, R.drawable.micicon};
//    private int[] imageResId = {R.drawable.community_forum_icon, R.drawable.conference_grey, R.drawable.addtocart,
//            R.drawable.search_with_diary, R.drawable.find_job_icon_, R.drawable.case_publication_icon, R.drawable.dealers_dist_grey};


    //Bablu
//    private int[] imageResId = {R.drawable.conference_grey, R.drawable.addtocart, R.drawable.find_job_icon_, R.drawable.case_publication_icon};
//    String[] tabName = {"Conference", "Classified", "Jobs", "Publication", "Case Studies"};
    //end

    private int[] imageResId = {R.drawable.conference_grey, R.drawable.addtocart, R.drawable.find_job_icon_, R.drawable.case_publication_icon};
    String[] tabName = {"Events", "Classifieds", "Jobs", "Publication"};


//    private int[] imageResId = {R.drawable.addtocart, R.drawable.find_job_icon_, R.drawable.case_publication_icon};
    //TODO :header menu


    static String tabPress = "", filter_type = "", conference_loc = "", choosecategory = "", change_like_status = "";
    private String profileImg="";
    ImageView menu, btnNotify, btnChat, profilePic;
    //TODO :these are the navigation drawer tabs
    RelativeLayout gettingPublished, myPublications, myArticles, myDeals, myCaseStudies, dealerDistribute, myConferences, myJob, logOut, privacy_policy, term_and_condn, about_rel_lay, myPlans, myresume;
    LinearLayout profileSection;
    ImageView btnHelp, btnSettings;
    public DatabaseHelper handler;
    TextView userName, designation, counter_txt, counter_chat_txt, article_count_txt, versionNametxt;
    public GPSTracker tracker;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    boolean filter_show = false;
    AutoCompleteTextView medicalProfileauto, Spealization_auto, dept_auto;
    ArrayAdapter adapter_medical_profile, adapter_specialization, adapter_dept;
    ArrayList<String> medicalProfileName;
    ArrayList<String> specializationName;

    ArrayList<String> tempMedProf;
    ArrayList<String> tempSplzation;
    ArrayList<String> DeptName;
    ArrayList<String> tempDept;
    private String medStr = "", splStr = "", medicalId = "", specializationId = "", dept_Id = "", dept_Str = "";
    EditText edit_others, edit_spl_others, edit_dept_others;
    RelativeLayout Spl_rel_lay, other_rel_lay, other_spl_rel_lay, dept_rel_lay, other_dept_rel_lay;
    ArrayList<MedicalProfileModel> medicalList = new ArrayList<>();
    ArrayList<MedicalProfileModel> dept_SpinnerList = new ArrayList<>();
    ArrayList<JobSpecializationModel> specializationList = new ArrayList<>();
    View dept_view, spl_view;
    Dialog dialogSociallog;
    private GoogleApiClient googleApiClient;


    ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
    ArrayList<TextView> textViewArrayList = new ArrayList<>();
    final static int REQUEST_LOCATION = 199;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        AppConstantClass.mActivity = MainActivity.this;

        showBannerAds();

        FirebaseApp.initializeApp(MainActivity.this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Zsvadsv", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                       String token = task.getResult().getToken();

                        // Log and toast
                        Log.e("TokenCheck",token);
//                        Toast.makeText(MainActivity.this, "asdggadegads", Toast.LENGTH_SHORT).show();
                    }
                });

        showInterstitialAds();


        tracker = new GPSTracker(this) {
            @Override
            public void setLoc() {

            }
        };

        enableLoc(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA}, 1);
        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
//
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.CAMERA},
//                        2);
//
//            return;
//        }
        handler = new DatabaseHelper(this);
        //TODO :these are used for the navigation drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        toolbar = findViewById(R.id.toolbar);
        userName = findViewById(R.id.userName);
        profilePic = findViewById(R.id.profilePic);
        designation = findViewById(R.id.designation);
        counter_txt = findViewById(R.id.counter_txt);
        myPlans = findViewById(R.id.myPlans);
        myresume = findViewById(R.id.myresume);
        article_count_txt = findViewById(R.id.article_count_txt);
        counter_chat_txt = findViewById(R.id.counter_chat_txt);
        versionNametxt = findViewById(R.id.versionNametxt);
        setSupportActionBar(toolbar);
//        userName.setText(AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.name,""));
//        designation.setText(AppCustomPreferenceClass.readString(this,AppCustomPreferenceClass.medical_profile_name,""));
        //TODO: these are used for the tab_layout

        // TODO: Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);
//        viewPager.setAdapter(new DashBoardFragmentPagerAdapter(getSupportFragmentManager(),
//                MainActivity.this));
        // TODO Give the TabLayout the ViewPager
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
//        int versionCode = BuildConfig.VERSION_CODE;
//        String versionName = BuildConfig.VERSION_NAME;
//        versionNametxt.setText(String.format("Version : %s", versionName));
        if (getIntent() != null) {
            try
            {
                tabPress = getIntent().getStringExtra("tabPress");
                filter_type = getIntent().getStringExtra("filter_type");
                conference_loc = getIntent().getStringExtra("conference_loc");
                choosecategory = getIntent().getStringExtra("choosecategory");
                change_like_status = getIntent().getStringExtra("changeLike");
                filter_show = getIntent().getBooleanExtra("filter_show", false);

                System.out.println("tabPRESSSS" + filter_show);
                AppCustomPreferenceClass.writeString(MainActivity.this, AppCustomPreferenceClass.filter_show, String.valueOf(filter_show));
                AppCustomPreferenceClass.writeString(MainActivity.this, AppCustomPreferenceClass.conference_loc, conference_loc);
                AppCustomPreferenceClass.writeString(MainActivity.this, AppCustomPreferenceClass.tabPress, tabPress);
                AppCustomPreferenceClass.writeString(MainActivity.this, AppCustomPreferenceClass.filter_type, filter_type);
                AppCustomPreferenceClass.writeString(MainActivity.this, AppCustomPreferenceClass.conference_categ, choosecategory);
                //getTabPress(tabPress);
                if (tabPress == null) {
                    if (change_like_status == null) {
                        selectPage(0);
                    } else {
                        selectPage(1);
                    }
                } else {

                    selectPage(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.d("UUUUU", "222222");

        }

//        for (int i = 0; i < imageResId.length; i++) {
//            tabLayout.getTabAt(i).setIcon(imageResId[i]);
//            //TODO: this is used to make the first tab section selectable at the start of the activity
//            tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);
//        }

        displayProfileDP();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < imageResId.length; i++) {

            //TODO: this is used to make the first tab section selectable at the start of the activity

            View view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tabOne = view.findViewById(R.id.tab);
            ImageView tabImage = view.findViewById(R.id.tabImage);
            tabImage.setImageDrawable(getResources().getDrawable(imageResId[i]));

            tabImage.setTag("" + i);
            imageViewArrayList.add(tabImage);
            tabOne.setText(tabName[i]);
            //tabOne.setTextSize(10);


            textViewArrayList.add(tabOne);
            tabLayout.getTabAt(i).setIcon(imageResId[i]);


            if (filter_type == null && conference_loc == null && choosecategory == null) {
                //tabLayout.getTabAt(i).setIcon(imageResId[i]);
                // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, imageResId[i], 0, 0);
                imageViewArrayList.get(i).setImageDrawable(getResources().getDrawable(imageResId[i]));
                imageViewArrayList.get(0).setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);
                textViewArrayList.get(0).setTextColor(Color.parseColor("#27c0ba"));

                tabLayout.getTabAt(i).setCustomView(imageViewArrayList.get(i));
                tabLayout.getTabAt(i).setCustomView(textViewArrayList.get(i));
                //tabOne.setTextSize(10);
                // tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);
            } else {

                //tabLayout.getTabAt(i).setIcon(imageResId[i]);

                // tabOne.setCompoundDrawablesWithIntrinsicBounds(0, imageResId[i], 0, 0);
                tabImage.setImageDrawable(getResources().getDrawable(imageResId[i]));
                tabLayout.getTabAt(i).setCustomView(tabImage);
                tabLayout.getTabAt(i).setCustomView(textViewArrayList.get(i));
//                tabOne.setTextSize(10);


                //tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_IN);
            }

        }

        //this is usd for the showcase view

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);
        config.setShapePadding(-15);
        //config.setMaskColor(Color.parseColor("#80000919"));// half second between each showcase view
        config.setMaskColor(Color.parseColor("#CC000000"));// half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "1");
        sequence.setConfig(config);
        sequence.addSequenceItem(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0),
                "Healthcare Conferences/events" + "\n\n" + "List your healthcare events and view listed events like conferences, seminars . training courses, exhibitions etc.", "NEXT");
        sequence.addSequenceItem(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1),
                "Healthcare Deals & Classifieds" + "\n\n" + "See and post deals and classifieds on everything in Healthcare from buying, leasing, selling hospitals, clinics to equipment, books etc.", "NEXT");
        sequence.addSequenceItem(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(2),
                "Healthcare Jobs" + "\n\n" + "Apply & post Healthcare jobs- easy , convenient and near exact matching jobs on our copyrighted Job classification system.", "NEXT");
        sequence.addSequenceItem(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(3),
                "Healthcare Medical writing/Publication" + "\n\n" + "Want to publish something in Healthcare? get in touch with us. Data capturing&analysis, writing, backend support.", "FINISH");
//        sequence.addSequenceItem(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(3),
//                "Find Online Deals & Save here", "NEXT");
//        sequence.addSequenceItem(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(4),
//                "Search Coupons here...", "NEXT");
//        sequence.addSequenceItem(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(5),
//                "Search Nearby Stores On Map.", "NEXT");
//        sequence.addSequenceItem(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(6), "New Coupons Tap here...", "FINISH");
        sequence.start();


        //TODO : here we set up the icons the respective code in case of select , deselect and reselect
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //TODO : here we are changing the tab layout icon color for the selection purpose and also same have been done in case of the tab layout deselect and reselect case
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //tabImage.setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);
                tab.getIcon().setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);

                Log.e("tabposition", tabLayout.getSelectedTabPosition() + "");

                imageViewArrayList.get(tabLayout.getSelectedTabPosition()).setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);
                textViewArrayList.get(tab.getPosition()).setTextColor(Color.parseColor("#27c0ba"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tabImage.setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_IN);
                tab.getIcon().setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_IN);
                tab.getPosition();
                imageViewArrayList.get(tab.getPosition()).setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_IN);
                textViewArrayList.get(tab.getPosition()).setTextColor(Color.parseColor("#737373"));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //tabImage.setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);
                tab.getIcon().setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);
                imageViewArrayList.get(tab.getPosition()).setColorFilter(Color.parseColor("#27c0ba"), PorterDuff.Mode.SRC_IN);
                textViewArrayList.get(tab.getPosition()).setTextColor(Color.parseColor("#27c0ba"));
            }
        });

//        setupViewPager(viewPager);
//        viewPager.setAdapter(new DashBoardFragmentPagerAdapter(getSupportFragmentManager(),
//                MainActivity.this));
        // TODO Give the TabLayout the ViewPager
//        tabLayout = findViewById(R.id.tabLayout);

//        tabLayout.setupWithViewPager(viewPager);


        //TODO :this is used to open the navigation_menu on click
        menu = findViewById(R.id.menu);

//        try {
//            GetProfileShowDialogApi();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                drawerLayout.openDrawer(GravityCompat.START);
                try {
                    GetProfileApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    GetMyDealApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        //TODO :different tabs of the navigation_menu
        profileSection = findViewById(R.id.profileSection);
        gettingPublished = findViewById(R.id.gettingPublished);
        myPublications = findViewById(R.id.myPublications);
        myArticles = findViewById(R.id.myArticles);
        myDeals = findViewById(R.id.myDeals);
        myCaseStudies = findViewById(R.id.myCaseStudies);
        dealerDistribute = findViewById(R.id.dealerDistribute);
        myConferences = findViewById(R.id.myConferences);
        privacy_policy = findViewById(R.id.privacy_policy);
        term_and_condn = findViewById(R.id.term_and_condn);
        about_rel_lay = findViewById(R.id.about_rel_lay);
        myJob = findViewById(R.id.myJob);
        myArticles.setVisibility(View.GONE);
        myCaseStudies.setVisibility(View.GONE);
        myConferences.setVisibility(View.VISIBLE);
        myPublications.setVisibility(View.GONE);


//        medicalProfileauto = findViewById(R.id.medicalProfileauto);
//        Spealization_auto = findViewById(R.id.Spealization_auto);
//        dept_auto = findViewById(R.id.dept_auto);
//        edit_others = findViewById(R.id.edit_others);
//        edit_dept_others = findViewById(R.id.edit_dept_others);
//        edit_spl_others = findViewById(R.id.edit_spl_others);

        logOut = findViewById(R.id.logOut);
        btnHelp = findViewById(R.id.btnHelp);
        btnSettings = findViewById(R.id.btnSettings);
        btnNotify = findViewById(R.id.btnNotify);
        btnChat = findViewById(R.id.btnChat);
        //TODO : making all the views clickable
        profileSection.setOnClickListener(this);
        gettingPublished.setOnClickListener(this);
        myPublications.setOnClickListener(this);
        myArticles.setOnClickListener(this);
        myDeals.setOnClickListener(this);
        myCaseStudies.setOnClickListener(this);
        dealerDistribute.setOnClickListener(this);
        myConferences.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
        term_and_condn.setOnClickListener(this);
        myJob.setOnClickListener(this);
        logOut.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        about_rel_lay.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnNotify.setOnClickListener(this);
        myPlans.setOnClickListener(this);
        myresume.setOnClickListener(this);

    }

    private void displayProfileDP() {

        profileImg = AppCustomPreferenceClass.readString(MainActivity.this,AppCustomPreferenceClass.profile_image,"");
        Log.e("profileImg",profileImg);
        if (!profileImg.isEmpty())
            Picasso.with(getApplicationContext()).load(profileImg).placeholder(R.drawable.avatar).into(profilePic);
        else
            Picasso.with(getApplicationContext()).load(R.drawable.avatar).error(R.drawable.avatar).into(profilePic);
    }

    InterstitialAd interstitialAd;

    private void showInterstitialAds() {
        Log.e("interstitialAddShow", "showAdd");
        AdRequest adRequest = new AdRequest.Builder().build();

        MobileAds.initialize(this, getString(R.string.googleAdsAppID));
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.googleAdsInterstitialAdId));
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                String keyClose = CustomPreference.readString(MainActivity.this, CustomPreference.CheckAddKey, "");
                Log.e("vnkdnv", "sdgvbswdrbv");
                if (keyClose.equals("1")) {

                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

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


    private void deviceId() {
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            return;
        }
    }


    private void showBannerAds() {
        AdView mAdView;
        MobileAds.initialize(this,
                getResources().getString(R.string.googleAdsAppID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    void selectPage(int pageIndex) {
//        tabLayout.setScrollPosition(pageIndex, 0f, true);
//        viewPager.setCurrentItem(pageIndex);
        viewPager.setCurrentItem(pageIndex);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void enableLoc(Activity mContext) {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE_LOCATION);
            return;
        }

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < imageResId.length; i++) {

            //TODO: this is used to make the first tab section selectable at the start of the activity
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
            tabLayout.getTabAt(i).getIcon().setColorFilter(Color.parseColor("#737373"), PorterDuff.Mode.SRC_IN);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileSection:
                //startActivity(new Intent(this, MyProfileActivity.class));
                Intent intent1 = new Intent(this, MyProfileActivity.class);
                intent1.putExtra("edit_disable", "false");
                startActivity(intent1);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.gettingPublished:
                //startActivity(new Intent(this,GettingPublishedActivity.class));
                //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.term_and_condn:
                startActivity(new Intent(this, Terms_And_Condtions_Activity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.about_rel_lay:
                CustomPreference.writeString(this, CustomPreference.CheckAddKey, "1");
                if (interstitialAd.isLoaded() || interstitialAd.isLoading()) {
                    interstitialAd.show();
//                    Intent  intent=new Intent(this, AboutActivity.class);
                    showInterstitialAds();
                } else {
                    Log.e("vnkdnv", "srghesrb");
                    Intent intent = new Intent(this, AboutActivity.class);
                    startActivity(intent);

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                break;
            case R.id.myPlans:
                startActivity(new Intent(this, MyPlansActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myPublications:
                startActivity(new Intent(this, MyPublicationsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myArticles:
                startActivity(new Intent(this, MyArticlesActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myDeals:
                Intent i = new Intent(MainActivity.this, MyDealsActivity.class);
                i.putExtra("FromNoti", "1");
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;
            case R.id.myCaseStudies:
                startActivity(new Intent(this, MyCaseStudiesActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.dealerDistribute:
                startActivity(new Intent(this, DealerDistributeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myConferences:
                Intent intent = new Intent(this, MyConferencesActivity.class);
                //startActivity(new Intent(this, MyConferencesActivity.class));
                intent.putExtra("paymentsucess", "0");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myJob:
                startActivity(new Intent(this, MyJobActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logOut:
                //TODO: calling the logOutApi
                drawerLayout.closeDrawer(GravityCompat.START);
                // LogOutDialog();
                showDialog();

                break;
            case R.id.btnHelp:
                break;
            case R.id.btnSettings:
                break;
            case R.id.btnNotify:
                //TODO: opening the notifications activity
                startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                break;
            case R.id.btnChat:
                //TODO: opening the notifications activity
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                break;
            case R.id.myresume:
                //TODO: opening the notifications activity
                startActivity(new Intent(getApplicationContext(), ResumeUpLoad.class));
                break;

        }
    }

    Handler handle = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                try {
                    GetCounterApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    };

    @Override
    public void onPause() {
        handle.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.e("OnResume====", "=");
        handle.postDelayed(runnable, 1000);
        displayProfileDP();
        try {
            GetProfileShowDialogApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    public void LogOutDialog() {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(MainActivity.this);
        exitDialog.setTitle("Logout");
        exitDialog.setMessage("Are you sure, you want to logout?");
        exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    logOutApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        exitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        exitDialog.show();
    }

    Dialog dialoglog;

    public void showDialog() {
        Button yes, no;
        dialoglog = new Dialog(MainActivity.this);
        dialoglog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglog.setContentView(R.layout.dialog_msg_new);
        Window window = dialoglog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialoglog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        yes = (Button) dialoglog.findViewById(R.id.yes);
        no = (Button) dialoglog.findViewById(R.id.no);


        dialoglog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logOutApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialoglog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoglog.dismiss();
            }
        });

    }

    public void showSocialDialog() {
        final Dialog dialogSociallog;
        Button btn_Submit;
        ImageView cross_btn;
        dialogSociallog = new Dialog(MainActivity.this);
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.dialog_social_login_details);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSociallog.setCancelable(false);
        cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        btn_Submit = dialogSociallog.findViewById(R.id.btn_Submit);
        dialogSociallog.show();

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                dialogSociallog.dismiss();

            }
        });

        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSociallog.dismiss();
            }
        });

    }

    public void showSocialDialogDetails() {
        Button btn_Submit;
        ImageView cross_btn;
        dialogSociallog = new Dialog(MainActivity.this);
        dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSociallog.setContentView(R.layout.dialog_fill_social_login_details);
        Window window = dialogSociallog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSociallog.setCancelable(false);
        // cross_btn = dialogSociallog.findViewById(R.id.cross_btn);
        medicalProfileauto = dialogSociallog.findViewById(R.id.medicalProfileauto);
        Spealization_auto = dialogSociallog.findViewById(R.id.Spealization_auto);
        dept_auto = dialogSociallog.findViewById(R.id.dept_auto);

        edit_others = dialogSociallog.findViewById(R.id.edit_others);
        edit_dept_others = dialogSociallog.findViewById(R.id.edit_dept_others);
        edit_spl_others = dialogSociallog.findViewById(R.id.edit_spl_others);
        other_rel_lay = dialogSociallog.findViewById(R.id.other_rel_lay);
        other_spl_rel_lay = dialogSociallog.findViewById(R.id.other_spl_rel_lay);
        other_dept_rel_lay = dialogSociallog.findViewById(R.id.other_dept_rel_lay);
        dept_rel_lay = dialogSociallog.findViewById(R.id.dept_rel_lay);
        Spl_rel_lay = dialogSociallog.findViewById(R.id.Spl_rel_lay);
        dept_view = dialogSociallog.findViewById(R.id.dept_view);
        spl_view = dialogSociallog.findViewById(R.id.spl_view);
        btn_Submit = dialogSociallog.findViewById(R.id.btn_Submit);

        medicalProfileauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (medicalProfileauto.getText().toString().equals("")) {
                        tempMedProf = new ArrayList<>();
                        tempMedProf.addAll(medicalProfileName);
                    }

                    adapter_medical_profile = new Adapter_Filter(MainActivity.this, R.layout.dialog_fill_social_login_details, R.id.lbl_name, tempMedProf);
                    medicalProfileauto.setAdapter(adapter_medical_profile);
                    medicalProfileauto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }
        });

        Spealization_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Spealization_auto.getText().toString().equals("")) {
                        tempSplzation = new ArrayList<>();
                        tempSplzation.addAll(specializationName);
                    }

                    adapter_specialization = new Adapter_Filter(MainActivity.this, R.layout.dialog_fill_social_login_details, R.id.lbl_name, tempSplzation);
                    Spealization_auto.setAdapter(adapter_specialization);
                    Spealization_auto.showDropDown();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }
        });

        dept_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dept_auto.getText().toString().equals("")) {
                    tempDept = new ArrayList<>();

                    try {
                        tempDept.addAll(DeptName);
                        adapter_dept = new Adapter_Filter(MainActivity.this, R.layout.dialog_fill_social_login_details, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                        dept_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                }
            }
        });
        dialogSociallog.show();
        getMedicalProfileApi();
        try {
            getProfileSpecialization("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        medicalProfileauto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (medicalProfileauto.getText().toString().equals("")) {
                        tempMedProf = new ArrayList<>();
                        tempMedProf.addAll(medicalProfileName);
                    }
                    adapter_medical_profile = new Adapter_Filter(MainActivity.this, R.layout.activity_main, R.id.lbl_name, tempMedProf);
                    medicalProfileauto.setAdapter(adapter_medical_profile);
                    medicalProfileauto.showDropDown();

                } else {
                    if (medicalProfileauto.toString().equals("")) {

                    } else {

                    }

                }
            }


        });

        medicalProfileauto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {

                    tempMedProf = new ArrayList<>();
                    tempMedProf.addAll(medicalProfileName);

                    adapter_medical_profile = new Adapter_Filter(MainActivity.this, R.layout.activity_main, R.id.lbl_name, tempMedProf);
                    medicalProfileauto.setAdapter(adapter_medical_profile);


                } else {

                }

            }
        });

        medicalProfileauto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                medStr = medicalProfileauto.getText().toString();
                try {
                    medicalId = medicalList.get(medicalProfileName.indexOf(medStr)).getId();
                    edit_others.setText("");
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }

                Spealization_auto.setText("");

                dept_auto.setText("");

                edit_others.setText("");
                edit_spl_others.setText("");

                edit_dept_others.setText("");

                other_dept_rel_lay.setVisibility(View.GONE);
                other_spl_rel_lay.setVisibility(View.GONE);

                if (medicalId.equalsIgnoreCase("-1")) {
                    other_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_rel_lay.setVisibility(View.GONE);

                }
                if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
                    try {
                        GetDepartmentApi(medicalId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dept_rel_lay.setVisibility(View.VISIBLE);
                    dept_view.setVisibility(View.VISIBLE);
                    Spl_rel_lay.setVisibility(View.GONE);
                    spl_view.setVisibility(View.GONE);

                    Spealization_auto.setVisibility(View.GONE);
                    dept_auto.setVisibility(View.VISIBLE);
                } else {
                    dept_rel_lay.setVisibility(View.GONE);
                    dept_view.setVisibility(View.GONE);
                    Spl_rel_lay.setVisibility(View.VISIBLE);
                    spl_view.setVisibility(View.VISIBLE);
                    Spealization_auto.setVisibility(View.VISIBLE);
                    dept_auto.setVisibility(View.GONE);
                }
                Log.d("medicalId", medicalId);

            }
        });

        Spealization_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");


                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);
                    try {
                        if (Spealization_auto.getText().toString().equals("")) {
                            tempSplzation = new ArrayList<>();
                            tempSplzation.addAll(specializationName);
                        }
                        adapter_specialization = new Adapter_Filter(MainActivity.this, R.layout.activity_main, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                        Spealization_auto.showDropDown();
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {
                    if (Spealization_auto.toString().equals("")) {

                    } else {

                    }

                }


            }


        });

        Spealization_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempSplzation = new ArrayList<>();
                        tempSplzation.addAll(specializationName);

                        adapter_specialization = new Adapter_Filter(MainActivity.this, R.layout.activity_main, R.id.lbl_name, tempSplzation);
                        Spealization_auto.setAdapter(adapter_specialization);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }

                } else {

                }

            }
        });

        Spealization_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                splStr = Spealization_auto.getText().toString();
                try {
                    specializationId = specializationList.get(specializationName.indexOf(splStr)).getSpecialization_id();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                edit_spl_others.setText("");
                if (specializationId.equalsIgnoreCase("-3")) {
                    other_spl_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_spl_rel_lay.setVisibility(View.GONE);
                }

                Log.d("specializationId", specializationId);
            }
        });

        dept_auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {

                    Log.e("in ", "Country");

                    // stateC.setVisibility(View.GONE);
                    //  cityC.setVisibility(View.GONE);

                    if (dept_auto.getText().toString().equals("")) {
                        tempDept = new ArrayList<>();

                        try {
                            tempDept.addAll(DeptName);
                            adapter_dept = new Adapter_Filter(MainActivity.this, R.layout.activity_main, R.id.lbl_name, tempDept);
                            dept_auto.setAdapter(adapter_dept);
                            dept_auto.showDropDown();
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }


                } else {
                    if (dept_auto.toString().equals("")) {

                    } else {

                    }

                }
            }


        });

        dept_auto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    try {
                        tempDept = new ArrayList<>();
                        tempDept.addAll(DeptName);

                        adapter_dept = new Adapter_Filter(MainActivity.this, R.layout.activity_main, R.id.lbl_name, tempDept);
                        dept_auto.setAdapter(adapter_dept);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }


                } else {

                }

            }
        });

        dept_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                dept_Str = dept_auto.getText().toString();

                try {
                    dept_Id = dept_SpinnerList.get(DeptName.indexOf(dept_Str)).getId();
                } catch (ArrayIndexOutOfBoundsException ao) {
                    ao.printStackTrace();
                }
                edit_dept_others.setText("");
                if (dept_Id.equalsIgnoreCase("-5")) {
                    other_dept_rel_lay.setVisibility(View.VISIBLE);

                } else {
                    other_dept_rel_lay.setVisibility(View.GONE);
                }


            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidScreenOne()) {
                    try {
                        UpdateSocialMediaProfileApi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

//        cross_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogSociallog.dismiss();
//            }
//        });

        //deviceId();
    }

    private void getMedicalProfileApi() {
        new GetMedicalProfileApi(MainActivity.this) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    if (res_code.equals("1")) {
                        // medicalList.clear();
                        // medicalProfileSpinner.clear();
                        //MyToast.toastLong(MainActivity.this,res_msg);
                        JSONArray header2 = header.getJSONArray("ProfileCategoryMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            medicalList.add(new MedicalProfileModel(item.getString("id"), item.getString("profile_category_name")));
                            //  medicalProfileSpinner.add(item.getString("profile_category_name"));
                            //Log.e("medicalListSize",String.valueOf(medicalProfileSpinner.size()));
                        }
                        medicalList.add(new MedicalProfileModel("-1", "Others"));
//                        try{
//                            medicalProfile.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, medicalProfileSpinner));
//                        }
//                        catch (NullPointerException ne)
//                        {
//                            ne.printStackTrace();
//                        }
                        if (medicalList.size() > 0) {

                            medicalProfileName = new ArrayList<>();
                            tempMedProf = new ArrayList<>();
                            for (int j = 0; j < medicalList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempMedProf.add(medicalList.get(j).getProfile_category_name());
                                medicalProfileName.add(medicalList.get(j).getProfile_category_name());
                            }

                            adapter_medical_profile = new Adapter_Filter(MainActivity.this, R.layout.dialog_fill_social_login_details, R.id.lbl_name, tempMedProf);
                            medicalProfileauto.setAdapter(adapter_medical_profile);
                            medicalProfileauto.setThreshold(1);
                        }
                        //  medicalProfile.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, medicalProfileSpinner));
                    } else {
                        //medicalList.clear();
                        // medicalProfileSpinner.clear();
                        //MyToast.toastLong(MainActivity.this,res_msg);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
                        return;
                    }
                    String imeiNumber = telephonyManager.getDeviceId();
                    Log.i("imeiNumber", imeiNumber);
                    Toast.makeText(MainActivity.this, imeiNumber, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Without permission we check", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getProfileSpecialization(String id) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("profile_category_id", id);
        header.put("Cynapse", params);
        new GetSpecializationApi(MainActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        //jobSpecializationSpinner.clear();
                        specializationList.clear();
                        JSONArray header2 = header.getJSONArray("ProfileSpecializationMaster");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            specializationList.add(new JobSpecializationModel(item.getString("specialization_id"),
                                    item.getString("profile_category_id"), item.getString("specialization_name")));
                            //     jobSpecializationSpinner.add(item.getString("specialization_name"));
                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item
//                                , jobSpecializationSpinner);
//                        jobSpecialization.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(MainActivity.this,res_msg);
                        specializationList.add(new JobSpecializationModel("-3", "1", "Others"));

                        if (specializationList.size() > 0) {

                            specializationName = new ArrayList<>();
                            tempSplzation = new ArrayList<>();
                            for (int j = 0; j < specializationList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempSplzation.add(specializationList.get(j).getSpecialization_name());
                                specializationName.add(specializationList.get(j).getSpecialization_name());
                            }

                            adapter_specialization = new Adapter_Filter(MainActivity.this, R.layout.dialog_fill_social_login_details, R.id.lbl_name, tempSplzation);
                            Spealization_auto.setAdapter(adapter_specialization);
                            Spealization_auto.setThreshold(1);

                        }
                    } else {
                        // jobSpecializationSpinner.clear();
                        // specializationList.clear();
                        //MyToast.toastLong(MainActivity.this,res_msg);
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

    private void GetDepartmentApi(String medicalId) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("medical_profile_id", medicalId);
        params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetDepartmentApi(MainActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        //  dept_Spinner.clear();
                        dept_SpinnerList.clear();
                        JSONArray header2 = header.getJSONArray("Department");
                        for (int i = 0; i < header2.length(); i++) {
                            JSONObject item = header2.getJSONObject(i);
                            dept_SpinnerList.add(new MedicalProfileModel(item.getString("id"), item.getString("department_name")));
                            // dept_Spinner.add(item.getString("department_name"));
                        }
                        dept_SpinnerList.add(new MedicalProfileModel("-5", "Others"));
                        if (dept_SpinnerList.size() > 0) {

                            DeptName = new ArrayList<>();
                            tempDept = new ArrayList<>();
                            for (int j = 0; j < dept_SpinnerList.size(); j++) {
                                // countryName.add(medicalList.get(j).getProfile_category_name());
                                tempDept.add(dept_SpinnerList.get(j).getProfile_category_name());
                                DeptName.add(dept_SpinnerList.get(j).getProfile_category_name());
                            }

                            adapter_dept = new Adapter_Filter(MainActivity.this, R.layout.activity_main, R.id.lbl_name, tempDept);
                            dept_auto.setAdapter(adapter_dept);
                            dept_auto.setThreshold(1);
                        }
//                        ArrayAdapter<String> adapter =new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_item
//                                ,dept_Spinner);
//                        dept_dd.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        //MyToast.toastLong(MainActivity.this,res_msg);
                    } else {
                        // dept_Spinner.clear();
                        // dept_SpinnerList.clear();
                        //MyToast.toastLong(MainActivity.this,res_msg);
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

    private void GetCounterApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(MainActivity.this, AppCustomPreferenceClass.UserId, ""));
        // params.put("uuid","S75f3");
        header.put("Cynapse", params);
        new GetCounterApi(MainActivity.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    if (header.has("notification_counter")) {
                        String notification_counter = header.getString("notification_counter");
                        counter_txt.setText(notification_counter);
                    }

                    if (header.has("message_counter")) {
                        String message_counter = header.getString("message_counter");
                        counter_chat_txt.setText(message_counter);
                    }

                    handle.postDelayed(runnable, 10000);
                    Log.d("NOTIFICATION", response.toString());
                    if (res_code.equals("1")) {
                        //adapter.notifyDataSetChanged();
                    }
                    else {
                        if (res_msg.equals("Invalid User.")) {
                            try {
                                logOutApi();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // MyToast.toastLong(MainActivity.this, res_msg);
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

    private void GetProfileApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(MainActivity.this, header, false) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    if (res_code.equals("1")) {
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");
                        //akash changes

                        AppCustomPreferenceClass.writeString(MainActivity.this, "phone_no1", item.getString("phone_number"));
                        AppCustomPreferenceClass.writeString(MainActivity.this, "title_id1", item.getString("title_id"));
                        AppCustomPreferenceClass.writeString(MainActivity.this, "medical_Id1", item.getString("medical_profile_category_id"));
                        AppCustomPreferenceClass.writeString(MainActivity.this, "country_code1", item.getString("country_code"));
                        AppCustomPreferenceClass.writeString(MainActivity.this, "state_id1", item.getString("state_id"));
                        AppCustomPreferenceClass.writeString(MainActivity.this, "city_id1", item.getString("city_id"));

                        userName.setText(item.getString("name"));
                        designation.setText(item.getString("medical_profile_category_name"));

//                        Glide.with(getApplicationContext())
//                                .load(AppConstantClass.HOST_IMAGE + item.getString("profile_image"))
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .error(R.drawable.avatar)
//                                .dontAnimate()
//                                .into(profilePic);



                    } else {

                        //MyToast.toastLong(MainActivity.this,res_msg);
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

    private void GetProfileShowDialogApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        new GetProfileApi(MainActivity.this, header, false) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");

                    if (res_code.equals("1")) {
                        JSONObject item = header.getJSONObject("GetProfile");
                        item.getString("uuid");
                        userName.setText(item.getString("name"));

                        if (item.getString("medical_profile_category_id").equalsIgnoreCase("")) {
                            // showSocialDialog();
                            //showSocialDialogDetails();
                        }

                        designation.setText(item.getString("medical_profile_category_name"));
//                        Glide.with(getApplicationContext())
//                                .load(AppConstantClass.HOST_IMAGE + item.getString("profile_image"))
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .error(R.drawable.avatar)
//                                .dontAnimate().
//                                into(profilePic);


                        Utils.sop("bob---"+item.getString("profile_image"));
                        if (!item.getString("profile_image").isEmpty())
                        Picasso.with(getApplicationContext()).load(item.getString("profile_image")).placeholder(R.drawable.avatar).into(profilePic);


                    } else {
                        //MyToast.toastLong(MainActivity.this,res_msg);
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

    private boolean isValidScreenOne() {
        boolean isValid = false;
        if (medicalId.equalsIgnoreCase("2") || medicalId.equalsIgnoreCase("12") || medicalId.equalsIgnoreCase("13")) {
            if (medicalId.equalsIgnoreCase("")) {
                MyToast.toastShort(MainActivity.this, "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Medical Profile cannot be blank!");
                return false;
            } else if (dept_Id.equalsIgnoreCase("")) {
                MyToast.toastShort(MainActivity.this, "Please Enter Department");
            } else if (dept_Id.equalsIgnoreCase("-5") && edit_dept_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Department cannot be blank!");
                return false;
            } else {

                isValid = true;
            }
        } else {
            if (medicalId.equalsIgnoreCase("")) {
                MyToast.toastShort(MainActivity.this, "Please Enter Medical Profile");
            } else if (medicalId.equalsIgnoreCase("-1") && edit_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Medical Profile cannot be blank!");
                return false;
            } else if (specializationId.equalsIgnoreCase("")) {
                MyToast.toastShort(MainActivity.this, "Please Enter Specialization");
            } else if (specializationId.equalsIgnoreCase("-3") && edit_spl_others.getText().toString().equalsIgnoreCase("")) {
                MyToast.toastLong(this, "Others for Specialization cannot be blank!");
                return false;
            } else {
                isValid = true;
            }
        }

        return isValid;
    }

    private void UpdateSocialMediaProfileApi() throws JSONException {
        //TODO : json Objects Creation
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        //TODO: putting all the parameters
        params.put("uuid", AppCustomPreferenceClass.readString(MainActivity.this, AppCustomPreferenceClass.UserId, ""));
//        params.put("name", AppCustomPreferenceClass.readString(MainActivity.this, AppCustomPreferenceClass.name, ""));

        if (medStr.equalsIgnoreCase("Others")) {
            params.put("medical_profile_id", "othermedicalprofile");
            params.put("others_medical_profile", edit_others.getText().toString());
        } else {

            params.put("medical_profile_id", medicalId);
            params.put("others_medical_profile", "");
        }
        if (splStr.equalsIgnoreCase("Others")) {
            params.put("specialization_id", "otherspecialization");
            params.put("others_specialization", edit_spl_others.getText().toString());
        } else {

            params.put("specialization_id", specializationId);
            params.put("others_specialization", "");
        }

        if (dept_Str.equalsIgnoreCase("Others")) {
            params.put("department_id", "otherdepartment");
            params.put("others_department", edit_dept_others.getText().toString());
        } else {

            params.put("department_id", dept_Id);
            params.put("others_department", "");
        }


        //params.put("email", AppCustomPreferenceClass.readString(MainActivity.this, AppCustomPreferenceClass.email, ""));

        header.put("Cynapse", params);

        Log.d("PARAMSSMYPROFILE", params + "");
        new UpdateSocialMediaProfileApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_code = header.getString("res_code");
                    String res_msg = header.getString("res_msg");
                    Log.d("RESPONSEUPDATEPROFILE", response.toString());
                    if (res_code.trim().equals("1")) {
                        MyToast.toastLong(MainActivity.this, res_msg);
                        // JSONObject item  = header.getJSONObject("SignUp");
                        //TODO: saving the SignUpDetails
                        dialogSociallog.dismiss();
                        try {
                            GetProfileApi();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        MyToast.toastLong(MainActivity.this, res_msg);
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

    public void GetMyDealApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("sync_time", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.deal_sync_time, ""));
        //  params.put("sync_time", "");
        header.put("Cynapse", params);
        new GetMyDealApi(this, header, false) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    // arrayList.clear();
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");
                    AppCustomPreferenceClass.writeString(MainActivity.this, AppCustomPreferenceClass.deal_sync_time, sync_time);
                    Log.d("STATERESPONSE", response.toString());

                    if (res_code.equals("1")) {
                        //MyToast.toastShort(this,res_msg);
                        if (header.has("SellingProduct")) {
                            JSONArray header2 = header.getJSONArray("SellingProduct");
                            for (int i = 0; i < header2.length(); i++) {
                                JSONObject item = header2.getJSONObject(i);
                                DashBoardProductModel model = new DashBoardProductModel();
                                model.setId(item.getString("id"));
                                model.setProduct_id(item.getString("product_id"));
                                model.setProduct_name(item.getString("product_name"));
                                model.setCategory_id(item.getString("category_id"));
                                model.setCategory_name(item.getString("category_name"));
                                model.setPrice(item.getString("price"));
                                // model.setCondition_type(item.getString("condition_type"));
                                // model.setCondition(item.getString("condition"));
                                model.setAge(item.getString("age"));
                                model.setPractice_category_type(item.getString("practice_category_type"));
                                model.setPractice_category_name(item.getString("practice_category_name"));
                                model.setPractice_type(item.getString("practice_type"));
                                model.setPractice_type_name(item.getString("practice_type_name"));
                                model.setRooms(item.getString("rooms"));
                                model.setCountry_code(item.getString("country"));
                                model.setCountry_name(item.getString("country_name"));
                                model.setState_code(item.getString("state"));
                                model.setState_name(item.getString("state_name"));
                                model.setCity_id(item.getString("city"));
                                model.setCity_name(item.getString("city_name"));
                                model.setSpecific_locality(item.getString("specific_locality"));
                                model.setLand_length(item.getString("land_length"));
                                model.setLand_width(item.getString("land_width"));
                                model.setTotal_area(item.getString("total_area"));
                                model.setBuild_up_area(item.getString("build_up_area"));
                                //model.setPrimary_type( item.getString("primary_type"));
                                //model.setPrimary( item.getString("primary"));
                                // model.setLicence(item.getString("licence"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                //model.setSpecification(item.getString("specification"));
                                //model.setDeal_type(item.getString("deal_type"));
                                // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("1");
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER, DatabaseHelper.id, item.getString("id"))) {

                                    handler.AddSellBuyMaster(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddSellBuyMaster(model, false);
                                }
                            }
//                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId);
//                        Log.e("menulist.size();","<><><"+arrayList.size());
//                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(MyDealsActivity.this,R.layout.marketplacerow,arrayList);
//                        recycleView.setAdapter(myDealsAdapter);
                            // myDealsAdapter.notifyDataSetChanged();
                        }
                        if (header.has("BuyingProduct")) {
                            JSONArray header3 = header.getJSONArray("BuyingProduct");
                            for (int i = 0; i < header3.length(); i++) {
                                JSONObject item = header3.getJSONObject(i);
                                DashBoardProductModel model = new DashBoardProductModel();
                                // model.setId(item.getString("id"));
                                model.setId(item.getString("buying_id"));
                                model.setProduct_id(item.getString("product_id"));
                                model.setProduct_name(item.getString("product_name"));
                                model.setCategory_id(item.getString("category_id"));
                                model.setCategory_name(item.getString("category_name"));
                                model.setPrice(item.getString("price"));
                                // model.setCondition_type(item.getString("condition_type"));
                                // model.setCondition(item.getString("condition"));
                                model.setAge(item.getString("age"));
                                model.setPractice_category_type(item.getString("practice_category_type"));
                                model.setPractice_category_name(item.getString("practice_category_name"));
                                model.setPractice_type(item.getString("practice_type"));
                                model.setPractice_type_name(item.getString("practice_type_name"));
                                model.setRooms(item.getString("rooms"));
                                model.setCountry_code(item.getString("country"));
                                model.setCountry_name(item.getString("country_name"));
                                model.setState_code(item.getString("state"));
                                model.setState_name(item.getString("state_name"));
                                model.setCity_id(item.getString("city"));
                                model.setCity_name(item.getString("city_name"));
                                model.setSpecific_locality(item.getString("specific_locality"));
                                model.setLand_length(item.getString("land_length"));
                                model.setLand_width(item.getString("land_width"));
                                model.setTotal_area(item.getString("total_area"));
                                model.setBuild_up_area(item.getString("build_up_area"));
                                // model.setPrimary_type( item.getString("primary_type"));
                                // model.setPrimary( item.getString("primary"));
                                // model.setLicence(item.getString("licence"));
                                model.setNo_of_bed(item.getString("no_of_bed"));
                                // model.setSpecification(item.getString("specification"));
                                // model.setDeal_type(item.getString("deal_type"));
                                // model.setDeal_type_name(item.getString("deal_type_name"));
                                model.setDescription(item.getString("description"));
                                model.setProduct_image(item.getString("product_image"));
                                model.setAdd_date(item.getString("add_date"));
                                model.setModify_date(item.getString("modify_date"));
                                model.setStatus(item.getString("status"));
                                model.setSell_buy_status("2");
                                if (!handler.CheckIsDataAlreadyInDBorNot(DatabaseHelper.TABLE_SELL_BUY_PRODUCT_MASTER, DatabaseHelper.id, item.getString("buying_id"))) {

                                    handler.AddSellBuyMaster(model, true);

                                    //    Log.e("ADDED_Sub_item", true + " " + model.getProduct_id());
                                } else {
                                    //   Log.e("UPDATED", true + " " + model.getProduct_id());
                                    handler.AddSellBuyMaster(model, false);
                                }

                            }
//                        arrayList = handler.getMarketProducts(DatabaseHelper.TABLE_PRODUCT_MASTER, categoryId);
//                        Log.e("menulist.size();","<><><"+arrayList.size());
//                        MarketPlaceProductAdapter myDealsAdapter = new MarketPlaceProductAdapter(MyDealsActivity.this,R.layout.marketplacerow,arrayList);
//                        recycleView.setAdapter(myDealsAdapter);
                            // myDealsAdapter.notifyDataSetChanged();
                        }

                    } else {

                        // MyToast.toastLong(MyDealsActivity.this,res_msg);
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

    public void logOutApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, ""));
        params.put("auth_token", AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.authToken, ""));
        header.put("Cynapse", params);
        new LogOutApi(this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    if (res_code.equals("1")) {
                        //TODO: clearing all the app data
                        //AppCustomPreferenceClass.removeAll(getApplicationContext());
                        removeKeys();
                        LoginManager.getInstance().logOut();
                        LISessionManager.getInstance(getApplicationContext()).clearSession();
                        MyToast.toastLong(MainActivity.this, res_msg);
                        startActivity(new Intent(MainActivity.this, LoginSignUpActivity.class));
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        handler.deleteTable();
                        ActivityCompat.finishAffinity(MainActivity.this);
                    } else {
                        MyToast.toastLong(MainActivity.this, res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
                removeKeys();
                LoginManager.getInstance().logOut();
                LISessionManager.getInstance(getApplicationContext()).clearSession();
                MyToast.toastLong(MainActivity.this, "User Logout");
                startActivity(new Intent(MainActivity.this, LoginSignUpActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                handler.deleteTable();
                ActivityCompat.finishAffinity(MainActivity.this);
            }
        };
    }

    //this is used to open the exit app alert dialog
    boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {
        //TODO : here we check if the navigation drawer is opened then close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            //Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            //TODO : using a customized toast in android
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = layout.findViewById(R.id.text);
            text.setText(R.string.exit_msg);
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
            //TODO : this is used to make the exit condition false after the two seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    //TODO: this is used for the dynamic view pager implementation
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//         adapter.addFrag(new DashBoardFragment());

        TicketsFragment ticketsFragment = new TicketsFragment();

        if (getIntent() != null) {
            if (getIntent().hasExtra("fromWhere")) {
                if (getIntent().getStringExtra("fromWhere").equals("FilterConferenceActivity")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("response", getIntent().getStringExtra("response"));
                    ticketsFragment.setArguments(bundle);
                }
            }
        }

        adapter.addFrag(ticketsFragment);
        adapter.addFrag(new MarketPlaceFragment());
        adapter.addFrag(new JobRequirementFragment());
        adapter.addFrag(new GettingPublishedFragment());

        // adapter.addFrag(new DealerDistributerFragment());

//        adapter.addFrag(new MarketPlaceFragment());
//        adapter.addFrag(new JobRequirementFragment());
//        adapter.addFrag(new GettingPublishedFragment());


        viewPager.setAdapter(adapter);
    }

    //This method is called to remove keys individually as logging out deletes DeviceId
    private void removeKeys() {

        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.authToken);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.Price);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.name);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.UserId);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.medical_profile_id);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.medical_profile_name);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.email);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.phoneNumber);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.email_verified);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.mobile_verified);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.Country_name);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.Country_id);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.address);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.dob);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.occupation);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.profile_image);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.sync_time);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.noti_sync_time);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.deal_sync_time);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.job_ra_sync_time);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.app_job_ra_sync_time);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.other_cat_sync_time);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.sync_time_allconfer);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.sync_time_cases);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.sync_time_attend_cases);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.sync_time_myconference);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.sync_time_posted_jobs);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.sync_time_publish_list);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.conference_city);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.conference_state);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.filter_type);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.conference_loc);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.conference_categ);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.tabPress);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.change_like_status);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.filter_show);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.medical_profile_category_name);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.promo_price);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.promo_code);
//        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.promo_price3);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.pay_plan_sync_time);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.all_pdf_list);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.checkSelectStateAdapter);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.ACCESS_CODE);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.MERCHANT_ID);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.ORDER_ID);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.AMOUNT);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.CURRENCY);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.ENC_VAL);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.REDIRECT_URL);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.CANCEL_URL);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.pdf_name_code);
        AppCustomPreferenceClass.removeKey(this, AppCustomPreferenceClass.pdf_name);
    }
}
