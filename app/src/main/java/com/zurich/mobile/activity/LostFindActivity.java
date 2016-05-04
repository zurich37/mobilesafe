package com.zurich.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.SharedPreferenceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置安全防盗页面
 * Created by weixinfei on 16/4/19.
 */
public class LostFindActivity extends FragmentActivity implements View.OnClickListener {

    @Bind(R.id.tv_lostfind_number)
    TextView tvLostfindNumber;
    @Bind(R.id.iv_lostfind_status)
    ImageView ivLostfindStatus;
    @Bind(R.id.tv_lostfind_reset)
    TextView tvLostfindReset;
    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find);
        ButterKnife.bind(this);
        Boolean isConfiged = SharedPreferenceUtil.getLostFindConfigPrefs(getBaseContext(), false);
        if (isConfiged) {
            initActionBar();
            boolean protecting = SharedPreferenceUtil.getProtectConfigPrefs(getBaseContext(), false);
            if (protecting) {
                ivLostfindStatus.setImageResource(R.drawable.lock);
            } else {
                ivLostfindStatus.setImageResource(R.drawable.unlock);
            }
            tvLostfindNumber.setText(SharedPreferenceUtil.getSafePhoneNumberPrefs(getBaseContext(), ""));
        }else {
            Intent intent = new Intent(getBaseContext(), SetupSafeActivty.class);
            startActivity(intent);
            finish();
        }

        configViews();
    }

    private void initActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.lost_find_tool_bar);
        ivToolbarBack = (ImageView) mToolbar.findViewById(R.id.iv_toolbar_back);
        tvToolbarTitle = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("安全防盗");
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void configViews() {
        tvLostfindReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_lostfind_reset) {
            Intent intent = new Intent(getBaseContext(), SetupSafeActivty.class);
            startActivity(intent);
        }
    }
}
