package com.example.kushalkadaba.myapplication;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final int FUSED_VIEW = 1;
    private static final int NORMAL_VIEW = 2;
    private static final int NONE_VIEW = 3;
    private static final int POSITION_TAG = R.id.position;
    RecyclerView recyclerView;
    int deviceWidth;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Point size = new Point(10, 10);
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        deviceWidth = size.x;
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        MyAdapter adapter = new MyAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration((int) getResources().getDimension(R.dimen.separator)));
        recyclerView.setAdapter(adapter);
    }

    public static Fragment newInstance() {
        return new MainActivityFragment();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        int scale = 50;

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = null;
            if (viewType == NORMAL_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item, parent, false);
            } else if (viewType == FUSED_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_line_item, parent, false);
            } else if (viewType == NONE_VIEW) {
                v = new View(getContext());
            }
            return new MyHolder(v);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            if (position == 1) {
                return;
            }
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            int height;
            height = (holder.itemView.getMeasuredHeight() + 1) * scale * (position + 1);
            params.height = height;
            if (position == 0) {
                params.height *= 10;
                params.setFullSpan(true);
                holder.itemView.findViewById(R.id.primary).setTag(POSITION_TAG, position);
                holder.itemView.findViewById(R.id.primary).setOnClickListener(cognizantClickListener);
                holder.itemView.findViewById(R.id.secondary).setTag(POSITION_TAG, position + 1);
                holder.itemView.findViewById(R.id.secondary).setOnClickListener(cognizantClickListener);
                holder.itemView.findViewById(R.id.secondary).getLayoutParams().height = height * 5;
                ((ViewGroup.MarginLayoutParams)holder.itemView.findViewById(R.id.secondary).getLayoutParams()).topMargin = height * 5;
            } else {
                holder.itemView.setTag(POSITION_TAG, position);
                holder.itemView.setOnClickListener(cognizantClickListener);
            }

        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return FUSED_VIEW;
            } else if (position == 1) {
                return NONE_VIEW;
            } else {
                return NORMAL_VIEW;
            }
        }

        @Override
        public int getItemCount() {
            return 7;
        }

    }

    private View.OnClickListener cognizantClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag(POSITION_TAG) != null) {
                Snackbar.make(getRootView(), String.format("Position %s clicked", String.valueOf(v.getTag(POSITION_TAG))),
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    private View getRootView() {
        MainActivity mainActivity = (MainActivity) getActivity();
        return mainActivity.findViewById(R.id.rootLayout);
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);

        }
    }
}
