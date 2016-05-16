package com.zurich.mobile.adapter.assemblyadapter.shl;//package me.xiaopan.shl;
//
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.RecyclerView;
//
//public class RecyclerViewHolder implements ContentViewHolder{
//    private RecyclerView recyclerView;
//
//    public RecyclerViewHolder(RecyclerView recyclerView) {
//        this.recyclerView = recyclerView;
//    }
//
//    @Override
//    public boolean isContentStayTheTop() {
//        return !ViewCanScrollSupport.canScrollVerticallySupport(recyclerView, -1);
//    }
//
//    @Override
//    public boolean isParentSwipeRefresh() {
//        return recyclerView.getParent() instanceof SwipeRefreshLayout;
//    }
//}