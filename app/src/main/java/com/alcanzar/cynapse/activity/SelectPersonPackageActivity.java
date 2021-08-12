package com.alcanzar.cynapse.activity;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.adapter.MyPagerAdapter;
import com.alcanzar.cynapse.fragments.BookTicketFragmentNew;
import com.alcanzar.cynapse.fragments.ForeignPackageSelectionFragment;
import com.alcanzar.cynapse.fragments.PackageSelectionFragment;

import java.util.ArrayList;
import java.util.List;

public class SelectPersonPackageActivity extends AppCompatActivity {

    private String conferenceID = "";
    public static Activity activity;
    public ImageView btnBack;
    private ViewPager vpPager;
    private List<Fragment> fragmentList;
    private PagerTabStrip pager_header;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_person_package);

        findViewById(R.id.titleIcon).setVisibility(View.GONE);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        pager_header =  findViewById(R.id.pager_header);
        title =  findViewById(R.id.title);
        title.setText("Package Details");

//        pager_header.setTabIndicatorColor(getResources().getColor(R.color.color_black));
//        pager_header.setDrawFullUnderline(false);

        conferenceID = getIntent().getStringExtra("conferenceID");
        activity = SelectPersonPackageActivity.this;
        btnBack = findViewById(R.id.btnBack);
        fragmentList = new ArrayList<>();

        if (BookTicketFragmentNew.whichPAckage.equals("")) {
            fragmentList.add(PackageSelectionFragment.newInstance(1, conferenceID));
            fragmentList.add(ForeignPackageSelectionFragment.newInstance(2, conferenceID));
            loadAdapter(0);
        } else if (BookTicketFragmentNew.whichPAckage.trim().equals("1")) {
            fragmentList.add(PackageSelectionFragment.newInstance(1, conferenceID));
            loadAdapter(1);
        } else {
            fragmentList.add(ForeignPackageSelectionFragment.newInstance(2, conferenceID));
            loadAdapter(2);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    private void loadAdapter(int pos) {
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), fragmentList,pos);
        vpPager.setAdapter(adapterViewPager);
    }
}
