package com.hysting.library;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Hysting on 2014/11/10.
 */
public class ClosableSlidingLayout extends RelativeLayout {

    private ViewDragHelper mDragHelper;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private SlideListener mListener;

    public ClosableSlidingLayout(Context context) {
        this(context, null);
    }

    public ClosableSlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClosableSlidingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                if (adx > ady && adx - ady > 10){
                    interceptTap = true;
                    mDragHelper.captureChildView(getChildAt(0),0);
                }else{
                    interceptTap = false;
                }
            }
        }
        final boolean interceptForDrag = mDragHelper.shouldInterceptTouchEvent(ev);
        return interceptTap || interceptForDrag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setSlideListener(SlideListener listener){
        mListener = listener;
    }

    /**
     *Callback
     */
    private class ViewDragCallback extends ViewDragHelper.Callback{
        private static final int MINVEL = 500;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            //Is off the screen ?
            if (state == ViewDragHelper.STATE_IDLE && mDragHelper.getCapturedView().getLeft()!=0) {
                mListener.onClosed();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel > MINVEL) {
                mDragHelper.smoothSlideViewTo(releasedChild, getWidth(), 0);
            } else if (xvel < -MINVEL) {
                mDragHelper.smoothSlideViewTo(releasedChild, -getWidth(), 0);
            } else {
                if (releasedChild.getLeft() > getWidth() / 2) {
                    mDragHelper.smoothSlideViewTo(releasedChild, getWidth(), 0);
                } else if (releasedChild.getLeft() < -getWidth() / 2) {
                    mDragHelper
                            .smoothSlideViewTo(releasedChild, -getWidth(), 0);
                } else {
                    mDragHelper.smoothSlideViewTo(releasedChild, 0, 0);
                }
            }
            invalidate();
        }


        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }
    }

    /**
     * set listener
     */
    public interface SlideListener{
        public void onClosed();
    }

}
