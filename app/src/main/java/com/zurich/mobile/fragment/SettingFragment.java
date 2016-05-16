package com.zurich.mobile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.zurich.mobile.R;
import com.zurich.mobile.service.AddressService;
import com.zurich.mobile.service.CallSmsSafeService;
import com.zurich.mobile.service.PrivacyService;
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
    private CheckBoxPreference appLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mActivity = getActivity();

        autoUpdate = (CheckBoxPreference) SettingFragment.this.findPreference(getString(R.string.setting_auto_update));
        location = (CheckBoxPreference) SettingFragment.this.findPreference(getString(R.string.setting_location));
        blackNumber = (CheckBoxPreference) SettingFragment.this.findPreference(getString(R.string.setting_black_number));
        appLock = (CheckBoxPreference) SettingFragment.this.findPreference(getString(R.string.setting_app_lock));
        autoUpdate.setOnPreferenceChangeListener(this);
        appLock.setOnPreferenceChangeListener(this);
        location.setOnPreferenceChangeListener(this);
        blackNumber.setOnPreferenceChangeListener(this);

        initPreference();
    }

    private void initPreference() {
        autoUpdate.setSummary(autoUpdate.isChecked() ? "自动更新已打开" : "自动更新已关闭");
        location.setSummary(location.isChecked() ? "来电归属地显示已打开" : "来电归属地显示已关闭");
        blackNumber.setSummary(blackNumber.isChecked() ? "隐私保护已打开" : "隐私保护已关闭");
        appLock.setSummary(location.isChecked() ? "隐私保护已打开" : "隐私保护已关闭");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean isOpen = Boolean.valueOf(newValue.toString());
        if (preference.getTitle().equals(getString(R.string.setting_auto_update))) {
            SharedPreferenceUtil.setAutoUpdatePrefs(isOpen);
            autoUpdate.setSummary(isOpen ? "自动更新已打开" : "自动更新已关闭");
        }

        if (preference.getTitle().equals(getString(R.string.setting_black_number))) {
            SharedPreferenceUtil.setBlackInterceptPrefs(isOpen);
            blackNumber.setSummary(isOpen ? "通信拦截已打开" : "通信拦截已关闭");
            Intent callSmsSafeIntent = new Intent(mActivity, CallSmsSafeService.class);
            if (isOpen) {
                mActivity.startService(callSmsSafeIntent);
            } else {
                mActivity.stopService(callSmsSafeIntent);
            }
        }

        if (preference.getTitle().equals(getString(R.string.setting_location))) {
            SharedPreferenceUtil.setSettingLocationPrefs(isOpen);
            location.setSummary(isOpen ? "来电归属地显示已打开" : "来电归属地显示已关闭");
            Intent showAddressIntent = new Intent(getActivity(), AddressService.class);
            if (!isOpen) {
                mActivity.stopService(showAddressIntent);
            } else {
                mActivity.startService(showAddressIntent);
            }
        }

        if (preference.getTitle().equals(getString(R.string.setting_app_lock))) {
            SharedPreferenceUtil.setPrivacyPrefs(isOpen);
            appLock.setSummary(isOpen ? "隐私保护已打开" : "隐私保护已关闭");
            Intent privacyIntent = new Intent(getActivity(), PrivacyService.class);
            if (!isOpen) {
                mActivity.stopService(privacyIntent);
            } else {
                mActivity.startService(privacyIntent);
            }
        }
        return true;
    }
}
