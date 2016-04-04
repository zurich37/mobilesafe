package com.zurich.mobile.assemblyadapter.shl;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Scroller;

public class TouchEventProcessor implements GestureDetector.OnGestureListener {
    private static final String TAG = "TouchEventProcessor";

    private Callback callback;
    private int touchSlop;
    private GestureDetector gestureDetector;
    private boolean isBeingDragged;
    private float distanceYCount;
    private Scroller scroller;
    private boolean scrolling;

    public TouchEventProcessor(Context context, Callback callback) {
        this.callback = callback;

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        gestureDetector = new GestureDetector(context, this);
        scroller = new Scroller(context);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        if (ScrollHeaderLayout.DEBUG) {
            Log.d(TAG, "GestureDetector. onDown, x=" + event.getX() + ", y=" + event.getY());
        }
        isBeingDragged = false;
        distanceYCount = 0;
        if (!scroller.isFinished()) {
            if (ScrollHeaderLayout.DEBUG) {
                Log.w(TAG, "scroll abort");
            }
            scrolling = false;
            scroller.abortAnimation();
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        distanceYCount += distanceY;

        if (isBeingDragged) {
            callback.updateOffsetIncrement(distanceY);
            return true;
        } else if (Math.abs(distanceYCount) >= touchSlop) {
            if (distanceYCount >= 0) {
                // 上滑的时候如果头部尚未全部隐藏就拦截事件先隐藏头部再说
                if (!callback.isHeaderFullyHidden()) {
                    if (ScrollHeaderLayout.DEBUG) {
                        Log.e(TAG, "intercepted. 1");
                    }
                    isBeingDragged = true;
                    return true;
                } else {
                    if (ScrollHeaderLayout.DEBUG) {
                        Log.e(TAG, "intercepted. 2");
                    }
                }
            } else {
                if (callback.isHeaderFullyHidden()) {
                    // 下滑的时候如果头部已经全部隐藏，并且列表也停在顶部了，那么就需要下拉显示头部
                    if (callback.getContentViewHolder().isContentStayTheTop()) {
                        if (ScrollHeaderLayout.DEBUG) {
                            Log.e(TAG, "intercepted. 3");
                        }
                        isBeingDragged = true;
                        return true;
                    } else {
                        if (ScrollHeaderLayout.DEBUG) {
                            Log.e(TAG, "intercepted. 4");
                        }
                    }
                } else if (callback.isHeaderFullyDisplay()) {
                    // 下滑的时候如果头部已经全部显示出来了，并且列表也停在顶部了
                    if (callback.getContentViewHolder().isContentStayTheTop()) {
                        // 如果ContentView的父布局不是一个下拉刷新控件，那么就拦截事件
                        if (!callback.getContentViewHolder().isParentSwipeRefresh()) {
                            if (ScrollHeaderLayout.DEBUG) {
                                Log.e(TAG, "intercepted. 5");
                            }
                            isBeingDragged = true;
                            return true;
                        } else {
                            if (ScrollHeaderLayout.DEBUG) {
                                Log.e(TAG, "intercepted. 6");
                            }
                        }
                    } else {
                        if (ScrollHeaderLayout.DEBUG) {
                            Log.e(TAG, "intercepted. 7");
                        }
                    }
                } else {
                    // 下滑的时候如果头部没有全部隐藏，也没有完全显示出来，那么就需要下拉显示头部
                    if (ScrollHeaderLayout.DEBUG) {
                        Log.e(TAG, "intercepted. 8");
                    }
                    isBeingDragged = true;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (ScrollHeaderLayout.DEBUG) {
            Log.d(TAG, "GestureDetector. onFling, velocityX=" + velocityX + ", velocityY=" + velocityY);
        }
        if (isBeingDragged) {
            if (!scroller.isFinished()) {
                scroller.abortAnimation();
            }
            scroller.fling(0, callback.getOffset(), 0, (int) velocityY * -1, 0, 0, 0, callback.getHeaderHeight());
            scrolling = true;
            callback.postInvalidate();
            return true;
        }
        return false;
    }

    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (ScrollHeaderLayout.DEBUG) {
                Log.i(TAG, "computeScroll. currentY=" + scroller.getCurrY());
            }
            callback.updateOffset(scroller.getCurrY());
            callback.postInvalidate();
        } else {
            if (scrolling) {
                scrolling = false;
                if (ScrollHeaderLayout.DEBUG) {
                    Log.w(TAG, "computeScroll. end");
                }
            } else {
                if (ScrollHeaderLayout.DEBUG) {
                    Log.e(TAG, "computeScroll. end");
                }
            }
        }
    }

    public interface Callback {
        ContentViewHolder getContentViewHolder();

        void updateOffsetIncrement(float offsetIncrement);

        void updateOffset(int newOffset);

        boolean isHeaderFullyHidden();

        boolean isHeaderFullyDisplay();

        void postInvalidate();

        int getOffset();

        int getHeaderHeight();
    }
}
