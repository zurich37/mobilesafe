package com.zurich.mobile.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by weixinfei on 16/5/15.
 */
public class BFImageCache implements ImageLoader.ImageCache {
    //LruCache对象
    private LruCache<String, Bitmap> lruCache ;
    //设置最大缓存为10Mb，大于这个值会启动自动回收
    private int max = 10*1024*1024;

    public BFImageCache(){
        //初始化 LruCache
        lruCache = new LruCache<String, Bitmap>(max){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url) {
        return lruCache.get(url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }
}