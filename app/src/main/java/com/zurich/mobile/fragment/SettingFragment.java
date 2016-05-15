package com.zurich.mobile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.zurich.mobile.R;
import com.zurich.mobile.service.AddressService;
import com.zurich.mobile.service.CallSmsSafeService;
import com.zurich.mobile.utils.SharedPreferenceUtil;

/**
 * 设置子页面
 * Created by weixinfei on 16/5/13.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private CheckBoxPreference autoUpdate;
    private CheckBoxPreference location;
    private CheckBoxPreference blackNumber;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mActivity = getActivity();

        autoUpdate = (CheckBoxPreference) SettingFragment.this.findPreference(getString(R.string.setting_auto_update));
        location = (CheckBoxPreference) SettingFragment.this.findPreference(getString(R.string.setting_location));
        blackNumber = (CheckBoxPreference) SettingFragment.this.findPreference(getString(R.string.setting_black_number));
        autoUpdate.setOnPreferenceChangeListener(this);
        location.setOnPreferenceChangeListener(this);
        blackNumber.setOnPreferenceChangeListener(this);

        initPreference();
    }

    private void initPreference() {
        autoUpdate.setSummary(autoUpdate.isChecked() ? "自动更新已打开" : "自动更新已关闭");
        location.setSummary(location.isChecked() ? "骚扰拦截已打开" : "骚扰拦截已关闭");
        blackNumber.setSummary(blackNumber.isChecked() ? "骚扰拦截已打开" : "骚扰拦截已关闭");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean isOpen = Boolean.valueOf(newValue.toString());
        if (preference.getTitle().equals(getString(R.string.setting_auto_update))){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SharedPreferenceUtil.setAutoUpdatePrefs(isOpen);
            }
            SharedPreferenceUtil.setAutoUpdatePrefs(isOpen);
            autoUpdate.setSummary(isOpen ? "自动更新已打开" : "自动更新已关闭");
        }

        if (preference.getTitle().equals(getString(R.string.setting_black_number))){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SharedPreferenceUtil.setBlackInterceptPrefs(isOpen);
            }
            SharedPreferenceUtil.setBlackInterceptPrefs(isOpen);
            blackNumber.setSummary(isOpen ? "骚扰拦截已打开" : "骚扰拦截已关闭");
            Intent callSmsSafeIntent = new Intent(mActivity, CallSmsSafeService.class);
            if (isOpen){
                mActivity.startService(callSmsSafeIntent);
            }else {
                mActivity.stopService(callSmsSafeIntent);
            }
        }

        if (preference.getTitle().equals(getString(R.string.setting_location))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SharedPreferenceUtil.setSettingLocationPrefs(isOpen);
            }
            SharedPreferenceUtil.setSettingLocationPrefs(isOpen);
            location.setSummary(isOpen ? "来电归属地显示已打开" : "来电归属地显示已关闭");
            Intent showAddressIntent = new Intent(getActivity(), AddressService.class);
            if (!isOpen) {
                mActivity.stopService(showAddressIntent);
            } else {
                mActivity.startService(showAddressIntent);
            }
        }
        return true;
    }
}
