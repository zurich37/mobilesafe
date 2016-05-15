package com.zurich.mobile.controller;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zurich.mobile.R;

import java.io.Serializable;

import me.xiaopan.psts.PagerSlidingTabStrip;

public class SkinTabViewFactory implements PagerSlidingTabStrip.TabViewFactory, Serializable {
    private Context context;
    private String[] titles;

	public SkinTabViewFactory(Context context, String[] titles) {
        this.context = context;
        this.titles = titles;
    }

    @Override
    public void addTabs(ViewGroup parent, int currentItemPosition) {
        // 先清除已有的Tab View
        parent.removeAllViews();

		ColorStateList colorStateList;
        int[][] states = new int[2][];
        int[] colors = new int[2];

        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

		colors[0] = Color.parseColor("#2196F3");
        colors[1] = Color.parseColor("#212121");

		colorStateList = new ColorStateList(states, colors);
        for (int w = 0; w < titles.length; w++) {
            TextView titleTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.tab_category, parent, false);
            String title = titles[w];
            titleTextView.setText(title);
            if (w == currentItemPosition) {
                titleTextView.setSelected(true);
            } else {
                titleTextView.setSelected(false);
            }
            parent.addView(titleTextView);
        }
    }
}
