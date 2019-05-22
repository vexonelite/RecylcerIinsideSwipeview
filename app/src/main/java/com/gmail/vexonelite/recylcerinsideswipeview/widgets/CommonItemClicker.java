package com.gmail.vexonelite.recylcerinsideswipeview.widgets;

import android.view.View;

import androidx.annotation.NonNull;


public abstract class CommonItemClicker<T> extends CommonItemWrapper<T> implements View.OnClickListener {
    public CommonItemClicker(@NonNull T object, @NonNull String action, int position) {
        super(object, action, position);
    }
}
