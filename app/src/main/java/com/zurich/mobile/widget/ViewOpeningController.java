package com.zurich.mobile.widget;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;

/**
 * 用于快捷实现View展开关闭效果
 * <br>
 * <br>首先new ViewOpeningController(view)，注意ViewOpeningController有一个抽象方法setViewStatus(boolean)需要你重写，在这个方法里根据不同的状态设置View
 * <br>然后调用measureViewHeight(boolean)方法重置相关属性并初始化View状态
 * <br>最后调用opening(boolean)方法展开或关闭View
 * <br>
 * <br>ViewOpeningController可用于任何地方，特别是ListView中
 * @author pan peng fei
 */
public abstract class ViewOpeningController implements AnimationListener {
	private View view;
	private int fillParent;
	private int minHeight;
	private int maxHeight;
	
	public ViewOpeningController(View view){
		this.view = view;
		this.fillParent = view.getResources().getDisplayMetrics().widthPixels;
	}
	
	public View getOpeningView(){
		return view;
	}
	
	/**
	 * 设置View的状态
	 * @param opened true：打开；false：关闭
	 */
	public abstract void setViewStatus(boolean opened);
	
	/**
	 * 测量View在不同状态下的高度，并初始化View的状态
	 * @param opened 当前状态
	 */
	public void measureViewHeightAndInit(boolean opened){
		// 要先将高度设置为wrap_content，要不然测量高度就不准确了
		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		view.setLayoutParams(layoutParams);

		// 切换到关闭状态测量最低高度
		setViewStatus(false);
		if(view.getVisibility() != View.GONE){
			view.measure(
					MeasureSpec.makeMeasureSpec(fillParent, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
					);
			minHeight = view.getMeasuredHeight();
		}else{
			minHeight = 0;
		}
		
		// 切换到打开状态测量最高高度
		setViewStatus(true);
		if(view.getVisibility() != View.GONE){
			view.measure(
					MeasureSpec.makeMeasureSpec(fillParent, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
					);
			maxHeight = view.getMeasuredHeight();
		}else{
			minHeight = 0;
		}
		
		// 初始化状态以及高度
		setViewStatus(opened);
		layoutParams.height = opened?maxHeight:minHeight;
		view.setLayoutParams(layoutParams);
	}
	
	/**
	 * 展开或关闭View
	 * @param open true：展开View；false：关闭View
	 */
	public void opening(boolean open){
		MyAnimation animation;
		int fromHeight;
		if(view.getVisibility() != View.GONE){
			fromHeight= view.getHeight();
		}else{
			fromHeight = 0;
		}
		if(open){
			animation = new MyAnimation(view, fromHeight, maxHeight);
			setViewStatus(true);
		}else{
			animation = new MyAnimation(view, fromHeight, minHeight);
			animation.setAnimationListener(this);
		}
		animation.setDuration(200l);
		view.startAnimation(animation);
	}
	
	@Override
	public void onAnimationStart(Animation animation) {
		
	}
	
	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		setViewStatus(false);
	}
	
	private static class MyAnimation extends Animation {
		private int number;
		private int from;
		private View view;
		
		public MyAnimation(View view, int from, int to){
			this.view = view;
			this.from = from;
			this.number = to - from;
		}
		
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			LayoutParams param = view.getLayoutParams();
			param.height = from + (int) (number * interpolatedTime);
			view.setLayoutParams(param);
		}
	}
}
