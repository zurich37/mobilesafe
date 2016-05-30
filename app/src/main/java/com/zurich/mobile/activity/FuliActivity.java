package com.zurich.mobile.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.zurich.mobile.adapter.assemblyadapter.OnRecyclerLoadMoreListener;
import com.zurich.mobile.adapter.itemfactory.FuliInfoItemFactory;
import com.zurich.mobile.adapter.itemfactory.LoadMoreRecyclerListItemFactory;
import com.zurich.mobile.model.GankInfo;
import com.zurich.mobile.retrofit.GankRetrofit;
import com.zurich.mobile.retrofit.GankService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 福利页面
 * Created by weixinfei on 16/5/30.
 */
public class FuliActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, FuliInfoItemFactory.FuliEventListener, OnRecyclerLoadMoreListener {
    private Toolbar mToolbar;
    private EasyRecyclerView recyclerFuli;
    private List<GankInfo.ResultsBean> gankInfos;
    private AssemblyRecyclerAdapter mAdapter;
    private int mPage = 1;
    private GankService gankService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli);
        ButterKnife.bind(this);

        initToolbar();
        initView();
        initAdapter();
        onRefresh();
    }

    private void initView() {

        recyclerFuli = (EasyRecyclerView) findViewById(R.id.recycler_fuli);
        recyclerFuli.setRefreshListener(this);

        gankInfos = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerFuli.setLayoutManager(staggeredGridLayoutManager);
        recyclerFuli.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_fuli);
        mToolbar.setTitle(getResources().getString(R.string.manage_center_meizhi));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_back_normal);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //设置menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化adaptor
     */
    public void initAdapter(){
        if (mAdapter == null){
            mAdapter = new AssemblyRecyclerAdapter(gankInfos);
            mAdapter.addItemFactory(new FuliInfoItemFactory(this));
            mAdapter.enableLoadMore(new LoadMoreRecyclerListItemFactory(FuliActivity.this));
            refreshAdapter();
            recyclerFuli.setAdapter(mAdapter);
        }
    }

    /**
     * 获取福利
     */
    public void getMeizhi(int count, int page) {

        gankService = GankRetrofit.getRetrofit().create(GankService.class);

        gankService.getGankInfo("福利", count, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(recyclerFuli,"NO WIFI，不能愉快的看妹纸啦..",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(GankInfo gankInfo) {
                        gankInfos = gankInfo.getResults();
                        if (gankInfos != null && gankInfos.size() > 0)
                            mAdapter.append(gankInfos);
                        if (gankInfo.getResults().size() < 20)
                            mAdapter.loadMoreEnd();
                        else
                            mAdapter.loadMoreFinished();
                        refreshAdapter();
                    }
                });
    }

    @Override
    public void onRefresh() {
        gankInfos.clear();
        getMeizhi(20, 1);
        mPage = 1;
    }

    private void refreshAdapter() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMeizhiClick(GankInfo.ResultsBean gankInfo) {

    }

    @Override
    public void onLoadMore(AssemblyRecyclerAdapter adapter) {
        mPage++;
        getMeizhi(20, mPage);
    }
}
