package com.gmail.vexonelite.recylcerinsideswipeview.widgets.ui.fling;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @see <a href="https://stfalcon.com/en/blog/post/learning-android-gestures">Learning Android gestures</a>
 */
public final class SwipeViewMovementHelper implements TouchSwipeDirectionDelegate {

    private final View swipeView;
    private int thresholdTranslationY = -1;
    private int maxTranslationY = -1;
    private float startY;
    /**
     * True for the swipeView is being animated; False is */
    private boolean animationLock;
    /** The state of private SwipeView */
    private SwipeViewState swipeViewState = SwipeViewState.DOWN;


    public SwipeViewAppearDelegate swipeViewAppearCallback;

    public SwipeViewMovementHelper(@NonNull View swipeView) {
        this.swipeView = swipeView;
        swipeView.getViewTreeObserver().addOnGlobalLayoutListener(this::swipeViewOnGlobalLayoutCallback);
    }

    public SwipeViewMovementHelper(@NonNull View swipeView, @Nullable SwipeViewAppearDelegate callback) {
        this.swipeView = swipeView;
        swipeView.getViewTreeObserver().addOnGlobalLayoutListener(this::swipeViewOnGlobalLayoutCallback);
        this.swipeViewAppearCallback = callback;
    }

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    // implements ViewTreeObserver.OnGlobalLayoutListener
    private void swipeViewOnGlobalLayoutCallback () {
        swipeView.getViewTreeObserver().removeOnGlobalLayoutListener(this::swipeViewOnGlobalLayoutCallback);
        if (maxTranslationY < 0) {
            final Rect localVisibleRect = new Rect();
            swipeView.getLocalVisibleRect(localVisibleRect);
            maxTranslationY = localVisibleRect.bottom;
            thresholdTranslationY = (int)( ((float) maxTranslationY) / 3f);
//            Log.i(getLogTag(), "swipeView localVisibleRect: " + localVisibleRect
//                + ", maxTranslationY: " + maxTranslationY + ", thresholdTranslationY: " + thresholdTranslationY);
            new Handler(Looper.getMainLooper()).post(this::hideSwipeViewInitially);
        }
    }

    // https://stackoverflow.com/questions/27462468/custom-view-overrides-ontouchevent-but-not-performclick
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchWithDirection(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction) {
        final float currentTranslationY = swipeView.getTranslationY();
        //Log.i(getLogTag(), "onTouchWithDirection - currentTranslationY: " + currentTranslationY + ", direction: " + direction);

//        final int action = motionEvent.getAction();
//        switch (action & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                Log.i(getLogTag(), "onTouch - ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(getLogTag(), "onTouch - ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.i(getLogTag(), "onTouch - ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.i(getLogTag(), "onTouch - ACTION_CANCEL");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                Log.i(getLogTag(), "onTouch - ACTION_POINTER_DOWN");
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                Log.i(getLogTag(), "onTouch - ACTION_POINTER_UP");
//                break;
//        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startY = motionEvent.getY();
//                Log.i(getLogTag(), "onTouch - startY: " + startY);
                return true;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (doesMoveAlongVerticalDirection(direction)) {
                    animateSwipeView(motionEvent, direction);
                }
                return true;

            case MotionEvent.ACTION_MOVE: {
                boolean isVertical = doesMoveAlongVerticalDirection(direction);
                if (isVertical) {
                    movementHandler(motionEvent, direction);
                }

                return isVertical;
            }
        }

        return false;
    }

    private boolean doesMoveAlongVerticalDirection(@NonNull SwipeDirection direction) {
        return (direction == SwipeDirection.UP) || (direction == SwipeDirection.DOWN);
    }

    private void movementHandler(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction) {
        final float translationY = startY - motionEvent.getY();
        final float newTranslationY;
        if (direction == SwipeDirection.UP) {
            final float tmp = maxTranslationY - translationY;
            newTranslationY = (tmp >= 0) ? tmp : 0f;
        }
        else {
            final float tmp = (-translationY);
            newTranslationY = (tmp <= maxTranslationY)? tmp : maxTranslationY;
        }
        //Log.i(getLogTag(), "movementHandler - translationY: " + translationY + ", newTranslationY: " + newTranslationY);
        swipeView.setTranslationY(newTranslationY);
        swipeViewState = SwipeViewState.MOVE;
    }

    private void animateSwipeView(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction) {
        final float currentPosition = swipeView.getTranslationY();
        final float translationY = startY - motionEvent.getY();
        final boolean hasExceededThreshold = (direction == SwipeDirection.UP)
                ? (translationY > thresholdTranslationY)
                : ((-translationY) > thresholdTranslationY);
        final float top = 0f;
        final float bottom = swipeView.getHeight();
        final float source = (direction == SwipeDirection.UP) ? bottom : top;
        final float destination = (direction == SwipeDirection.UP) ? top : bottom;
        final float animateTo = hasExceededThreshold ? destination : source;
        animateSwipeView(currentPosition, animateTo, 200L);
    }

    private void animateSwipeView (float currentPosition, float animateTo, long duration) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(
                swipeView, "translationY", currentPosition, animateTo);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        animationLock = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        notifySwipeViewAppearCallbackIfNeeded();
                        final float currentPosition = swipeView.getTranslationY();
                        swipeViewState = (currentPosition <= 0f) ? SwipeViewState.TOP : SwipeViewState.DOWN;
                        //Log.w(getLogTag(), "onAnimationEnd - swipeViewState: " + swipeViewState);
                        animationLock = false;
                    }
                });
        animator.start();
    }

    private void hideSwipeViewInitially () {
        swipeView.setTranslationY(maxTranslationY);
    }

    private void notifySwipeViewAppearCallbackIfNeeded () {
        if (null != swipeViewAppearCallback) {
            swipeViewAppearCallback.onSwipeViewAppeared();
        }
    }

    public interface SwipeViewAppearDelegate {
        void onSwipeViewAppeared();
    }

    public boolean doesSwipeViewAppearFully() {
        //final boolean result = swipeViewState == SwipeViewState.TOP;
        //Log.w(getLogTag(), "doesSwipeViewAppearFully - swipeViewState: " + swipeViewState + ", result: " + result);
        return swipeViewState == SwipeViewState.TOP;
    }

}
