package com.gmail.vexonelite.recylcerinsideswipeview.widgets.ui.fling;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @see <a href="https://stfalcon.com/en/blog/post/learning-android-gestures">Learning Android gestures</a>
 */
public final class FlingDispatchConstraintLayout extends ConstraintLayout {

    private SwipeDirectionDetector swipeDirectionDetector;
    private SwipeDirection swipeDirection;

    private RecyclerView refRecyclerView;

    private SwipeViewMovementHelper swipeViewMovementHelper;


    public FlingDispatchConstraintLayout(Context context) {
        super(context);
    }

    public FlingDispatchConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingDispatchConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private String getLogTag () {
        return this.getClass().getSimpleName();
    }


    public void doInitialization(@IdRes int recyclerViewId, @IdRes int swipeViewId) {
        final Context context = getContext();
        refRecyclerView = findViewById(recyclerViewId);

        final View swipeView = findViewById(swipeViewId);
        swipeViewMovementHelper = new SwipeViewMovementHelper(swipeView);

        swipeDirectionDetector = new SwipeDirectionDetector(context);
        swipeDirectionDetector.swipeDirectionDetectorCallback = this::onDirectionDetected;
    }


    /** implements SwipeDirectionDetectorDelegate via method reference */
    private void onDirectionDetected(@NonNull SwipeDirection direction) {
        this.swipeDirection = direction;
        //Log.i(getLogTag(), "onDirectionDetected - direction: " + direction);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
//        final int action = motionEvent.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                Log.i(getLogTag(), "dispatchTouchEvent - ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(getLogTag(), "dispatchTouchEvent - ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.i(getLogTag(), "dispatchTouchEvent - ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.i(getLogTag(), "dispatchTouchEvent - ACTION_CANCEL");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                Log.i(getLogTag(), "dispatchTouchEvent - ACTION_POINTER_DOWN");
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                Log.i(getLogTag(), "dispatchTouchEvent - ACTION_POINTER_UP");
//                break;
//        }


        //****** [Start of scrolling the RecyclerView smoothly] ******
        // the code snippet is used to enable user can scroll the RecyclerView smoothly
        // by involving super.dispatchTouchEvent(motionEvent);
        boolean canScrollDown = canRecyclerViewScrollDown();
        boolean canScrollUp = canRecyclerViewScrollUp();
        boolean hasReachedBottom = (!canScrollDown) && canScrollUp;
        boolean hasReachedTop = canScrollDown && (!canScrollUp);
        if (swipeViewMovementHelper.doesSwipeViewAppearFully()) {
            //Log.i(getLogTag(), "dispatchTouchEvent - RecyclerView - canScrollDown: " + canScrollDown);
            //Log.i(getLogTag(), "dispatchTouchEvent - RecyclerView - canScrollUp: " + canScrollUp);
            //Log.i(getLogTag(), "dispatchTouchEvent - RecyclerView - hasReachedBottom: " + hasReachedBottom);
            //Log.i(getLogTag(), "dispatchTouchEvent - RecyclerView - hasReachedTop: " + hasReachedTop);
            if (!hasReachedTop) {
                return super.dispatchTouchEvent(motionEvent);
            }
        }
        //****** [End of scrolling the RecyclerView smoothly] ******

        onUpDownEvent(motionEvent);

        swipeDirectionDetector.onTouchEvent(motionEvent);
        if (null != swipeDirection) {
            //Log.i(getLogTag(), "dispatchTouchEvent - swipeDirection: " + swipeDirection);
            switch (swipeDirection) {
                case UP: {
                    if (swipeViewMovementHelper.doesSwipeViewAppearFully()) {
                        return dispatchTouchEventToRecyclerViewIfNeed(motionEvent);
                    }
                    else {
                        return dispatchTouchEventToSwipeViewIfNeed(motionEvent, swipeDirection);
                    }
                }
                case DOWN: {
                    return dispatchTouchEventToSwipeViewIfNeed(motionEvent, swipeDirection);
                }
            }
        }
//        else {
//            Log.i(getLogTag(), "dispatchTouchEvent - swipeDirection is null!");
//        }

        return true;
    }

    private void onUpDownEvent(@NonNull MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            onActionDown(motionEvent);
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            onActionUp(motionEvent);
        }
    }

    private void onActionDown(@NonNull MotionEvent motionEvent) {
        swipeDirection = null;
//        Log.i(getLogTag(), "onActionDown - reset swipeDirection");
        dispatchTouchEventToRecyclerViewIfNeed(motionEvent);
        dispatchTouchEventToSwipeViewIfNeed(motionEvent, SwipeDirection.NOT_DETECTED);
    }

    private void onActionUp(@NonNull MotionEvent motionEvent) {
        dispatchTouchEventToRecyclerViewIfNeed(motionEvent);
        dispatchTouchEventToSwipeViewIfNeed(motionEvent, SwipeDirection.NOT_DETECTED);
    }

    private boolean dispatchTouchEventToSwipeViewIfNeed(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction) {
        //Log.i(getLogTag(), "dispatchTouchEventToSwipeViewIfNeed");
        final boolean handled;
        if (null != swipeViewMovementHelper) {
            handled = swipeViewMovementHelper.onTouchWithDirection(motionEvent, direction);
            //Log.i(getLogTag(), "dispatchTouchEventToSwipeViewIfNeed: " + handled);
        }
        else {
            handled = false;
        }
        return handled;
    }

    private boolean dispatchTouchEventToRecyclerViewIfNeed(@NonNull MotionEvent motionEvent) {
        //Log.i(getLogTag(), "dispatchTouchEventToRecyclerViewIfNeed");
        final boolean handled;
        if (null != refRecyclerView) {
            handled = refRecyclerView.dispatchTouchEvent(motionEvent);
            //Log.i(getLogTag(), "dispatchTouchEventToRecyclerViewIfNeed: " + handled);
        }
        else {
            handled = false;
        }
        return handled;
    }

    private boolean canRecyclerViewScrollUp() {
        if (null == refRecyclerView) {
            return false;
        }
        else {
            return refRecyclerView.canScrollVertically(-1);
        }
    }

    private boolean canRecyclerViewScrollDown() {
        if (null == refRecyclerView) {
            return false;
        }
        else {
            return refRecyclerView.canScrollVertically(1);
        }
    }
}
