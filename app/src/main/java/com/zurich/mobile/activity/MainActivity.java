package com.zurich.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.FragmentArrayPageAdapter;
import com.zurich.mobile.controller.FragmentSwitchController;
import com.zurich.mobile.controller.SkinTabIconController;
import com.zurich.mobile.fragment.ManageCenterFragment;
import com.zurich.mobile.fragment.NormalFragment;
import com.zurich.mobile.fragment.SafeFragment;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.view.ViewPagerCompat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity{

    @Bind(R.id.radio_mainActivity_safe)
    RadioButton radioMainActivitySafe;
    @Bind(R.id.radio_mainActivity_normal)
    RadioButton radioMainActivityNormal;
    @Bind(R.id.radio_mainActivity_manage)
    RadioButton radioMainActivityManage;
    @Bind(R.id.pager_navigationActivity_content)
    ViewPagerCompat viewPager;

    private SkinTabIconController skinTabIconController;
    private FragmentSwitchController fragmentSwitchController;
    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        setupViews();
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        ivToolbarBack = (ImageView) mToolbar.findViewById(R.id.iv_toolbar_back);
        tvToolbarTitle = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        ivToolbarBack.setVisibility(View.GONE);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.action_settings){
                //开启设置页面
                startActivity(new Intent());
                GlobalUtils.showToast(getBaseContext(), "打开设置");
            }
            return true;
        }
    };

    private void setupViews() {

        skinTabIconController = new SkinTabIconController(getBaseContext());
        skinTabIconController.resetSkin();
        skinTabIconController.addTab(radioMainActivitySafe, R.drawable.ic_tab_safe);
        skinTabIconController.addTab(radioMainActivityNormal, R.drawable.ic_tab_home);
        skinTabIconController.addTab(radioMainActivityManage, R.drawable.ic_tab_manage);
        skinTabIconController.init();

        setFragments();
    }

    private void setFragments() {
        viewPager.setAdapter(new FragmentArrayPageAdapter(getSupportFragmentManager(), new SafeFragment(), new NormalFragment(), new ManageCenterFragment()));
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        fragmentSwitchController = new FragmentSwitchController(viewPager, radioMainActivitySafe, radioMainActivityNormal, radioMainActivityManage);
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
