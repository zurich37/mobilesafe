package com.zurich.mobile.view.kbv;

/**
 * Created by weixinfei on 16/3/30.
 */
public class IncompatibleRatioException extends RuntimeException{
    private static final long serialVersionUID = 234608108593115395L;

    public IncompatibleRatioException() {
        super("Can\'t perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}
