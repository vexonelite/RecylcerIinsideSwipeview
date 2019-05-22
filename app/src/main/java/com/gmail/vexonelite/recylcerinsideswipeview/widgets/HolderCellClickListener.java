package com.gmail.vexonelite.recylcerinsideswipeview.widgets;

import androidx.annotation.NonNull;


public interface HolderCellClickListener<T> {
    void onHolderCellClicked(@NonNull T item, @NonNull String action, int position);
}
