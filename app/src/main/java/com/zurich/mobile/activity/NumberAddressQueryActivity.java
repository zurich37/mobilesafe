package com.zurich.mobile.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zurich.mobile.R;
import com.zurich.mobile.db.Dao.AddressDao;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 常用号码归属地查询
 * Created by weixinfei on 16/5/10.
 */
public class NumberAddressQueryActivity extends BaseActivity {
    private static final String TAG = "NumberAddressQueryActivity";
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.toolbar_location_query)
    Toolbar mToolbar;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到手机振动器的服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        setContentView(R.layout.activity_number_query);
        ButterKnife.bind(this);
        initActionBar();
        initView();
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.query_location));
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
                SettingActivity.launch(NumberAddressQueryActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        etPhone.addTextChangedListener(new TextWatcher() {
            //当文本发生变化的时候调用的方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    String address = AddressDao.getAddress(s.toString());
                    tvResult.setText(address);
                }
            }

            //当文本变化之前调用的方法
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            //文本变化后调用的方法
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void query(View view) {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            etPhone.startAnimation(shake);
            Toast.makeText(this, "号码不能为空", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(new long[]{100, 200, 100, 300, 50, 200}, 1);
            return;
        } else {
            String address = AddressDao.getAddress(phone);
            tvResult.setText(address);
        }
    }
}
