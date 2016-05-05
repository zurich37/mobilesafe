package com.zurich.mobile.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.FragmentArrayPageAdapter;
import com.zurich.mobile.controller.FragmentSwitchController;
import com.zurich.mobile.controller.SkinTabIconController;
import com.zurich.mobile.fragment.ManageCenterFragment;
import com.zurich.mobile.fragment.SafeFragment;
import com.zurich.mobile.view.ViewPagerCompat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页面
 * 包含两个Fragment
 */
public class MainActivity extends FragmentActivity {

    @Bind(R.id.radio_mainActivity_safe)
    RadioButton radioMainActivitySafe;
    @Bind(R.id.radio_mainActivity_manage)
    RadioButton radioMainActivityManage;
    @Bind(R.id.pager_navigationActivity_content)
    ViewPagerCompat viewPager;
    @Bind(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    private SkinTabIconController skinTabIconController;
    private FragmentSwitchController fragmentSwitchController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        setupViews();
    }

    private void initToolbar() {
        ivToolbarBack.setVisibility(View.GONE);
        tvToolbarTitle.setVisibility(View.INVISIBLE);
    }

    private void setupViews() {

        skinTabIconController = new SkinTabIconController(getBaseContext());
        skinTabIconController.resetSkin();
        skinTabIconController.addTab(radioMainActivitySafe, R.drawable.ic_tab_safe);
        skinTabIconController.addTab(radioMainActivityManage, R.drawable.ic_tab_manage);
        skinTabIconController.init();

        setFragments();
    }

    private void setFragments() {
        viewPager.setAdapter(new FragmentArrayPageAdapter(getSupportFragmentManager(), new SafeFragment(), new ManageCenterFragment()));
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        fragmentSwitchController = new FragmentSwitchController(viewPager, radioMainActivitySafe, radioMainActivityManage);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        skinTabIconController.destroy();
    }
}
