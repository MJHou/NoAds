package cn.edu.zzti.soft.noads.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.listener.OnRecyclerViewItemClickListener;
import cn.edu.zzti.soft.noads.sql.model.HistoricalRecordModel;

/**
 * 历史纪录适配器
 * See{@link AdInterceptionAdapter}
 */

public class HistoricalRecordAdapter extends RecyclerView.Adapter<HistoricalRecordAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<HistoricalRecordModel> mData;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public void setData(ArrayList<HistoricalRecordModel> data) {
        if (mData == null) {
            mData = data;
        } else {
            mData.clear();
            if (data != null) mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public HistoricalRecordAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_historical_records, parent, false);
        return new ViewHolder(view, this);
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public HistoricalRecordModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public void onClick(View v) {
        Object obj = v.getTag();
        if (obj instanceof Integer) {
            int position = (int) obj;
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(this, getItem(position), position);
            }
        }
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle,//
                mTvPath;
        private View mParent;

        private ViewHolder(View view, View.OnClickListener listener) {
            super(view);
            mParent = view;
            view.setOnClickListener(listener);
            mTvTitle = view.findViewById(R.id.tv_title);
            mTvPath = view.findViewById(R.id.tv_path);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoricalRecordModel bean = getItem(position);
        holder.mTvTitle.setText(bean.getTitle());
        holder.mTvPath.setText(bean.getPath());
        holder.mParent.setTag(position);
    }
}
