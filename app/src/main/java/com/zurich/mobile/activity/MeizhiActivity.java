package com.zurich.mobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zurich.mobile.R;
import com.zurich.mobile.utils.ImageUtil;
import com.zurich.mobile.utils.ShareUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 妹纸详情页
 * Created by weixinfei on 16/5/30.
 */
public class MeizhiActivity extends BaseActivity {
    @Bind(R.id.tool_bar_fuli_detail)
    Toolbar mToolbar;
    @Bind(R.id.ivMeizhi)
    ImageView ivMeizhi;

    private String desc;
    private String url;
    private PhotoViewAttacher attacher;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_detail);
        ButterKnife.bind(this);

        initView();
        initToolbar();
        initData();
    }

    private void initView() {
        Intent intent = getIntent();
        desc = intent.getStringExtra("desc");
        url = intent.getStringExtra("url");
    }

    private void initData() {
        if (!TextUtils.isEmpty(url)){
            attacher = new PhotoViewAttacher(ivMeizhi);
            Glide.with(this)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ivMeizhi.setImageBitmap(resource);
                            attacher.update();
                            bitmap = resource;
                        }
                    });
        }
    }

    private void initToolbar(){
        mToolbar.setTitle(desc);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_back_normal);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizhi,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_share:
                ShareUtil.shareImage(this, ImageUtil.saveImage(this, url, bitmap, ivMeizhi,"share"));
                break;
            case R.id.action_save:
                ImageUtil.saveImage(this, url,bitmap,ivMeizhi,"save");
                break;
            case R.id.action_click_me:
                Snackbar.make(ivMeizhi,"福利多多...",Snackbar.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
