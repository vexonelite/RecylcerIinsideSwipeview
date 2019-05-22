package com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews;

import java.util.UUID;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


public final class PhotoNameImpl implements PhotoNameDelegate {

    private final String identifier;
    private final String photoUrl;
    @ColorInt
    private final int color;
    private final String name;

    public PhotoNameImpl(@NonNull String photoUrl, @ColorInt int color, @NonNull String name) {
        this.identifier = UUID.randomUUID().toString();
        this.photoUrl = photoUrl;
        this.color = color;
        this.name = name;
    }

    @NonNull
    @Override
    public String getIdentifier() {
        return identifier;
    }

    @NonNull
    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }


    @ColorInt
    @Override
    public int getColorInt() {
        return color;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }
}
