package com.zurich.mobile.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import me.xiaopan.psts.PagerSlidingTabStrip;

public class SkinTabStripController {
    private Context context;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private boolean bigTabStrip;

    public SkinTabStripController(Context context, PagerSlidingTabStrip pagerSlidingTabStrip) {
        this.context = context;
        this.pagerSlidingTabStrip = pagerSlidingTabStrip;
    }

    public void resetSkin(){
        int slidBlockWidth;
        if(bigTabStrip){
            slidBlockWidth = (int) ((60 * context.getResources().getDisplayMetrics().density) + 0.5);
        }else{
            slidBlockWidth = (int) ((33 * context.getResources().getDisplayMetrics().density) + 0.5);
        }
        int slidBlockHeight = (int) ((2 * context.getResources().getDisplayMetrics().density) + 0.5);
        RectShape shape = new RectShape();
        shape.resize(slidBlockWidth, slidBlockHeight);
        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.getPaint().setColor(Color.parseColor("#2196F3"));
        drawable.setIntrinsicWidth(slidBlockWidth);
        drawable.setIntrinsicHeight(slidBlockHeight);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        pagerSlidingTabStrip.setSlidingBlockDrawable(drawable);
        pagerSlidingTabStrip.setBackgroundColor(Color.WHITE);
    }

    public void setStripBackgroudDrawable(Drawable drawable) {
        pagerSlidingTabStrip.setBackgroundDrawable(drawable);
    }

    public void setStripSlidingBlockColor(int color) {
    }

    public void setBigTabStrip(boolean bigTabStrip) {
        this.bigTabStrip = bigTabStrip;
    }
}