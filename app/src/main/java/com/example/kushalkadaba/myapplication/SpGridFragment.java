package com.example.kushalkadaba.myapplication;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

/**
 * @author Kushal Kadaba (Fuzz)
 */
public class SpGridFragment extends Fragment {
    private static final String TAG = SpGridFragment.class.getSimpleName();
    @ViewDebug.ExportedProperty(indexMapping = {
                 @ViewDebug.IntToString(from = 0, to = "GREEN"),
                 @ViewDebug.IntToString(from = 1, to = "RED"),
                 @ViewDebug.IntToString(from = 2, to = "BLUE")
             })
    int[] colors = {R.color.gredn, R.color.red, R.color.blue};

    public static Fragment newInstance() {
        return new SpGridFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new SpStaggeredGridLayoutManager(1));
        recyclerView.addItemDecoration(new DividerDecoration((int) getResources().getDimension(R.dimen.separator)));
        recyclerView.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.i(TAG,String.valueOf(position));
            int color = position % colors.length;
            ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
            shapeDrawable.getPaint().setColor(colors[color]);
            shapeDrawable.invalidateSelf();
            holder.itemView.setBackground(shapeDrawable);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            int height;
            int scale = 50;
            height = (holder.itemView.getMeasuredHeight() + 1) * scale * (position + 1);
            params.height = height;
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(View inflate) {
            super(inflate);
        }
    }
}
