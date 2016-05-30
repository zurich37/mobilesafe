package com.zurich.mobile.retrofit;


import com.zurich.mobile.model.GankInfo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by weixinfei on 16/5/27.
 */
public interface GankService {
    /*@GET("api/data/{type}/{count}/{page}")
    Call<GankInfo> getGankInfo(@Path("type") String type,
                             @Path("count") int count,
                             @Path("page") int page);*/
    @GET("api/data/{type}/{count}/{page}")
    Observable<GankInfo> getGankInfo(
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page
    );
}
