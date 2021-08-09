package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ViewPagerAdapter;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.PostJobFragment;
import com.alcanzar.cynapse.fragments.RequestJobFragment;
import com.alcanzar.cynapse.utils.MyToast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class RequestPostJobActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO : header views
    TextView title;
    ImageView btnBack,titleIcon;
    //TODO: configure titles at the different tabs
    private String[] titleStr;
    public DatabaseHelper handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_post_job);

        showBannerAds();
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        handler = new DatabaseHelper(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.deals_white);
        title = findViewById(R.id.title);
        title.setText(R.string.job_title);
        //TODO: initialization of the tabLayout and the viewpager
        ViewPager viewPager = findViewById(R.id.viewPager);
        setUpViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        //TODO: setting the tab titles
        titleStr = new String[]{getResources().getString(R.string.request_jobs),getResources().getString(R.string.post_jobs)};
        for(int i = 0;i<titleStr.length;i++){
            tabLayout.getTabAt(i).setText(titleStr[i]);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition()==1)
                {
                    medicalProfileautoclick = 0;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void showBannerAds() {
        AdView mAdView;
        MobileAds.initialize(this,
                getResources().getString(R.string.googleAdsAppID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    //TODO: this is used for the dynamic view pager implementation
    private void setUpViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new RequestJobFragment());
        viewPagerAdapter.addFrag(new PostJobFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }}


    public int medicalProfileautoclick = 0;
}
