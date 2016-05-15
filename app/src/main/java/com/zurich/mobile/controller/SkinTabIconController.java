package com.zurich.mobile.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.widget.RadioButton;

import com.zurich.mobile.skin.SkinManager;

import java.util.LinkedList;
import java.util.List;

public class SkinTabIconController {
    private Context context;
    private List<Item> itemList;
    private List<Bitmap> bitmapList;

    public SkinTabIconController(Context context) {
        this.context = context;
    }

    public void addTab(RadioButton tabButton, int normalIconResId) {
        if (itemList == null) {
            itemList = new LinkedList<Item>();
        }
        itemList.add(new Item(tabButton, normalIconResId));
    }

    public void resetSkin(){
        init();
    }

    public void init() {
        int mainColor = SkinManager.with(context).getSkin().getPrimaryColor();
        if (itemList == null) {
            return;
        }
        if(bitmapList != null && bitmapList.size() > 0){
            for(Bitmap bitmap : bitmapList){
                if(bitmap != null){
                    bitmap.recycle();
                }
            }
            bitmapList.clear();
        }
        for (Item item : itemList) {
            StateListDrawable recommendDrawable = new StateListDrawable();
            Context context = item.tabButton.getContext();
            Drawable normalDrawable = context.getResources().getDrawable(item.normalIconResId);

            recommendDrawable.addState(new int[]{}, normalDrawable);
            item.tabButton.setCompoundDrawablesWithIntrinsicBounds(null, recommendDrawable, null, null);

            int[][] states = new int[2][];
            int[] colors = new int[2];

            states[0] = new int[]{android.R.attr.state_checked};
            states[1] = new int[]{};

            colors[0] = mainColor;
            colors[1] = Color.parseColor("#717171");

//            item.tabButton.setTextColor(new ColorStateList(states, colors));
        }
    }

    public void destroy(){
        if(bitmapList != null && bitmapList.size() > 0){
            for(Bitmap bitmap : bitmapList){
                if(bitmap != null){
                    bitmap.recycle();
                }
            }
            bitmapList.clear();
        }
    }

    private BitmapDrawable setTint(BitmapDrawable drawable, int tintColor){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            drawable.setTint(tintColor);
            return drawable;
        }else{
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();

            int x = 0;
            int y = 0;
            int sc = canvas.saveLayer(x, y, x + bitmap.getWidth(), y + bitmap.getHeight(), null,
                    Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                            | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                            | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            paint.setColor(Color.WHITE);
            canvas.drawBitmap(drawable.getBitmap(), 0, 0, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            paint.setColor(tintColor);
            canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
            paint.setXfermode(null);
            canvas.restoreToCount(sc);
            if(bitmapList == null){
                bitmapList = new LinkedList<Bitmap>();
            }
            bitmapList.add(bitmap);
            return new BitmapDrawable(bitmap);
        }
    }

    public static class Item {
        private RadioButton tabButton;
        private int normalIconResId;

        public Item(RadioButton tabButton, int normalIconResId) {
            this.tabButton = tabButton;
            this.normalIconResId = normalIconResId;
        }
    }
}
