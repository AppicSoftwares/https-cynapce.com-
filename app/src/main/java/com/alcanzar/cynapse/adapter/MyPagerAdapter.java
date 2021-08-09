package com.alcanzar.cynapse.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alcanzar.cynapse.fragments.PackageSelectionFragment;

import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private int NUM_ITEM = 0;

    private List<Fragment> fragmentList;
    private String normalPackage = "NORMAL PACKAGE";
    private String foreignDelegatesPackage = "FOREIGN DELEGATES PACKAGE";

    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, int NUM_ITEM) {
        super(fm);
        this.fragmentList = fragmentList;
        this.NUM_ITEM = NUM_ITEM;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (NUM_ITEM == 0) {
            if (position == 0)
                return normalPackage;
            else
                return foreignDelegatesPackage;
        } else if (NUM_ITEM == 1)
            return normalPackage;
        else
            return foreignDelegatesPackage;
    }
}
