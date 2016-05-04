package com.zurich.mobile.activity;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.SpringBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.FragmentArrayPageAdapter;
import com.zurich.mobile.fragment.SetupFirstFragment;
import com.zurich.mobile.fragment.SetupFourthFragment;
import com.zurich.mobile.fragment.SetupSecondFragment;
import com.zurich.mobile.fragment.SetupThridFragment;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.SharedPreferenceUtil;
import com.zurich.mobile.view.ViewPagerCompat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 安全防护引导
 * Created by weixinfei on 16/4/24.
 */
public class SetupSafeActivty extends FragmentActivity {
    @Bind(R.id.pager_setup_content)
    ViewPagerCompat viewPagerCompat;
    @Bind(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @Bind(R.id.btn_setup1_over)
    Button btnSetup1Over;
    @Bind(R.id.indicator_setup_safe)
    FixedIndicatorView indicatorSetupSafe;

    private int[] colors;
    private int mState = ViewPager.SCROLL_STATE_IDLE; // 初始位于停止滑动状态
    private ArgbEvaluator mArgbEvaluator;
    private FragmentArrayPageAdapter mPagerAdapter;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_safe);
        ButterKnife.bind(this);

        initActionBar();
        initColors();
        initViews();
        initData();
    }

    private void initActionBar() {
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvToolbarTitle.setText("安全防盗");
    }

    /**
     * 初始化颜色
     */
    private void initColors() {
        colors = new int[4];
        colors[0] = getResources().getColor(R.color.guideBackgroundColor1);
        colors[1] = getResources().getColor(R.color.guideBackgroundColor2);
        colors[2] = getResources().getColor(R.color.guideBackgroundColor3);
        colors[3] = getResources().getColor(R.color.guideBackgroundColor4);

        mArgbEvaluator = new ArgbEvaluator();

        int selectColorId = Color.parseColor("#f8f8f8");
        int unSelectColorId = Color.parseColor("#010101");
        indicatorSetupSafe.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColorId, unSelectColorId));
        indicatorSetupSafe.setScrollBar(new SpringBar(getApplicationContext(), Color.GRAY));
        viewPagerCompat.setOffscreenPageLimit(4);
        indicatorViewPager = new IndicatorViewPager(indicatorSetupSafe, viewPagerCompat);
        inflate = LayoutInflater.from(getApplicationContext());
        indicatorViewPager.setAdapter(indicatorPagerAdapter);
        indicatorViewPager.setCurrentItem(1, false);
    }


    /**
     * 初始化界面
     */
    private void initViews() {
        // 初始颜色
        viewPagerCompat.setBackgroundColor(colors[0]);

        viewPagerCompat.addOnPageChangeListener(new GuidePageListener());

        btnSetup1Over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPreferenceUtil.getProtectConfigPrefs(getBaseContext(), false)) {
                    Dialog.Builder builder = null;
                    builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                        @Override
                        public void onPositiveActionClicked(DialogFragment fragment) {
                            finish();
                            super.onPositiveActionClicked(fragment);
                        }

                        @Override
                        public void onNegativeActionClicked(DialogFragment fragment) {
                            super.onNegativeActionClicked(fragment);
                        }
                    };

                    ((SimpleDialog.Builder) builder).message("您还没有开启安全防盗，确认退出？")
                            .title("确认完成")
                            .positiveAction("确认")
                            .negativeAction("重新选择");
                    DialogFragment fragment = DialogFragment.newInstance(builder);
                    fragment.show(getSupportFragmentManager(), null);
                } else {
                    SharedPreferenceUtil.setLostFindConfigPrefs(getBaseContext(), true);
                    GlobalUtils.showToast(getBaseContext(), "已完成设置！");
                    finish();
                }
            }
        });
    }

    private IndicatorViewPager.IndicatorPagerAdapter indicatorPagerAdapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_top2, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(String.valueOf(position + 1));
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            return convertView;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };


    private void initData() {
        setFragments();
    }

    private void setFragments() {
        mPagerAdapter = new FragmentArrayPageAdapter(getSupportFragmentManager(), new SetupFirstFragment(), new SetupSecondFragment(), new SetupThridFragment(), new SetupFourthFragment());
        viewPagerCompat.setAdapter(mPagerAdapter);
        viewPagerCompat.setOffscreenPageLimit(viewPagerCompat.getAdapter().getCount());
    }

    /**
     * viewpager的滑动监听
     *
     * @author Kevin
     */
    class GuidePageListener implements ViewPager.OnPageChangeListener {

        // 滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 只要不是滑动停止状态就计算颜色
            if (mState != ViewPager.SCROLL_STATE_IDLE) {
                if (positionOffset > 0 && position < 4) {
                    int evaluatePreColor = (int) mArgbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]);
                    viewPagerCompat.setBackgroundColor(evaluatePreColor);
                } else if (positionOffset < 0 && position > 0) {
                    int evaluateNextColor = (int) mArgbEvaluator.evaluate(-positionOffset, colors[position], colors[position - 1]);
                    viewPagerCompat.setBackgroundColor(evaluateNextColor);
                }
            }
        }

        // 某个页面被选中
        @Override
        public void onPageSelected(int position) {
            if (position == 3) {
                btnSetup1Over.setVisibility(View.VISIBLE);
            } else {
                btnSetup1Over.setVisibility(View.GONE);
            }
        }

        // 滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {
            mState = state;
        }

    }
}
