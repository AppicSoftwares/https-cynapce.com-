package com.alcanzar.cynapse.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alcanzar.cynapse.fragments.CaseStudiesFragment;
import com.alcanzar.cynapse.fragments.DashBoardFragment;
import com.alcanzar.cynapse.fragments.JobRequirementFragment;
import com.alcanzar.cynapse.fragments.MarketPlaceFragment;
import com.alcanzar.cynapse.fragments.TicketsFragment;

public class DashBoardFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 5;
    private Context context;

    public DashBoardFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new DashBoardFragment();
        } else if(position == 1){
            return new TicketsFragment();
        }
        else  if(position == 2){
            return new MarketPlaceFragment();
        }
        else if(position == 3){
            return new CaseStudiesFragment();
        }
        else {
            return new JobRequirementFragment();
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {

        // TODO : this is used to return null in case we use icons at the different tabs
        return null;

        // TODO : Generate title based on item position
//        switch (position) {
//            case 0:
//                return context.getString(R.string.sign_up);
//            case 1:
//                return context.getString(R.string.log_in);
//            case 2:
//                return context.getString(R.string.sign_up);
//            case 3:
//                return context.getString(R.string.log_in);
//            case 4:
//                return context.getString(R.string.log_in);
//            default:
//                return null;
//        }
    }
}