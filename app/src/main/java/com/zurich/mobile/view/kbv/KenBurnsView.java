package com.zurich.mobile.view.kbv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.zurich.mobile.view.kbv.Transition;
import com.zurich.mobile.view.kbv.TransitionGenerator;
import com.zurich.mobile.view.kbv.RandomTransitionGenerator;

/**
 * Created by weixinfei on 16/3/30.
 */
public class KenBurnsView extends ImageView{
    private static final long FRAME_DELAY = 16L;
    private Transition mCurrentTrans;
    private RectF mDrawableRect;
    private long mElapsedTime;
    private boolean mInitialized;
    private long mLastFrameTime;
    private final Matrix mMatrix;
    private boolean mPaused;
    private TransitionGenerator mTransGen;
    private KenBurnsView.TransitionListener mTransitionListener;
    private final RectF mViewportRect;

    public KenBurnsView(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public KenBurnsView(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public KenBurnsView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.mMatrix = new Matrix();
        this.mTransGen = new RandomTransitionGenerator();
        this.mViewportRect = new RectF();
        this.mInitialized = true;
        super.setScaleType(ScaleType.MATRIX);
    }

    private void fireTransitionEnd(Transition var1) {
        if(this.mTransitionListener != null && var1 != null) {
            this.mTransitionListener.onTransitionEnd(var1);
        }

    }

    private void fireTransitionStart(Transition var1) {
        if(this.mTransitionListener != null && var1 != null) {
            this.mTransitionListener.onTransitionStart(var1);
        }

    }

    private void handleImageChange() {
        this.updateDrawableBounds();
        if(this.mInitialized && this.hasBounds()) {
            this.startNewTransition();
        }

    }

    private boolean hasBounds() {
        return !this.mViewportRect.isEmpty();
    }

    private void startNewTransition() {
        if(!this.hasBounds()) {
            throw new UnsupportedOperationException("Can\'t start transition if the drawable has no bounds!");
        } else {
            this.mCurrentTrans = this.mTransGen.generateNextTransition(this.mDrawableRect, this.mViewportRect);
            this.mElapsedTime = 0L;
            this.mLastFrameTime = System.currentTimeMillis();
            this.fireTransitionStart(this.mCurrentTrans);
        }
    }

    private void updateDrawableBounds() {
        if(this.mDrawableRect == null) {
            this.mDrawableRect = new RectF();
        }

        Drawable var1 = this.getDrawable();
        if(var1 != null) {
            this.mDrawableRect.set(0.0F, 0.0F, (float)var1.getIntrinsicWidth(), (float)var1.getIntrinsicHeight());
        }

    }

    private void updateViewport(float var1, float var2) {
        this.mViewportRect.set(0.0F, 0.0F, var1, var2);
    }

    protected void onDraw(Canvas var1) {
        Drawable var7 = this.getDrawable();
        if(!this.mPaused && var7 != null) {
            if(this.mDrawableRect.isEmpty()) {
                this.updateDrawableBounds();
            } else if(this.hasBounds()) {
                if(this.mCurrentTrans == null) {
                    this.startNewTransition();
                }

                if(this.mCurrentTrans.getDestinyRect() != null) {
                    this.mElapsedTime += System.currentTimeMillis() - this.mLastFrameTime;
                    RectF var8 = this.mCurrentTrans.getInterpolatedRect(this.mElapsedTime);
                    float var2 = Math.min(this.mDrawableRect.width() / var8.width(), this.mDrawableRect.height() / var8.height()) * (this.mViewportRect.width() / var8.width());
                    float var3 = this.mDrawableRect.centerX();
                    float var4 = var8.left;
                    float var5 = this.mDrawableRect.centerY();
                    float var6 = var8.top;
                    this.mMatrix.reset();
                    this.mMatrix.postTranslate(-this.mDrawableRect.width() / 2.0F, -this.mDrawableRect.height() / 2.0F);
                    this.mMatrix.postScale(var2, var2);
                    this.mMatrix.postTranslate(var2 * (var3 - var4), var2 * (var5 - var6));
                    this.setImageMatrix(this.mMatrix);
                    if(this.mElapsedTime >= this.mCurrentTrans.getDuration()) {
                        this.fireTransitionEnd(this.mCurrentTrans);
                        this.startNewTransition();
                    }
                } else {
                    this.fireTransitionEnd(this.mCurrentTrans);
                }
            }

            this.mLastFrameTime = System.currentTimeMillis();
            this.postInvalidateDelayed(16L);
        }

        super.onDraw(var1);
    }

    protected void onSizeChanged(int var1, int var2, int var3, int var4) {
        super.onSizeChanged(var1, var2, var3, var4);
        this.restart();
    }

    public void pause() {
        this.mPaused = true;
    }

    public void restart() {
        int var1 = this.getWidth();
        int var2 = this.getHeight();
        if(var1 != 0 && var2 != 0) {
            this.updateViewport((float)var1, (float)var2);
            this.updateDrawableBounds();
            if(this.hasBounds()) {
                this.startNewTransition();
            }

        } else {
            throw new UnsupportedOperationException("Can\'t call restart() when view area is zero!");
        }
    }

    public void resume() {
        this.mPaused = false;
        this.mLastFrameTime = System.currentTimeMillis();
        this.invalidate();
    }

    public void setImageBitmap(Bitmap var1) {
        super.setImageBitmap(var1);
        this.handleImageChange();
    }

    public void setImageDrawable(Drawable var1) {
        super.setImageDrawable(var1);
        this.handleImageChange();
    }

    public void setImageResource(int var1) {
        super.setImageResource(var1);
        this.handleImageChange();
    }

    public void setImageURI(Uri var1) {
        super.setImageURI(var1);
        this.handleImageChange();
    }

    public void setScaleType(ScaleType var1) {
    }

    public void setTransitionGenerator(TransitionGenerator var1) {
        this.mTransGen = var1;
        if(this.hasBounds()) {
            this.startNewTransition();
        }

    }

    public void setTransitionListener(KenBurnsView.TransitionListener var1) {
        this.mTransitionListener = var1;
    }

    public void setVisibility(int var1) {
        super.setVisibility(var1);
        switch(var1) {
            case 0:
                this.resume();
                return;
            default:
                this.pause();
        }
    }

    public interface TransitionListener {
        void onTransitionEnd(Transition var1);

        void onTransitionStart(Transition var1);
    }

}
