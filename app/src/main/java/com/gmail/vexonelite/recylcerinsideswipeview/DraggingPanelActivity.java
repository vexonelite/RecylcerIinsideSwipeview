package com.gmail.vexonelite.recylcerinsideswipeview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameAdapter;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameDecoration;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameDelegate;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameImpl;
import com.gmail.vexonelite.recylcerinsideswipeview.whiterabbit.dragqueen.DraggingPanel;

import java.util.ArrayList;
import java.util.List;

public final class DraggingPanelActivity extends AppCompatActivity {

    private LinearLayout mQueen;
    private Button mHidden, accept, decline;
    private DraggingPanel mDraggingPanel;
    private LinearLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_draggin_pannel);
//        mMainLayout = findViewById(R.id.main_layout);
//        mHidden = findViewById(R.id.hidden_button);
//        accept = findViewById(R.id.button1);
//        accept.setOnClickListener(this::onClick);
//        decline = findViewById(R.id.button2);
//        decline.setOnClickListener(this::onClick);
//        mHidden.setOnClickListener(this::onClick);
//        mQueen = findViewById(R.id.draggingButton);
//        mQueen.setOnClickListener(this::onClick);
//        mMainLayout.addOnLayoutChangeListener(this::onLayoutChange);
//        mDraggingPanel = findViewById(R.id.outer_layout);
//        mDraggingPanel.init(R.id.main_layout, mQueen);

        setContentView(R.layout.activity_list_inside_draggin_pannel);
        View draggingButton = findViewById(R.id.draggingButton);
        setupRecyclerView();
        mDraggingPanel = findViewById(R.id.outer_layout);
        mDraggingPanel.init(R.id.main_layout, draggingButton);
    }


    /** Implement View.OnLayoutChangeListener via method reference */
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (mDraggingPanel.isMoving()) {
            v.setTop(oldTop);
            v.setBottom(oldBottom);
            v.setLeft(oldLeft);
            v.setRight(oldRight);
        }
    }

    /** Implement View.OnClickListener via method reference */
    public void onClick(View v) {
        Button b = (Button) v;
        Toast t = Toast.makeText(this, b.getText() + " clicked", Toast.LENGTH_SHORT);
        t.show();
    }


    private void setupRecyclerView() {
        final PhotoNameAdapter photoNameAdapter = new PhotoNameAdapter();
        photoNameAdapter.holderCellClickCallback = this::onItemCellClicked;

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnLayoutChangeListener(this::onLayoutChange);
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
        Log.i("DraggingPanelActivity", "onItemCellClicked - position: " + position);
    }
}
