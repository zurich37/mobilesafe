package com.zurich.mobile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.rey.material.widget.EditText;
import com.zurich.mobile.R;
import com.zurich.mobile.activity.SelectContactActivity;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.SharedPreferenceUtil;
/**
 * 设置安全号码页面
 * Created by weixinfei on 16/4/24.
 */
public class SetupThridFragment extends AppBaseFragment implements View.OnClickListener {

    public static final int SELECT_CONTACT = 101;
    private EditText etSetupSafeNumber;
    private String safenumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        safenumber = SharedPreferenceUtil.getSafePhoneNumberPrefs(getContext(), "safenumber", "");
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_setup3;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {
        etSetupSafeNumber = (EditText) findViewById(R.id.et_setup3_phone);
        Button btnSelectContact = (Button) findViewById(R.id.btn_setup3_select_contact);
        Button btnConfirm = (Button) findViewById(R.id.btn_setup3_confirm);
        btnSelectContact.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public boolean isReadyData() {
        return false;
    }

    @Override
    public void onShowData() {
        if (safenumber != null)
            etSetupSafeNumber.setText(safenumber);
    }

    @Override
    public void onLoadData() {
        showData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_setup3_select_contact){
            Intent intent = new Intent(getActivity(), SelectContactActivity.class);
            startActivityForResult(intent, SELECT_CONTACT);
        }else if (v.getId() == R.id.btn_setup3_confirm){
            String res = etSetupSafeNumber.getText().toString();
            if (TextUtils.isEmpty(res))
                GlobalUtils.showToast(getContext(), "安全号码不能为空");
            else {
                SharedPreferenceUtil.setSafePhoneNumberPrefs(getContext(), "safenumber", res);
                GlobalUtils.showToast(getContext(), "成功保存");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CONTACT && resultCode == Activity.RESULT_OK){
            safenumber = data.getStringExtra("contact_number");
        }
    }
}
