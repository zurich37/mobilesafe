package com.zurich.mobile.assemblyadapter.shl;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ScrollView;

public class ScrollHeaderLayout extends ViewGroup implements TouchEventProcessor.Callback {
    public static final boolean DEBUG = false;
    private static final String TAG = "ScrollHeaderLayout";

    private ContentViewHolder contentViewHolder;
    private TouchEventProcessor touchEventProcessor;

    private int offset;
    private int headerViewHeight;
    private int titleBarHeight;

    private OnSlideListener onSlideListener;

    public ScrollHeaderLayout(Context context) {
        super(context);
        init(context);
    }

    public ScrollHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        touchEventProcessor = new TouchEventProcessor(context, this);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        // 限制子View数量不能超过2个
        if (getChildCount() >= 2) {
            throw new IllegalStateException("The child View number cannot be more than 2");
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 限制子View数量不能少于2个
        if (getChildCount() < 2) {
            throw new IllegalStateException("Child View number may not be less than 2");
        }

        findContentView();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("widthMode can not is UNSPECIFIED");
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightUsed = 0;
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("heightMode can not is UNSPECIFIED");
        }

        View headerView = getChildAt(0);
        View bodyView = getChildAt(1);

        measureChild(headerView, MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), heightMeasureSpec);
        heightUsed += headerView.getMeasuredHeight();

        measureChild(bodyView, MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - heightUsed, MeasureSpec.EXACTLY) + offset);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View headerView = getChildAt(0);
        View bodyView = getChildAt(1);

        t -= offset;

        l += getPaddingLeft();
        t += getPaddingTop();
        b -= getPaddingBottom();

        int childLeft = l;
        int childTop = t;
        int childRight = childLeft + headerView.getMeasuredWidth();
        int childBottom = childTop + headerView.getMeasuredHeight();
        headerView.layout(childLeft, childTop, childRight, childBottom);
        headerViewHeight = headerView.getHeight();

        childTop += headerView.getMeasuredHeight(); // 垂直排列，因此Top要增加
        childRight = childLeft + bodyView.getMeasuredWidth();
        childBottom = childTop + bodyView.getMeasuredHeight();
        childBottom = Math.min(childBottom, b); // 限制一下底部不能超过范围
        bodyView.layout(childLeft, childTop, childRight, childBottom);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        touchEventProcessor.computeScroll();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (contentViewHolder == null) {
            return false;
        }
        return touchEventProcessor.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return touchEventProcessor.onTouchEvent(event);
    }

    @Override
    public ContentViewHolder getContentViewHolder() {
        return contentViewHolder;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getHeaderHeight() {
        return headerViewHeight - titleBarHeight;
    }

    @Override
    public void updateOffsetIncrement(float offsetIncrement) {
        if(DEBUG){
            Log.d(TAG, "updateOffsetIncrement. offsetIncrement=" + offsetIncrement);
        }
        updateOffset((int) (offset + offsetIncrement));
    }

    @Override
    public void updateOffset(int newOffset) {
        this.offset = newOffset;
        this.offset = Math.min(this.offset, getHeaderHeight());
        this.offset = Math.max(this.offset, 0);
        if(DEBUG){
            Log.d(TAG, "updateOffset. newOffset=" + newOffset + ", offset=" + offset);
        }
        requestLayout();

        if(onSlideListener != null){
            onSlideListener.onSlide(getHeaderHeight(), this.offset);
        }
    }

    @Override
    public boolean isHeaderFullyHidden() {
        return offset >= getHeaderHeight();
    }

    @Override
    public boolean isHeaderFullyDisplay() {
        return offset == 0;
    }

    private void findContentView() {
        contentViewHolder = null;

        if (getChildCount() != 2) {
            return;
        }

        contentViewHolder = findContentView(getChildAt(1));
    }

    public static ContentViewHolder findContentView(View childView){
        if (childView instanceof AbsListView) {
            return new AbsListViewHolder((AbsListView) childView);
        } else if (childView instanceof ScrollView) {
            return new ScrollViewHolder((ScrollView) childView);
        } else if (childView instanceof ViewPager) {
            return new ViewPagerHolder((ViewPager) childView);
//        } else if(childView instanceof RecyclerView){
//            return new RecyclerViewHolder((RecyclerView) childView);
        }if (childView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) childView;
            ContentViewHolder contentViewHolder;
            for (int w = 0, size = viewGroup.getChildCount(); w < size; w++) {
                contentViewHolder = findContentView(viewGroup.getChildAt(w));
                if(contentViewHolder != null){
                    return contentViewHolder;
                }
            }
        }
        return null;
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }

    public void setTitleBarHeight(int titleBarHeight) {
        this.titleBarHeight = titleBarHeight;
    }

    public interface OnSlideListener{
        void onSlide(int headerHeight, int offset);
    }
}
