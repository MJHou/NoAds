package cn.edu.zzti.soft.noads.listener;

import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView的点击监听
 */

public interface OnRecyclerViewItemClickListener<T> {
    void onItemClick(RecyclerView.Adapter adapter, T date, int position);
}
