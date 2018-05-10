package cn.edu.zzti.soft.noads.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.dialog.IosAlertDialog;
import cn.edu.zzti.soft.noads.listener.OnRecyclerViewItemClickListener;
import cn.edu.zzti.soft.noads.sql.model.OfflineFileModel;
import cn.edu.zzti.soft.noads.utils.FileUtil;
import cn.edu.zzti.soft.noads.utils.TimeUtil;

/**
 * 离线适配器
 * See{@link AdInterceptionAdapter}
 */

public class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<OfflineFileModel> mData;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public void setData(ArrayList<OfflineFileModel> data) {
        if (mData == null) {
            mData = data;
        } else {
            mData.clear();
            if (data != null) mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public OfflineAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_offline, parent, false);
        return new ViewHolder(view, this,this);
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public OfflineFileModel getItem(int position) {
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

    @Override
    public boolean onLongClick(View v) {
        int position = (int) v.getTag();
        final OfflineFileModel model = mData.get(position);
        StringBuilder builder = new StringBuilder("您确认删除<<");
        builder.append(model.getTitle());
        builder.append(">> 离线网页?");
        new IosAlertDialog(mContext).builder()
                .setTitle("提示")
                .setMsg(builder.toString())
                .setNegativeButton("暂不删除",null)
                .setPositiveButton("立即删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.remove(model);
                        FileUtil.deleteHtml(model.getPath());
                        model.delete();
                        notifyDataSetChanged();
                    }
                })
                .show();
        return true;
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle,//
                mTvTime;
        private View mParent;

        private ViewHolder(View view, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
            super(view);
            mParent = view;
            view.setOnClickListener(listener);
            mTvTitle = view.findViewById(R.id.tv_title);
            mTvTime = view.findViewById(R.id.tv_time);
            mParent.setOnLongClickListener(longClickListener);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OfflineFileModel bean = getItem(position);
        holder.mTvTitle.setText(bean.getTitle());
        holder.mTvTime.setText(TimeUtil.formatTime(bean.getDate()));
        holder.mParent.setTag(position);
    }
}
