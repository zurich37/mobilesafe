package com.zurich.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.zurich.mobile.R;

/**
 * 各页面提示view
 * Created by weixinfei on 16/5/10.
 */
public class HintView extends FrameLayout{

    private ViewStub loadingViewStub;
    private ViewGroup loadingRootView;
    private ProgressBar loadingPbView;

    public static final int DEFAULT = 0;
    public static final int LOADING = 1;
    public static final int FAILURE = 2;
    public static final int EMPTY = 3;
    private int currentStatus = DEFAULT;
    private static final int MINIMUM_DURATION = 2000;
    private long loadingTime;

    private Runnable delayAnimationHiddenRunnable;

    private HintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public HintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HintView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        // 添加加载中子页面
        loadingViewStub = new ViewStub(getContext(), R.layout.widget_loading);
        addView(loadingViewStub);

        setVisibility(GONE);
    }

    /**
     * 显示加载中页面，默认提示信息为“加载中…”
     */
    public LoadingBuilder loading() {
        currentStatus = LOADING;
        return new LoadingBuilder(this);
    }

    public boolean isShowing(){
        return currentStatus == LOADING;
    }

    public static class LoadingBuilder {
        private HintView hintView;
        private String message;
        private String tips;

        private LoadingBuilder(HintView hintView) {
            this.hintView = hintView;
            this.message = "加载中…";
        }

        /**
         * 设置提示消息
         */
        public LoadingBuilder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置小提示
         */
        public LoadingBuilder tips(String tips) {
            this.tips = tips;
            return this;
        }

        /**
         * 显示
         */
        public void show() {
            // 如果是第一次执行此方法就始化加载中部分的View
            if (hintView.loadingViewStub != null) {
                View rootView = hintView.loadingViewStub.inflate();
                hintView.loadingViewStub = null;

                hintView.loadingPbView = (ProgressBar) hintView.findViewById(R.id.pb_loading);

                hintView.loadingRootView = (ViewGroup) rootView;
            }
            hintView.loadingRootView.setVisibility(View.VISIBLE);
            hintView.setVisibility(VISIBLE);
            hintView.setClickable(true);
            hintView.loadingTime = System.currentTimeMillis();
        }
    }

    /**
     * 隐藏提示视图，默认通过渐隐动画慢慢的隐藏
     */
    public void hidden() {
        if (currentStatus == DEFAULT)
            return;
        // 如果从loading开始到现在的时间小于小于MINIMUM_DURATION就再等一会儿
        long currentTime = System.currentTimeMillis();
        if (currentTime - loadingTime >= MINIMUM_DURATION) {
            animationHidden();
        }else {
            if (delayAnimationHiddenRunnable == null) {
                delayAnimationHiddenRunnable = new Runnable() {
                    @Override
                    public void run() {
                        animationHidden();
                    }
                };
            }
            postDelayed(delayAnimationHiddenRunnable, MINIMUM_DURATION
                    - (currentTime - loadingTime));
        }
    }

    /**
     * 动画隐藏
     */
    private void animationHidden() {
        setClickable(false);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(400);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (loadingRootView != null && loadingRootView.getVisibility() != View.GONE) {
                    loadingRootView.setVisibility(View.GONE);
                }
                if (getVisibility() != View.GONE) {
                    setVisibility(View.GONE);
                }
            }
        });
        startAnimation(alphaAnimation);
    }

}
