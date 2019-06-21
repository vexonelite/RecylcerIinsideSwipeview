package com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public final class PhotoNameDecoration extends RecyclerView.ItemDecoration {

    private final int margin;

    public PhotoNameDecoration(@NonNull Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        margin = (int)(density * 20f);
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        final int position = parent.getChildLayoutPosition(view);
        outRect.left = margin;
        outRect.right = margin;
        outRect.bottom = margin;
        outRect.top = (position == 0) ? margin : 0;
    }
}
