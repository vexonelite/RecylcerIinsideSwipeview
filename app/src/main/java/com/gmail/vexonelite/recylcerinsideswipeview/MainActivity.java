package com.gmail.vexonelite.recylcerinsideswipeview;

import android.os.Bundle;
import android.util.Log;

import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameAdapter;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameDecoration;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameDelegate;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameImpl;
import com.gmail.vexonelite.recylcerinsideswipeview.widgets.ui.fling.FlingDispatchConstraintLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerView();
        setupFlingDispatchLayout();
    }

    ///

    private void setupRecyclerView() {
        final PhotoNameAdapter photoNameAdapter = new PhotoNameAdapter();
        photoNameAdapter.holderCellClickCallback = this::onItemCellClicked;

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(photoNameAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new PhotoNameDecoration(this));

        final Integer[] colorArray = {
                android.R.color.holo_red_light,
                android.R.color.holo_purple,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_dark,
                android.R.color.darker_gray,
        };
        final List<PhotoNameDelegate> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (Integer colorResId : colorArray) {
                int color = ContextCompat.getColor(this, colorResId);
                list.add(new PhotoNameImpl("", color, ""));
            }
        }

        photoNameAdapter.submitList(null);
        photoNameAdapter.submitList(list);
    }

    /** Implementation of HolderCellClickListener via method reference */
    private void onItemCellClicked(@NonNull PhotoNameDelegate delegate, @NonNull String action, final int position) {
        Log.i("MainActivity", "onItemCellClicked - position: " + position);
    }

    ///

    private void setupFlingDispatchLayout() {
        final FlingDispatchConstraintLayout flingDispatchLayout = findViewById(R.id.flingDispatchLayout);
        flingDispatchLayout.doInitialization(R.id.recyclerView, R.id.swipeView);
    }
}
