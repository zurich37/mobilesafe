package com.zurich.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.FragmentArrayPageAdapter;
import com.zurich.mobile.controller.FragmentSwitchController;
import com.zurich.mobile.controller.SkinTabIconController;
import com.zurich.mobile.fragment.ManageCenterFragment;
import com.zurich.mobile.fragment.SafeFragment;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.SharedPreferenceUtil;
import com.zurich.mobile.view.ViewPagerCompat;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页面
 * 包含两个Fragment
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.radio_mainActivity_safe)
    RadioButton radioMainActivitySafe;
    @Bind(R.id.radio_mainActivity_manage)
    RadioButton radioMainActivityManage;
    @Bind(R.id.pager_navigationActivity_content)
    ViewPagerCompat viewPager;
    @Bind(R.id.main_tool_bar)
    Toolbar mToolbar;
    @Bind(R.id.nv_main_navigation)
    NavigationView nvMainNavigation;
    @Bind(R.id.dl_main_drawer)
    DrawerLayout drawerLayout;

    private SkinTabIconController skinTabIconController;
    private FragmentSwitchController fragmentSwitchController;

    private static Boolean isQuit = false;
    Timer timer = new Timer();
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer_layout);
        ButterKnife.bind(this);

        initToolbar();

        setupViews();
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_menu_drawer);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        //监听DrawerLayout
        //将抽屉事件和 toolbar联系起来，这是 material design 的设计
        toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
    }

    private void setupViews() {
        skinTabIconController = new SkinTabIconController(getBaseContext());
        skinTabIconController.resetSkin();
        skinTabIconController.addTab(radioMainActivitySafe, R.drawable.ic_tab_safe);
        skinTabIconController.addTab(radioMainActivityManage, R.drawable.ic_tab_manage);
        skinTabIconController.init();

        setupDrawerContent(nvMainNavigation);

        setFragments();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        switch (id) {
                            case R.id.men_action_settings:
                                SettingActivity.launch(MainActivity.this);
                                return true;
                            case R.id.men_action_change_mode:
                                SharedPreferenceUtil.setDarkMode(!SharedPreferenceUtil.getThemeMode());
                                MainActivity.this.recreate();//重新创建当前Activity实例
                                return true;
                            case R.id.men_action_about_me:
                                startActivity(new Intent(getBaseContext(), AboutMeActivity.class));
                                return true;
                            case R.id.menu_action_share:
                                return true;
                            case R.id.men_action_about_app:
                                startActivity(new Intent(getBaseContext(), AboutAppActivity.class));
                                return true;
                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewPager.getCurrentItem() == 0) {
            if (isQuit == false) {
                isQuit = true;
                GlobalUtils.showToast(getBaseContext(), "再按一次返回键退出程序");
                TimerTask task = null;
                task = new TimerTask() {
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                finish();
                System.exit(0);
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK && viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
            super.onKeyDown(keyCode, event);
        }
        return false;
    }
}
