package com.gmail.vexonelite.recylcerinsideswipeview.whiterabbit.dragqueen;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public interface ViewDragAdapter {

    /**
     * Return the magnitude of a draggable child view's horizontal range of motion in pixels.
     * This method should return 0 for views that cannot move horizontally.
     *
     * @param child Child view to check
     * @return range of horizontal motion in pixels
     */
    int getViewHorizontalDragRange(@Nullable View child);

    /**
     * Return the magnitude of a draggable child view's vertical range of motion in pixels.
     * This method should return 0 for views that cannot move vertically.
     *
     * @param child Child view to check
     * @return range of vertical motion in pixels
     */
    int getViewVerticalDragRange(@Nullable View child);

    /**
     * Called when the user's input indicates that they want to capture the given child view
     * with the pointer indicated by pointerId. The callback should return true if the user
     * is permitted to drag the given view with the indicated pointer.
     *
     * <p>ViewDragHelper may call this method multiple times for the same view even if
     * the view is already captured; this indicates that a new pointer is trying to take
     * control of the view.</p>
     *
     * <p>If this method returns true, a call to {@link #onViewCaptured(android.view.View, int)}
     * will follow if the capture is successful.</p>
     *
     * @param child Child the user is attempting to capture
     * @param pointerId ID of the pointer attempting the capture
     * @return true if capture should be allowed, false otherwise
     */
    boolean tryCaptureView(@NonNull View child, int pointerId);

    ///

    boolean doesHitTargetView(@NonNull MotionEvent motionEvent, boolean isMoving);
}
