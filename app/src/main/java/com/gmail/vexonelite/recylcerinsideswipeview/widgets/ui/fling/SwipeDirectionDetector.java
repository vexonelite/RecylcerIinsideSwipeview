package com.gmail.vexonelite.recylcerinsideswipeview.widgets.ui.fling;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;


/**
 * @see <a href="https://stfalcon.com/en/blog/post/learning-android-gestures">Learning Android gestures</a>
 */
public final class SwipeDirectionDetector {

    public SwipeDirectionDetectorDelegate swipeDirectionDetectorCallback;

    private int touchSlop;
    private float startX, startY;
    private boolean isDetected;

    public SwipeDirectionDetector(@NonNull Context context) {
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        //Log.i("SwipeDirectionDetector", "onTouchEvent");
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = motionEvent.getX();
                startY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!isDetected) {
                    notifySwipeDirectionDetectorCallbackIfNeeded(SwipeDirection.NOT_DETECTED);
                }
                startX = startY = 0.0f;
                isDetected = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isDetected && getDistance(motionEvent) > touchSlop) {
                    isDetected = true;
                    final float x = motionEvent.getX();
                    final float y = motionEvent.getY();
                    final SwipeDirection direction = SwipeDirection.getDirection(startX, startY, x, y);
                    notifySwipeDirectionDetectorCallbackIfNeeded(direction);
                }

//                if (!isDetected) {
//                    isDetected = true;
//                }
//                if (isDetected && getDistance(motionEvent) > touchSlop) {
//                    final float x = motionEvent.getX();
//                    final float y = motionEvent.getY();
//                    final SwipeDirection direction = SwipeDirection.getDirection(startX, startY, x, y);
//                    notifySwipeDirectionDetectorCallbackIfNeeded(direction);
//                }
                break;
        }
        return false;
    }

    private float getDistance(@NonNull MotionEvent motionEvent) {
        final float dx = (motionEvent.getX(0) - startX);
        final float dy = (motionEvent.getY(0) - startY);
        final double distanceSum = 0d + Math.sqrt(dx * dx + dy * dy);
        return (float) distanceSum;
    }

    private void notifySwipeDirectionDetectorCallbackIfNeeded (@NonNull SwipeDirection direction) {
        if (null != swipeDirectionDetectorCallback) {
            swipeDirectionDetectorCallback.onDirectionDetected(direction);
        }
    }

}
