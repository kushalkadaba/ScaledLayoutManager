package com.example.kushalkadaba.myapplication;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Kushal Kadaba (Fuzz)
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {
    final int size;

    public DividerDecoration(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = size;
        outRect.right = size;
    }
}