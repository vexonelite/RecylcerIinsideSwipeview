package com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


public interface PhotoNameDelegate {
    @NonNull String getIdentifier();
    @NonNull String getPhotoUrl();
    @ColorInt int getColorInt();
    @NonNull String getName();
}
