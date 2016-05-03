package com.zurich.mobile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;
import com.zurich.mobile.R;

/**
 * 设置界面组件
 * Created by weixinfei on 2016/3/20.
 */
public class SettingLayout extends RelativeLayout {
    private TextView tvName;
    private ImageView ivIcon;
    private CheckBox checkBox;
    private TextView tvSubName;
    public SettingLayout(Context context) {
        super(context);
        initData(context, null);
    }

    public SettingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
    }

    public SettingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    private void initData(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.widget_setting, this, true);
        tvName = (TextView) layout.findViewById(R.id.setting_name);
        tvSubName = (TextView) layout.findViewById(R.id.setting_sub_name);
        ivIcon = (ImageView) layout.findViewById(R.id.setting_icon);
        checkBox = (CheckBox) layout.findViewById(R.id.setting_check);
        setViewDatas(context, attrs);
    }
    private void setViewDatas(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingLayout);
        int icon =typedArray.getResourceId(R.styleable.SettingLayout_setting_icon,0);
        ivIcon.setBackgroundResource(icon);
        String title = typedArray.getString(R.styleable.SettingLayout_setting_name);
        if (!TextUtils.isEmpty(title)) {
            tvName.setText(title);
        }
        String version =typedArray.getString(R.styleable.SettingLayout_setting_sub_name);
        if (!TextUtils.isEmpty(version)) {
            tvSubName.setText(version);
            tvSubName.setVisibility(VISIBLE);
        }
        typedArray.recycle();
    }

    public void setTvSubName (String subName){
        if (TextUtils.isEmpty(subName))
            return;
        tvSubName.setText(subName);
        tvSubName.setVisibility(VISIBLE);
    }

    public void setCheckBox(boolean isChecked){
        checkBox.setChecked(isChecked);
    }

    public boolean getCheckBox(){
        return checkBox.isChecked();
    }
}
