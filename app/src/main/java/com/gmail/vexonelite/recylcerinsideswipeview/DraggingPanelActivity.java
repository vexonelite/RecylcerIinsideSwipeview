package com.gmail.vexonelite.recylcerinsideswipeview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameAdapter;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameDecoration;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameDelegate;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.PhotoNameImpl;
import com.gmail.vexonelite.recylcerinsideswipeview.recyclerviews.RecyclerViewItemOnTouchImpl;
import com.gmail.vexonelite.recylcerinsideswipeview.whiterabbit.dragqueen.DraggingPanel;
import com.gmail.vexonelite.recylcerinsideswipeview.whiterabbit.dragqueen.OnSizeChangedDelegate;
import com.gmail.vexonelite.recylcerinsideswipeview.whiterabbit.dragqueen.ViewDragAdapter;

import java.util.ArrayList;
import java.util.List;

public final class DraggingPanelActivity extends AppCompatActivity {

    private LinearLayout mQueen;
    private Button mHidden, accept, decline;
    private DraggingPanel mDraggingPanel;
    private LinearLayout mMainLayout;
    private View draggingButton;
    private RecyclerView recyclerView;

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

        draggingButton = findViewById(R.id.draggingButton);

        setupRecyclerView();

        final ViewDragAdapterImpl viewDragAdapter = new ViewDragAdapterImpl();

        mDraggingPanel = findViewById(R.id.outer_layout);
        mDraggingPanel.onSizeChangedCallback = viewDragAdapter;
        mDraggingPanel.viewDragAdapter = viewDragAdapter;
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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnLayoutChangeListener(this::onLayoutChange);
        recyclerView.setAdapter(photoNameAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new PhotoNameDecoration(this));
        recyclerView.addOnItemTouchListener(new RecyclerViewItemOnTouchImpl());

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



    private class ViewDragAdapterImpl implements ViewDragAdapter, OnSizeChangedDelegate {
        /* Implementation of ViewDragAdapter */

        private int verticalRange = 0;
        private final Rect draggingPanelHitRect = new Rect();
        private final Rect draggingButtonHitRect = new Rect();
        private final Rect recyclerViewHitRect = new Rect();

        private boolean draggingPanelHasBeenInitialized = false;
        private boolean draggingButtonHasBeenInitialized = false;
        private boolean recyclerViewHasBeenInitialized = false;

        private boolean initializeHitRectIfNeeded(Boolean hasBeenInitialized,
                                                   @NonNull View view,
                                                   @NonNull Rect hitRect) {
            if (!hasBeenInitialized) {
                view.getGlobalVisibleRect(hitRect);
                Log.i("ViewDragAdapterImpl", "initializeHitRect - " +
                        view.getClass().getSimpleName() + " hitRect : " + hitRect);
                return true;
            }
            return false;
        }

        private void initializeHitRectsIfNeeded() {
            if (!draggingPanelHasBeenInitialized) {
                mDraggingPanel.getGlobalVisibleRect(draggingPanelHitRect);
                Log.i("ViewDragAdapterImpl", "initializeHitRect - mDraggingPanel hitRect : " + draggingPanelHitRect);
                draggingPanelHasBeenInitialized = true;
            }

            if (!draggingButtonHasBeenInitialized) {
                draggingButton.getGlobalVisibleRect(draggingButtonHitRect);
                Log.i("ViewDragAdapterImpl", "initializeHitRect - draggingButton hitRect : " + draggingButtonHitRect);
                draggingButtonHasBeenInitialized = true;
            }

            if (!recyclerViewHasBeenInitialized) {
                recyclerView.getGlobalVisibleRect(recyclerViewHitRect);
                Log.i("ViewDragAdapterImpl", "initializeHitRect - recyclerViewHitRect hitRect : " + recyclerViewHitRect);
                recyclerViewHasBeenInitialized = true;
            }
        }

        @Override
        public int getViewHorizontalDragRange(@Nullable View child) {
            return 0;
        }

        @Override
        public int getViewVerticalDragRange(@Nullable View child) {
            return verticalRange;
        }

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return (child.getId() == R.id.main_layout);
        }

        @Override
        public boolean doesHitTargetView(@NonNull MotionEvent motionEvent) {

            initializeHitRectsIfNeeded();
            Log.i("ViewDragAdapterImpl", "[Start ============================================]");
            //Log.i("ViewDragAdapterImpl", "doesHitTargetView - motionEvent: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");

            final float offsetX = motionEvent.getX() + ((float) draggingPanelHitRect.left);
            final float offsetY = motionEvent.getY() + ((float) draggingPanelHitRect.top);
            //Log.i("ViewDragAdapterImpl", "doesHitTargetView - offsetX: " + offsetX + ", offsetY: " + offsetY);

            final boolean hitDraggingButton = draggingButtonHitRect.contains((int) offsetX, (int) offsetY);
            final boolean hitRecyclerView = recyclerViewHitRect.contains((int) offsetX, (int) offsetY);

            if (hitDraggingButton) {
                Log.i("ViewDragAdapterImpl", "doesHitTargetView - hitDraggingButton: " + hitDraggingButton);
                Log.i("ViewDragAdapterImpl", "[End ==============================================]");
                return true;
            }
            else if (hitRecyclerView) {
                boolean canScrollDown = canRecyclerViewScrollDown();
                boolean canScrollUp = canRecyclerViewScrollUp();
                boolean hasReachedBottom = (!canScrollDown) && canScrollUp;
                boolean hasReachedTop = canScrollDown && (!canScrollUp);
//                Log.i("ViewDragAdapterImpl", "doesHitTargetView - canScrollDown: " + canScrollDown);
//                Log.i("ViewDragAdapterImpl", "doesHitTargetView - canScrollUp: " + canScrollUp);
//                Log.i("ViewDragAdapterImpl", "doesHitTargetView - hasReachedBottom: " + hasReachedBottom);
//                Log.i("ViewDragAdapterImpl", "doesHitTargetView - hasReachedTop: " + hasReachedTop);

                Log.i("ViewDragAdapterImpl", "doesHitTargetView - hitRecyclerView: " + hitRecyclerView);
                Log.e("ViewDragAdapterImpl", "mDraggingPanel close: " + mDraggingPanel.isOpen());
                if (!mDraggingPanel.isOpen()) {
                    if (hasReachedTop) {
                        final View itemView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                        if (null == itemView) {
                            Log.e("ViewDragAdapterImpl", "doesHitTargetView - No item is touched!");
                            Log.i("ViewDragAdapterImpl", "[End ==============================================]");
                            return true;
                        }
                        else {
                            Log.i("ViewDragAdapterImpl", "doesHitTargetView - Item is touched!");
                        }
                    }
                }
            }

            Log.i("ViewDragAdapterImpl", "[End ==============================================]");
            return false;
        }

        ///

        /* Implementation of OnSizeChangedDelegate */
        @Override
        public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
            verticalRange = (int) (height * 0.84);
        }
    }

    private boolean canRecyclerViewScrollUp() {
       return recyclerView.canScrollVertically(-1);
    }

    private boolean canRecyclerViewScrollDown() {
        return recyclerView.canScrollVertically(1);
    }

}


