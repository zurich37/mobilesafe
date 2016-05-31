package com.zurich.mobile.net;

import com.zurich.mobile.Exception.ApiException;
import com.zurich.mobile.entity.GankResult;
import com.zurich.mobile.retrofit.GankService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 网络请求封装
 * Created by weixinfei on 16/5/30.
 */
public class HttpMethods {
    public static final String BASE_URL = "https://gank.io/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private GankService gankService;

    //构造方法私有
    private HttpMethods(){
        //手动创建OKHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        gankService = retrofit.create(GankService.class);
    }

    /**
     * 访问HttpMethods时创建单例
     */
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     *
     * @param subscriber 由调用者传的观察者对象
     * @param count 请求长度
     * @param page 请求页
     */
    public void getGankInfo(Subscriber<List<GankResult>> subscriber, int count, int page){
        gankService.getGankInfo("福利", count, page)
                .map(new HttpResultFunc<List<GankResult>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T>{

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.isResultCode()){
                throw new ApiException(httpResult.isResultCode());
            }
            return httpResult.getResults();
        }
    }
}
