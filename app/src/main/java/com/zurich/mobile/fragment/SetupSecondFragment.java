package com.zurich.mobile.fragment;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.SharedPreferenceUtil;
import com.zurich.mobile.widget.SettingLayout;

/**
 * 绑定sim卡页面
 * Created by weixinfei on 16/4/24.
 */
public class SetupSecondFragment extends AppBaseFragment{

    private SettingLayout slSetupBind;
    private TelephonyManager tm;
    private String bindNumber;
    private String simSerialNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tm = (TelephonyManager) getContext().getSystemService(getContext().TELEPHONY_SERVICE);
        simSerialNumber = tm.getSimSerialNumber();
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_setup2;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {
        slSetupBind = (SettingLayout)findViewById(R.id.sl_setup_bind);
        slSetupBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slSetupBind.getCheckBox()){
                    slSetupBind.setCheckBox(false);
                    slSetupBind.setTvSubName("没有绑定sim卡");
                    SharedPreferenceUtil.setBindNumberPrefs(getContext(), "sim", null);
                }else {
                    slSetupBind.setCheckBox(true);
                    slSetupBind.setTvSubName("已绑定sim卡");
                    SharedPreferenceUtil.setBindNumberPrefs(getContext(), "sim", simSerialNumber);
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
        if(!TextUtils.isEmpty(bindNumber)){
            slSetupBind.setCheckBox(true);
            slSetupBind.setTvSubName("已绑定sim卡");
        }else{
            slSetupBind.setCheckBox(false);
        }
    }

    @Override
    public void onLoadData() {
        bindNumber = SharedPreferenceUtil.getBindNumberPrefs(getContext(), "sim", null);
        showData();
    }
}
