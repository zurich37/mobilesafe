package com.zurich.mobile.view.kbv;

import android.graphics.RectF;

/**
 * Created by weixinfei on 16/3/30.
 */
public class MathUtils {
    public MathUtils() {
    }

    protected static float getRectRatio(RectF var0) {
        return var0.width() / var0.height();
    }

    protected static boolean haveSameAspectRatio(RectF var0, RectF var1) {
        return Math.abs(truncate(getRectRatio(var0), 2) - truncate(getRectRatio(var1), 2)) <= 0.01F;
    }

    protected static float truncate(float var0, int var1) {
        float var2 = (float)Math.pow(10.0D, (double)var1);
        return (float)Math.round(var0 * var2) / var2;
    }

}
