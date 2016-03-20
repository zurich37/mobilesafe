package com.zurich.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentArrayPageAdapter extends
        FragmentPagerAdapter {
    private Fragment[] fragments;

    public FragmentArrayPageAdapter(FragmentManager fm,
                                    Fragment... fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.length : 0;
    }

    public Fragment[] getFragments() {
        return fragments;
    }
}
