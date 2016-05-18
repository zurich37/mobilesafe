package com.zurich.mobile.fragment;

import android.os.Bundle;
import android.view.View;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.SharedPreferenceUtil;
import com.zurich.mobile.widget.SettingLayout;

/**
 * 设置防盗保护是否开启
 * Created by weixinfei on 16/4/24.
 */
public class SetupFourthFragment extends AppBaseFragment {

    private boolean lostFindSetting;
    private SettingLayout settingLayout;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_setup4;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {
        settingLayout = (SettingLayout) view.findViewById(R.id.setting_setup4_isopen);
        settingLayout.findViewById(R.id.setting_check).setEnabled(false);
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingLayout.getCheckBox()) {
                    settingLayout.setCheckBox(false);
                    SharedPreferenceUtil.setProtectConfigPrefs(false);
                    settingLayout.setTvSubName("未开启");
                } else {
                    settingLayout.setCheckBox(true);
                    SharedPreferenceUtil.setProtectConfigPrefs(true);
                    SharedPreferenceUtil.setSafeSettingPrefs(1);
                    settingLayout.setTvSubName("已开启");
                }
            }
        });
    }

    @Override
    public boolean isReadyData() {
        return false;
    }

    @Override
    public void onShowData() {
        settingLayout.setCheckBox(lostFindSetting);
        if (lostFindSetting) {
            settingLayout.setTvSubName("已开启");
        } else {
            settingLayout.setTvSubName("未开启");
        }
    }

    @Override
    public void onLoadData() {
        lostFindSetting = SharedPreferenceUtil.getProtectConfigPrefs();
        showData();
    }
}
