package com.zurich.mobile.utils;

import android.view.View;
import android.widget.AbsListView;

public abstract class ListHeadScrollListener implements AbsListView.OnScrollListener{
    private boolean out;
    private int headHeight = -1;
    private int scrollDistance = -1;
    private int toolbarHeight;
    private float lastPercent = -1;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0) {
            out = false;
            View firstChildView = view.getChildAt(0);
            if (firstChildView != null) {
                scrollDistance = Math.abs(firstChildView.getTop());
                headHeight = firstChildView.getHeight() - toolbarHeight;
                float percent = (float) scrollDistance / headHeight;
                percent = Math.max(percent, 0);
                percent = Math.min(percent, 1);

                if(lastPercent != percent){
                    onHeadScroll(headHeight, scrollDistance, percent);
                    lastPercent = percent;
                }
            }
        } else {
            if(!out && headHeight != -1 && scrollDistance != -1){
                out = true;
                float percent = 1;
                if(lastPercent != percent){
                    onHeadScroll(headHeight, scrollDistance, percent);
                    lastPercent = percent;
                }
            }
        }
    }

    public float getPercent() {
        return lastPercent;
    }

    public ListHeadScrollListener setToolbarHeight(int toolbarHeight) {
        this.toolbarHeight = toolbarHeight;
        return this;
    }

    /**
     * 列表头滚动了
     * @param headHeight 列表头的高度
     * @param scrollDistance 滚动距离
     * @param percent 滚动距离相对于列表头高度的百分比
     */
    protected abstract void onHeadScroll(int headHeight, int scrollDistance, float percent);
}
