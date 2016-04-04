package com.zurich.mobile.view.kbv;

import android.graphics.RectF;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.Random;

/**
 * Created by weixinfei on 16/3/30.
 */
public class RandomTransitionGenerator implements TransitionGenerator{
    public static final int DEFAULT_TRANSITION_DURATION = 10000;
    private static final float MIN_RECT_FACTOR = 0.75F;
    private RectF mLastDrawableBounds;
    private Transition mLastGenTrans;
    private final Random mRandom;
    private long mTransitionDuration;
    private Interpolator mTransitionInterpolator;

    public RandomTransitionGenerator() {
        this(10000L, new AccelerateDecelerateInterpolator());
    }

    public RandomTransitionGenerator(long var1, Interpolator var3) {
        this.mRandom = new Random(System.currentTimeMillis());
        this.setTransitionDuration(var1);
        this.setTransitionInterpolator(var3);
    }

    private RectF generateRandomRect(RectF var1, RectF var2) {
        if(MathUtils.getRectRatio(var1) > MathUtils.getRectRatio(var2)) {
            var2 = new RectF(0.0F, 0.0F, var1.height() / var2.height() * var2.width(), var1.height());
        } else {
            var2 = new RectF(0.0F, 0.0F, var1.width(), var1.width() / var2.width() * var2.height());
        }

        float var4 = 0.75F + 0.25F * MathUtils.truncate(this.mRandom.nextFloat(), 2);
        float var3 = var4 * var2.width();
        var4 *= var2.height();
        int var5 = (int)(var1.width() - var3);
        int var6 = (int)(var1.height() - var4);
        if(var5 > 0) {
            var5 = this.mRandom.nextInt(var5);
        } else {
            var5 = 0;
        }

        if(var6 > 0) {
            var6 = this.mRandom.nextInt(var6);
        } else {
            var6 = 0;
        }

        return new RectF((float)var5, (float)var6, (float)var5 + var3, (float)var6 + var4);
    }

    public Transition generateNextTransition(RectF var1, RectF var2) {
        boolean var3;
        if(this.mLastGenTrans == null) {
            var3 = true;
        } else {
            var3 = false;
        }

        boolean var5 = true;
        boolean var4 = true;
        RectF var6 = null;
        if(!var3) {
            var6 = this.mLastGenTrans.getDestinyRect();
            if(var1.equals(this.mLastDrawableBounds)) {
                var3 = false;
            } else {
                var3 = true;
            }

            if(MathUtils.haveSameAspectRatio(var6, var2)) {
                var4 = false;
                var5 = var3;
            } else {
                var4 = true;
                var5 = var3;
            }
        }

        if(var6 == null || var5 || var4) {
            var6 = this.generateRandomRect(var1, var2);
        }

        this.mLastGenTrans = new Transition(var6, this.generateRandomRect(var1, var2), this.mTransitionDuration, this.mTransitionInterpolator);
        this.mLastDrawableBounds = var1;
        return this.mLastGenTrans;
    }

    public void setTransitionDuration(long var1) {
        this.mTransitionDuration = var1;
    }

    public void setTransitionInterpolator(Interpolator var1) {
        this.mTransitionInterpolator = var1;
    }

}
