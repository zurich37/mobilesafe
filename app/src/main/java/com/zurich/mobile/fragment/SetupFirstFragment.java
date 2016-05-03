package com.zurich.mobile.fragment;

import android.os.Bundle;
import android.view.View;

import com.zurich.mobile.R;

/**
 * Created by weixinfei on 16/4/24.
 */
public class SetupFirstFragment extends AppBaseFragment {
    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_setup1;
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
