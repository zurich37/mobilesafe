package com.zurich.mobile.view.kbv;

import android.graphics.RectF;
import android.view.animation.Interpolator;

/**
 * Created by weixinfei on 16/3/30.
 */
public class Transition {
    private float mCenterXDiff;
    private float mCenterYDiff;
    private final RectF mCurrentRect = new RectF();
    private RectF mDstRect;
    private long mDuration;
    private float mHeightDiff;
    private Interpolator mInterpolator;
    private RectF mSrcRect;
    private float mWidthDiff;

    public Transition(RectF var1, RectF var2, long var3, Interpolator var5) {
        if(!MathUtils.haveSameAspectRatio(var1, var2)) {
            throw new IncompatibleRatioException();
        } else {
            this.mSrcRect = var1;
            this.mDstRect = var2;
            this.mDuration = var3;
            this.mInterpolator = var5;
            this.mWidthDiff = var2.width() - var1.width();
            this.mHeightDiff = var2.height() - var1.height();
            this.mCenterXDiff = var2.centerX() - var1.centerX();
            this.mCenterYDiff = var2.centerY() - var1.centerY();
        }
    }

    public RectF getDestinyRect() {
        return this.mDstRect;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public RectF getInterpolatedRect(long var1) {
        float var3 = Math.min((float)var1 / (float)this.mDuration, 1.0F);
        float var5 = this.mInterpolator.getInterpolation(var3);
        var3 = this.mSrcRect.width() + this.mWidthDiff * var5;
        float var4 = this.mSrcRect.height() + this.mHeightDiff * var5;
        float var8 = this.mSrcRect.centerX();
        float var9 = this.mCenterXDiff;
        float var6 = this.mSrcRect.centerY();
        float var7 = this.mCenterYDiff;
        var8 = var8 + var9 * var5 - var3 / 2.0F;
        var5 = var6 + var7 * var5 - var4 / 2.0F;
        this.mCurrentRect.set(var8, var5, var8 + var3, var5 + var4);
        return this.mCurrentRect;
    }

    public RectF getSourceRect() {
        return this.mSrcRect;
    }

}
