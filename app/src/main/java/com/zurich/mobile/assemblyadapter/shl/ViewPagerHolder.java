package com.zurich.mobile.assemblyadapter.shl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerHolder implements ContentViewHolder {
    private ViewPager viewPager;
    private int currentPageIndex = -1;
    private ContentViewHolder contentViewHolder;

    public ViewPagerHolder(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public boolean isContentStayTheTop() {
        ContentViewHolder contentViewHolder = findContentView();
        return contentViewHolder != null && contentViewHolder.isContentStayTheTop();
    }

    @Override
    public boolean isParentSwipeRefresh() {
        ContentViewHolder contentViewHolder = findContentView();
        return contentViewHolder != null && contentViewHolder.isParentSwipeRefresh();
    }

    private ContentViewHolder findContentView(){
        PagerAdapter adapter = viewPager.getAdapter();
        if(adapter == null){
            return null;
        }

        if(adapter instanceof FragmentPagerAdapter){
            int newPageIndex = viewPager.getCurrentItem();
            if(currentPageIndex != newPageIndex){
                currentPageIndex = newPageIndex;
                contentViewHolder = null;
                FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) adapter;
                Fragment fragment = fragmentPagerAdapter.getItem(currentPageIndex);
                View fragmentView = fragment.getView();
                if(fragmentView != null){
                    contentViewHolder = ScrollHeaderLayout.findContentView(fragmentView);
                }
            }
            return contentViewHolder;
        }

        return null;
    }
}
