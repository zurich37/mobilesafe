package com.zurich.mobile.retrofit;


import com.zurich.mobile.entity.GankResult;
import com.zurich.mobile.net.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by weixinfei on 16/5/27.
 */
public interface GankService {
    @GET("api/data/{type}/{count}/{page}")
    Observable<HttpResult<List<GankResult>>> getGankInfo(
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page
    );
}
