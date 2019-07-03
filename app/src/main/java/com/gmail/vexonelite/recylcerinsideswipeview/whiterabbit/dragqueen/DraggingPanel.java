package com.gmail.vexonelite.recylcerinsideswipeview.whiterabbit.dragqueen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * @see <a href="https://medium.com/android-development-p-ractises/dragging-panel-with-viewdraghelper-6df8dd980082">Dragging panel with ViewDragHelper</a>
 * @see <a href="https://gist.github.com/deyanm/58cbd80cd9907cf520c7a06b567eefba">Source Code @ Gist</a>
 */
public final class DraggingPanel extends RelativeLayout {

    private final double AUTO_OPEN_SPEED_LIMIT = 800.0;

    /** state of Dragging */
    private int mDraggingState = 0;

    private ViewDragHelper mDragHelper;

    private int mDraggingBorder;

    private boolean mIsOpen;


    public OnSizeChangedDelegate onSizeChangedCallback;
    public ViewDragAdapter viewDragAdapter;


    private String getLogTag() {
        return this.getClass().getSimpleName();
    }

    public class DragHelperCallback extends ViewDragHelper.Callback {
        @Override
        public void onViewDragStateChanged(int state) {
            if (state == mDraggingState) { // no change
                return;
            }
            if (    (mDraggingState == ViewDragHelper.STATE_DRAGGING || mDraggingState == ViewDragHelper.STATE_SETTLING)
                    && (state == ViewDragHelper.STATE_IDLE) ) {
                // the view stopped from moving.

                final int verticalRange = (null != viewDragAdapter) ? viewDragAdapter.getViewVerticalDragRange(null) : 0;
                if (mDraggingBorder == 0) {
                    onStopDraggingToClosed();
                } else if (mDraggingBorder == verticalRange) {
                    mIsOpen = true;
                    //Log.e("getLogTag", "[!! Opened now !!]");
                }
            }
            if (state == ViewDragHelper.STATE_DRAGGING) {
                onStartDragging();
            }
            mDraggingState = state;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            mDraggingBorder = top;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            if (null != viewDragAdapter) {
                return viewDragAdapter.getViewHorizontalDragRange(child);
            }
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            if (null != viewDragAdapter) {
                return viewDragAdapter.getViewVerticalDragRange(child);
            }
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public boolean tryCaptureView(@NonNull View view, int pointerId) {
            //Log.i("DraggingPanel", "tryCaptureView - pointerId: " + pointerId);
            return (null != viewDragAdapter) && (viewDragAdapter.tryCaptureView(view, pointerId));
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            final int topBound = getPaddingTop();
            //final int bottomBound = mVerticalRange;
            final int bottomBound = (null != viewDragAdapter) ? viewDragAdapter.getViewVerticalDragRange(child) : 0;
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            final int verticalRange = (null != viewDragAdapter) ? viewDragAdapter.getViewVerticalDragRange(releasedChild) : 0;
            //final float rangeToCheck = mVerticalRange;
            final float rangeToCheck = verticalRange;
            if (mDraggingBorder == 0) {
                mIsOpen = false;
                //Log.e("getLogTag", "[!! Closed now !!]");
                return;
            }
            if (mDraggingBorder == rangeToCheck) {
                mIsOpen = true;
                //Log.e("getLogTag", "[!! Opened now !!]");
                return;
            }
            boolean settleToOpen = false;
            if (yvel > AUTO_OPEN_SPEED_LIMIT) { // speed has priority over position
                settleToOpen = true;
            }
//            else if (yvel < -AUTO_OPEN_SPEED_LIMIT) {
//                settleToOpen = false;
//            }
//            else if (mDraggingBorder > rangeToCheck / 2) {
//                settleToOpen = true;
//            }
//            else if (mDraggingBorder < rangeToCheck / 2) {
//                settleToOpen = false;
//            }

            //final int settleDestY = settleToOpen ? mVerticalRange : 0;
            final int settleDestY = settleToOpen ? verticalRange : 0;

            if(mDragHelper.settleCapturedViewAt(0, settleDestY)) {
                ViewCompat.postInvalidateOnAnimation(DraggingPanel.this);
            }
        }
    }

    public DraggingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
        mIsOpen = false;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        if (null != onSizeChangedCallback) {
            onSizeChangedCallback.onSizeChanged(width, height, oldWidth, oldHeight);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final boolean shouldInterceptTouchEvent = mDragHelper.shouldInterceptTouchEvent(event);
        Log.e(getLogTag(), "onInterceptTouchEvent - shouldInterceptTouchEvent: " + shouldInterceptTouchEvent);

        final boolean isMoving = isMoving();
        final boolean doesHitTargetView = doesHitTargetView(event, isMoving);
        Log.i(getLogTag(), "onInterceptTouchEvent - doesHitTargetView: " + doesHitTargetView);

        return (isMoving && mDragHelper.shouldInterceptTouchEvent(event));
        //return (doesHitTargetView(event, isMoving) && mDragHelper.shouldInterceptTouchEvent(event));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(getLogTag(), "onTouchEvent");
        final boolean isMoving = isMoving();
        if (doesHitTargetView(event, isMoving) || isMoving) {
            mDragHelper.processTouchEvent(event);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void computeScroll() { // needed for automatic settling.
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    private void onStopDraggingToClosed() {
        // To be implemented
    }

    private void onStartDragging() {

    }

    private boolean doesHitTargetView(@NonNull MotionEvent motionEvent, boolean isMoving) {
        return (null != viewDragAdapter) && (viewDragAdapter.doesHitTargetView(motionEvent, isMoving));
    }


    public boolean isMoving() {
        return (mDraggingState == ViewDragHelper.STATE_DRAGGING ||
                mDraggingState == ViewDragHelper.STATE_SETTLING);
    }

    public boolean isOpen() {
        return mIsOpen;
    }
}
