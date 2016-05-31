package com.zurich.mobile.net;

/**
 * Created by weixinfei on 16/5/30.
 */
public class HttpResult<T> {

    private boolean resultCode;

    private T results;

    public boolean isResultCode() {
        return resultCode;
    }

    public T getResults() {
        return results;
    }

}
