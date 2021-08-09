package com.alcanzar.cynapse.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.ViewPagerAdapter;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.fragments.PostedJobsFragment;
import com.alcanzar.cynapse.fragments.RequestedJobsFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MyJobActivity extends AppCompatActivity implements View.OnClickListener{
    //TODO : header views
    TextView title;
    ImageView btnBack,titleIcon;
    Button btnSearch;
    String[] tabTitle;
   public DatabaseHelper handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);
        initialiseViews();
    }
    private void initialiseViews() {
        //TODO : initializing and setting the header views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        handler = new DatabaseHelper(this);
        titleIcon = findViewById(R.id.titleIcon);
        titleIcon.setImageResource(R.drawable.job_white);
        title = findViewById(R.id.title);
        title.setText(R.string.my_job);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        //TODO: setting the tabLayout and the viewPager
        ViewPager viewPager = findViewById(R.id.viewPager);
        //TODO: calling this setUpViewPager to add fragments to the viewPager
        setUpViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        //TODO: setting the tabLayout Title
        tabTitle = new String[]{getResources().getString(R.string.requested_jobs),getResources().getString(R.string.posted_jobs)};
        for(int i = 0;i<tabTitle.length;i++){
            tabLayout.getTabAt(i).setText(tabTitle[i]);
        }

        showBannerAds();
    }

    private void showBannerAds() {
        AdView mAdView;
        MobileAds.initialize(this,
                getResources().getString(R.string.googleAdsAppID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }
    }
    private  void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new RequestedJobsFragment());
        viewPagerAdapter.addFrag(new PostedJobsFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }
}
