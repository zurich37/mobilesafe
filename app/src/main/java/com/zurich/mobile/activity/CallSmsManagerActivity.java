package com.zurich.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.FragmentArrayPageAdapter;
import com.zurich.mobile.controller.SkinTabStripController;
import com.zurich.mobile.controller.SkinTabViewFactory;
import com.zurich.mobile.fragment.CallInterceptFragment;
import com.zurich.mobile.fragment.SmsInterceptFragment;
import com.zurich.mobile.view.ViewPagerCompat;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.xiaopan.psts.PagerSlidingTabStrip;

/**
 * 通信助手
 * Created by weixinfei on 16/5/5.
 */
public class CallSmsManagerActivity extends BaseActivity {
    @Bind(R.id.call_sms_viewpager)
    ViewPagerCompat callSmsViewpager;
    @Bind(R.id.call_sms_tabs)
    PagerSlidingTabStrip callSmsTabs;
    @Bind(R.id.toolbar_call_sms_manager)
    Toolbar mToolbar;
    private Fragment[] fragments;
    private SkinTabViewFactory tabViewFactory;
    private String[] tabNames;
    private SkinTabStripController tabStripController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_manager);
        ButterKnife.bind(this);

        tabNames = new String[]{"短信拦截", "电话拦截"};
        fragments = new Fragment[]{new SmsInterceptFragment(), new CallInterceptFragment()};

        initActionBar();
        initView();
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.manage_center_contact_assistant));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_back_normal);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    //设置menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.men_action_settings:
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), BlackNumberActivity.class);
                startActivity(intent);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        tabViewFactory = new SkinTabViewFactory(this, tabNames);
        callSmsViewpager.setAdapter(new FragmentArrayPageAdapter(getSupportFragmentManager(), fragments));

        tabStripController = new SkinTabStripController(getBaseContext(), callSmsTabs);
        tabStripController.resetSkin();
        callSmsTabs.setTabViewFactory(tabViewFactory);
        callSmsTabs.setViewPager(callSmsViewpager);
    }

    private void initData() {
        initAdapter();
    }

    private void initAdapter() {

    }

}
