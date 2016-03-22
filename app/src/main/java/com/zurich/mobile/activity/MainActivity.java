package com.zurich.mobile.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.rey.material.app.ToolbarManager;
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

public class MainActivity extends AppCompatActivity implements ToolbarManager.OnToolbarGroupChangedListener {

    @Bind(R.id.radio_mainActivity_safe)
    RadioButton radioMainActivitySafe;
    @Bind(R.id.radio_mainActivity_normal)
    RadioButton radioMainActivityNormal;
    @Bind(R.id.radio_mainActivity_manage)
    RadioButton radioMainActivityManage;
    @Bind(R.id.pager_navigationActivity_content)
    ViewPagerCompat viewPager;

    private ToolbarManager mToolbarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        setupViews();
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("安全卫士");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        mToolbarManager = new ToolbarManager(getDelegate(), mToolbar, R.id.tb_group_main, R.style.ToolbarRippleStyle, R.anim.abc_fade_in, R.anim.abc_fade_out);

        mToolbarManager.registerOnToolbarGroupChangedListener(this);
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

        SkinTabIconController skinTabIconController = new SkinTabIconController(getBaseContext());
        skinTabIconController.addTab(radioMainActivitySafe, R.drawable.ic_tab_safe);
        skinTabIconController.addTab(radioMainActivityNormal, R.drawable.ic_tab_home);
        skinTabIconController.addTab(radioMainActivityManage, R.drawable.ic_tab_manage);
        skinTabIconController.init();

        setFragments();
    }

    private void setFragments() {

        viewPager.setAdapter(null);
        viewPager.setAdapter(new FragmentArrayPageAdapter(getSupportFragmentManager(), new SafeFragment(), new NormalFragment(), new ManageCenterFragment()));
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());

        FragmentSwitchController fragmentSwitchController = new FragmentSwitchController(viewPager, radioMainActivitySafe, radioMainActivityNormal, radioMainActivityManage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbarManager.createMenu(R.menu.menu_main);
        return true;
    }

    @Override
    public void onToolbarGroupChanged(int oldGroupId, int groupId) {
        mToolbarManager.notifyNavigationStateChanged();
    }
}
