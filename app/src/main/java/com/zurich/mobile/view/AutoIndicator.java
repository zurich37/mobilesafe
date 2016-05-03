package com.zurich.mobile.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zurich.mobile.utils.GlobalUtils;

/**
 * 指针项目
 * Created by weixinfei on 16/4/26.
 */
public class AutoIndicator extends LinearLayout{
    public AutoIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AutoIndicator(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * 添加指定个数的指示器
     *
     * @param count
     */
    public void setIndicatorCount(int count) {
        removeAllViews();
        while (count > 0) {
            ImageView iv = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(GlobalUtils.convertDiptoPx(
                    getContext(), 15), GlobalUtils.convertDiptoPx(getContext(),
                    2));
            params.setMargins(5, 0, 5, 0);
            iv.setLayoutParams(params);
            addView(iv);
            count--;
        }
    }

    /**
     * 设置选中的指示器
     *
     * @param index
     */
    public void setSelectedIndicator(int index) {
        int count = getChildCount();
        ImageView child = null;
        for (int i = 0; i < count; i++) {
            child = (ImageView) getChildAt(i);
            if (index == i) {
                child.setImageDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            } else {
                child.setImageDrawable(new ColorDrawable(Color.parseColor("#3F000000")));
            }
        }
    }

}
