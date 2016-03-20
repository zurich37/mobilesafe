package com.zurich.mobile.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;

public class FragmentSwitchController {
    private ViewPager viewPager;
    private CompoundButton[] compoundButtons;
    private OnTabClickListener onTabClickListener;
    private OnPageChangedListener onPageChangedListener;

    public FragmentSwitchController(ViewPager viewPager, CompoundButton... compoundButtons) {
        this.viewPager = viewPager;
        this.compoundButtons = compoundButtons;
        init();
    }

    private void init(){
        // 点击TAB的时候切换页面
        View.OnClickListener tabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                viewPager.setCurrentItem(position);
                if(onTabClickListener != null){
                    onTabClickListener.onTabClick(position);
                }
            }
        };
        for(int w = 0, size = compoundButtons.length; w < size; w++){
            CompoundButton tabButton = compoundButtons[w];
            tabButton.setTag(w);
            tabButton.setOnClickListener(tabClickListener);
        }

        // 页面切换的时候改变相应TAB的状态
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                compoundButtons[position].setChecked(true);
                if (onPageChangedListener != null) {
                	onPageChangedListener.onPageChanged(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setChecked(int position){
        compoundButtons[position].setChecked(true);
        viewPager.setCurrentItem(position, false);
    }
    
    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
		this.onPageChangedListener = onPageChangedListener;
	}
    
    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }

    public int getCount(){
        return viewPager.getAdapter().getCount();
    }

    public Fragment getCurrentFragment(){
        return ((FragmentPagerAdapter)viewPager.getAdapter()).getItem(viewPager.getCurrentItem());
    }
    
    public interface OnPageChangedListener {
    	void onPageChanged(int position);
    }

    public interface OnTabClickListener {
        void onTabClick(int position);
    }
}
