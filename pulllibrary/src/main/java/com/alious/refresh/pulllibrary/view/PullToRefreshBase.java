package com.alious.refresh.pulllibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.alious.refresh.pulllibrary.interfaces.IPullToRefresh;


/**
 * Created by Administrator on 2016/1/3 0003.
 */
public class PullToRefreshBase extends LinearLayout implements IPullToRefresh{

    public PullToRefreshBase(Context context) {
        super(context);
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(context);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(context);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        lastX = ev.getX();
        lastY = ev.getY();
        return true;
    }

    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private float startX, startY;
    private float lastX, lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            return true;
        }else if (action == MotionEvent.ACTION_MOVE) {
            float curY = event.getY();
            int deltaY = (int) (lastY - curY);
            lastY = curY;
            scrollBy(0, deltaY);
            Log.e("move", "lastY:" + lastY + ";curY:" + curY + ";delat:" + deltaY);
        }else if (action == MotionEvent.ACTION_UP) {
            int scrollY = getScrollY();
            mVelocityTracker.computeCurrentVelocity(1000);
            float yVelocity = mVelocityTracker.getYVelocity();
            smoothScrollToTop(0, (int) yVelocity);
            mVelocityTracker.clear();
        }
        return super.onTouchEvent(event);
    }

    private void smoothScrollToTop(int dx, int dy) {
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public void startRefresh() {

    }
    @Override
    public void endRefresh() {

    }
}
