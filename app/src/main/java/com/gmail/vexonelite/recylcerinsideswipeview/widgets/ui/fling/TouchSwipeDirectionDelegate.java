package com.gmail.vexonelite.recylcerinsideswipeview.widgets.ui.fling;

import android.view.MotionEvent;

import androidx.annotation.NonNull;


/**
 * @see <a href="https://stfalcon.com/en/blog/post/learning-android-gestures">Learning Android gestures</a>
 */
public interface TouchSwipeDirectionDelegate {
    /**
     * the callback of a captured swipe gesture
     */
    boolean onTouchWithDirection(@NonNull MotionEvent motionEvent, @NonNull SwipeDirection direction);
}
