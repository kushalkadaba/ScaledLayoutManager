package com.example.kushalkadaba.myapplication;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author Kushal Kadaba (Fuzz)
 */
public class SpStaggeredGridLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = SpStaggeredGridLayoutManager.class.getSimpleName();
    private int mFirstVisiblePosition;
    private int spanCount;

    public SpStaggeredGridLayoutManager(int spanCount) {
        this.spanCount = spanCount;
    }

    GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.DefaultSpanSizeLookup();

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //We have nothing to show for an empty data set but clear any existing views
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        //Clear all attached views into the recycle bin
        detachAndScrapAttachedViews(recycler);

        //Fill the grid for the initial layout of views
        fill(recycler, state);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        View firstChild = getChildAt(0);
        int startTopOffset = 0;
        int startLeftOffset = 0;
        if (firstChild != null) {
            mFirstVisiblePosition = getPosition(firstChild);
            startLeftOffset = getDecoratedLeft(firstChild);
            startTopOffset = getDecoratedTop(firstChild);
        }
        if (mFirstVisiblePosition < 0) mFirstVisiblePosition = 0;
        if (mFirstVisiblePosition >= getItemCount()) mFirstVisiblePosition = getItemCount() - 1;

        detachAndScrapAttachedViews(recycler);
        int leftOffset = startLeftOffset;
        int topOffset = startTopOffset;
        Log.i(TAG, String.valueOf(state.getItemCount()+" "+getItemCount()+" "+mFirstVisiblePosition));
        int nextPosition = mFirstVisiblePosition;

        while (nextPosition < getItemCount()) {

            //Layout this position
            View view = recycler.getViewForPosition(nextPosition);

            addView(view);

            Log.i(TAG, String.format("left offset %d", leftOffset));
            Log.i(TAG, String.format("top offset %d", topOffset));


            measureChildWithMargins(view, 0, 0);
            int decoratedChildWidth = getDecoratedMeasuredWidth(view);
            int decoratedChildHeight = getDecoratedMeasuredHeight(view);
            Log.i(TAG, String.format("bottom %d", topOffset + decoratedChildHeight));
            Log.i(TAG, String.format("right %d", leftOffset + decoratedChildWidth));
            layoutDecorated(view, leftOffset, topOffset,
                    leftOffset + decoratedChildWidth,
                    topOffset + decoratedChildHeight);


            leftOffset += decoratedChildWidth;
            topOffset += decoratedChildHeight;


            if (leftOffset >= getViewableWidth()) {
                leftOffset = startLeftOffset;
            }

            if (topOffset >= getViewableHeight()) {
                break;
            }
            nextPosition++;
        }

        //Remove anything that is left behind
        final List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
        for (int i = 0; i < scrapList.size(); i++) {
            final RecyclerView.ViewHolder removingView = scrapList.get(i);
            recycler.recycleView(removingView.itemView);
        }

    }

    private int getViewableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public static class LayoutParams extends StaggeredGridLayoutManager.LayoutParams {

        /*
         * Span Id for Views that are not laid out yet.
         */

        public static final int INVALID_SPAN_ID = -1;


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

    }

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    private int getViewableHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }
}
