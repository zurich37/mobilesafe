package com.zurich.mobile.widget;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class ColorTextSpan extends ClickableSpan {
	
	private int mColor = 0;
	private View.OnClickListener mListener;
	
	public ColorTextSpan(int color, View.OnClickListener listener){
		mColor = color;
		mListener = listener;
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		tp.setColor(mColor);
	}

	@Override
	public void onClick(View widget) {
		if(mListener != null)
			mListener.onClick(widget);
	}

}
