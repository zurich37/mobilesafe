package com.zurich.mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.SmsTools;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 百宝箱
 * Created by weixinfei on 16/5/10.
 */
public class AtoolsActivity extends BaseActivity {
    @Bind(R.id.toolbar_atools)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        ButterKnife.bind(this);

        initToolBar();
    }

    private void initToolBar() {
        mToolbar.setTitle(getResources().getString(R.string.manage_center_installed_backup));
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
                finish();
                return true;
            case R.id.men_action_settings:
                SettingActivity.launch(AtoolsActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 号码归属地查询
     *
     * @param view
     */
    public void numberAddressQuery(View view) {
        Intent intent = new Intent(this, NumberAddressQueryActivity.class);
        startActivity(intent);
    }

    /**
     * 常用号码查询
     *
     * @param view
     */
    public void commonNumberQuery(View view) {
        Intent intent = new Intent(this, CommonNumberQueryActivity.class);
        startActivity(intent);
    }

    /**
     * 短信的备份
     *
     * @param view
     */
    public void smsBackup(View view) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            final File file = new File(Environment.getExternalStorageDirectory(), "smsbackup.xml");
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMessage("稍安勿躁，正在备份中...");
            pd.show();
            new Thread() {
                public void run() {
                    try {
                        SmsTools.backup(getApplicationContext(), file.getAbsolutePath(), new SmsTools.BackUpCallBack() {
                            @Override
                            public void onSmsBackup(int progress) {
                                pd.setProgress(progress);
                            }

                            @Override
                            public void beforeSmsBackup(int total) {
                                pd.setMax(total);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();
                }

                ;
            }.start();
        } else {
            GlobalUtils.showToast(getBaseContext(), "sd不可用");
        }
    }
}
