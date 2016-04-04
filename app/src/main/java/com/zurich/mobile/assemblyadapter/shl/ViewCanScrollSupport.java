package com.zurich.mobile.assemblyadapter.shl;

import android.os.Build;
import android.view.View;

import java.lang.reflect.Method;

public class ViewCanScrollSupport {
    private static Method computeVerticalScrollOffset;
    private static Method computeVerticalScrollRange;
    private static Method computeVerticalScrollExtent;

    /**
     * Check if this view can be scrolled vertically in a certain direction.
     *
     * @param view View
     * @param direction Negative to check scrolling up, positive to check scrolling down.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    public static boolean canScrollVerticallySupport(View view, int direction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return view.canScrollVertically(direction);
        }

        if(computeVerticalScrollOffset == null){
            computeVerticalScrollOffset = findMethod(view.getClass(), "computeVerticalScrollOffset");
            if(computeVerticalScrollOffset != null){
                computeVerticalScrollOffset.setAccessible(true);
            }else{
                return false;
            }
        }
        if(computeVerticalScrollRange == null){
            computeVerticalScrollRange = findMethod(view.getClass(), "computeVerticalScrollRange");
            if(computeVerticalScrollRange != null){
                computeVerticalScrollRange.setAccessible(true);
            }else{
                return false;
            }
        }
        if(computeVerticalScrollExtent == null){
            computeVerticalScrollExtent = findMethod(view.getClass(), "computeVerticalScrollExtent");
            if(computeVerticalScrollExtent != null){
                computeVerticalScrollExtent.setAccessible(true);
            }else{
                return false;
            }
        }
        int scrollOffset;
        try {
            scrollOffset = (Integer) computeVerticalScrollOffset.invoke(view);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        int scrollRange;
        try {
            scrollRange = (Integer) computeVerticalScrollRange.invoke(view);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        int scrollExtent;
        try {
            scrollExtent = (Integer) computeVerticalScrollExtent.invoke(view);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        final int range = scrollRange - scrollExtent;
        if (range == 0) return false;
        if (direction < 0) {
            return scrollOffset > 0;
        } else {
            return scrollOffset < range - 1;
        }
    }

    private static Method findMethod(Class<?> clas, String methodName){
        while(clas != null){
            try {
                return clas.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                clas = clas.getSuperclass();
            }
        }
        return null;
    }
}
