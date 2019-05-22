package com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.gmail.vexonelite.recylcerinsideswipeview.R;
import com.gmail.vexonelite.recylcerinsideswipeview.widgets.CommonItemWrapper;
import com.gmail.vexonelite.recylcerinsideswipeview.widgets.HolderCellClickListener;


public final class PhotoNameAdapter extends ListAdapter<PhotoNameDelegate, PhotoNameViewHolder> {

    public HolderCellClickListener<PhotoNameDelegate> holderCellClickCallback;

    public PhotoNameAdapter() {
        super(new PhotoNameDiffCallback());
    }

    @NonNull
    @Override
    public final PhotoNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.adapter_photo_name_item, parent, false);
        return new PhotoNameViewHolder(itemView);
    }

    @Override
    public final void onBindViewHolder(@NonNull PhotoNameViewHolder holder, int position) {
        final PhotoNameDelegate delegate = getItemAtPosition(position);
        if (null == delegate) {
            return;
        }
        holder.onBind(delegate, position);
        holder.itemView.setOnClickListener(new ItemClicker(delegate, position));
    }

    @Nullable
    public final PhotoNameDelegate getItemAtPosition (int position) {
        try {
            return getItem(position);
        }
        catch (Exception e) {
            return null;
        }
    }


    private class ItemClicker extends CommonItemWrapper<PhotoNameDelegate> implements View.OnClickListener {

        private ItemClicker (@NonNull PhotoNameDelegate delegate, int position) {
            super(delegate, "", position);
        }

        @Override
        public void onClick(View view) {
            if (null != holderCellClickCallback) {
                holderCellClickCallback.onHolderCellClicked(getDataObject(), getAction(), getPosition());
            }
        }
    }

    static class PhotoNameDiffCallback extends DiffUtil.ItemCallback<PhotoNameDelegate> {

        @Override
        public boolean areItemsTheSame(@NonNull PhotoNameDelegate oldItem, @NonNull PhotoNameDelegate newItem) {
            return oldItem.getIdentifier().equals(newItem.getIdentifier() );
        }

        @Override
        public boolean areContentsTheSame(@NonNull PhotoNameDelegate oldItem, @NonNull PhotoNameDelegate newItem) {
            return oldItem == newItem;
        }
    }

}
