package com.zurich.mobile.fragment;

import android.os.Bundle;
import android.view.View;

import com.zurich.mobile.R;

/**
 * Created by weixinfei on 2016/3/20.
 */
public class SafeFragment extends AppBaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_safe;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {

    }

    @Override
    public boolean isReadyData() {
        return false;
    }

    @Override
    public void onShowData() {

    }

    @Override
    public void onLoadData() {

    }
}
