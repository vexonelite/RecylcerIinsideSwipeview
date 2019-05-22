package com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gmail.vexonelite.recylcerinsideswipeview.R;


public final class PhotoNameViewHolder extends RecyclerView.ViewHolder {

    private final ImageView photoView;
    private final TextView nameView;

    public PhotoNameViewHolder(@NonNull View itemView) {
        super(itemView);
        photoView = itemView.findViewById(R.id.photo);
        nameView = itemView.findViewById(R.id.name);
    }

    public void onBind(@NonNull PhotoNameDelegate delegate, int position) {
        nameView.setText(delegate.getIdentifier());
        photoView.setBackgroundColor(delegate.getColorInt());
    }

}