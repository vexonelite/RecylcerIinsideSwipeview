package com.gmail.vexonelite.recylcerinsideswipeview.widgets.ui.fling;

import androidx.annotation.NonNull;


/**
 * @see <a href="https://stfalcon.com/en/blog/post/learning-android-gestures">Learning Android gestures</a>
 */
public interface SwipeDirectionDetectorDelegate {
    /**
     * the callback of a captured swipe gesture
     */
    void onDirectionDetected(@NonNull SwipeDirection direction);
}
