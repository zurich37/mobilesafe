package com.zurich.mobile.assemblyadapter.shl;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ScrollView;

public class ScrollViewHolder implements ContentViewHolder{
    private ScrollView scrollView;

    public ScrollViewHolder(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public boolean isContentStayTheTop() {
        return !ViewCanScrollSupport.canScrollVerticallySupport(scrollView, -1);
    }

    @Override
    public boolean isParentSwipeRefresh() {
        return scrollView.getParent() instanceof SwipeRefreshLayout;
    }
}