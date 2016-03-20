package com.zurich.mobile.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.FragmentArrayPageAdapter;
import com.zurich.mobile.controller.FragmentSwitchController;
import com.zurich.mobile.controller.SkinTabIconController;
import com.zurich.mobile.fragment.ManageCenterFragment;
import com.zurich.mobile.fragment.NormalFragment;
import com.zurich.mobile.fragment.SafeFragment;
import com.zurich.mobile.view.ViewPagerCompat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.radio_mainActivity_safe)
    RadioButton radioMainActivitySafe;
    @Bind(R.id.radio_mainActivity_normal)
    RadioButton radioMainActivityNormal;
    @Bind(R.id.radio_mainActivity_manage)
    RadioButton radioMainActivityManage;
    @Bind(R.id.layout_navigationActivity_tabs)
    RadioGroup layoutNavigationActivityTabs;
    @Bind(R.id.pager_navigationActivity_content)
    ViewPagerCompat viewPager;

    private SkinTabIconController skinTabIconController;
    private FragmentSwitchController fragmentSwitchController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViews();
    }

    private void setupViews() {

        skinTabIconController = new SkinTabIconController(getBaseContext());
        skinTabIconController.addTab(radioMainActivitySafe, R.drawable.ic_tab_safe1);
        skinTabIconController.addTab(radioMainActivityNormal, R.drawable.ic_tab_home);
        skinTabIconController.addTab(radioMainActivityManage, R.drawable.ic_tab_manage);
        skinTabIconController.init();

        setFragments();
    }

    private void setFragments() {

        viewPager.setAdapter(null);
        viewPager.setAdapter(new FragmentArrayPageAdapter(getSupportFragmentManager(), new SafeFragment(), new NormalFragment(), new ManageCenterFragment()));
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());

        fragmentSwitchController = new FragmentSwitchController(viewPager, radioMainActivitySafe, radioMainActivityNormal, radioMainActivityManage);

    }


}
