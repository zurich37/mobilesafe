package com.zurich.mobile.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by weixinfei on 16/3/30.
 */
public class MaterialDesignIconsTextView extends TextView{
    private static Typeface sMaterialDesignIcons;

    public MaterialDesignIconsTextView(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public MaterialDesignIconsTextView(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public MaterialDesignIconsTextView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        if(!this.isInEditMode()) {
            this.setTypeface();
        }
    }

    private void setTypeface() {
        if(sMaterialDesignIcons == null) {
            sMaterialDesignIcons = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/MaterialDesignIcons.ttf");
        }

        this.setTypeface(sMaterialDesignIcons);
    }

}
