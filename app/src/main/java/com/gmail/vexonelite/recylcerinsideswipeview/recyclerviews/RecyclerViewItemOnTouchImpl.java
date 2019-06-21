package com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public final class RecyclerViewItemOnTouchImpl implements RecyclerView.OnItemTouchListener {

    private String getLogTag() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        //Log.i(getLogTag(), "onInterceptTouchEvent");
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        Log.i(getLogTag(), "onTouchEvent");
        if (motionEvent.getActionMasked() != MotionEvent.ACTION_UP) {
            return;
        }
        Log.i(getLogTag(), "onTouchEvent - ACTION_UP");
        //check if touch on the item
        final View itemView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (null == itemView) {
            Log.e(getLogTag(), "onTouchEvent - No item is touched!");
        }
        else {
            Log.e(getLogTag(), "onTouchEvent - Item is touched!");
        }

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //Log.i(getLogTag(), "onRequestDisallowInterceptTouchEvent - parameter: " + disallowIntercept);
    }
}
