package com.zurich.mobile.adapter.assemblyadapter.shl;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;

public class AbsListViewHolder implements ContentViewHolder {
    private AbsListView absListView;

    public AbsListViewHolder(AbsListView absListView) {
        this.absListView = absListView;
    }

    @Override
    public boolean isContentStayTheTop() {
        return !ViewCanScrollSupport.canScrollVerticallySupport(absListView, -1);
    }

    @Override
    public boolean isParentSwipeRefresh() {
        return absListView.getParent() instanceof SwipeRefreshLayout;
    }
}
