package cn.edu.zzti.soft.noads.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.base.BaseWebActivity;
import cn.edu.zzti.soft.noads.listener.OnRecyclerViewItemClickListener;
import cn.edu.zzti.soft.noads.sql.model.SearchRecordsModel;

/**
 * 搜索记录的适配器
 * See{@link AdInterceptionAdapter}
 */

public class SearchRecordsAdapter extends RecyclerView.Adapter<SearchRecordsAdapter.ViewHolder> implements View.OnClickListener {
    private BaseWebActivity mContext;
    private LayoutInflater mInflater;
    private ArrayList<SearchRecordsModel> mData;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public void setData(ArrayList<SearchRecordsModel> data) {
        if (mData == null) {
            mData = data;
        } else {
            mData.clear();
            if (data != null) mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public SearchRecordsAdapter(BaseWebActivity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_records, parent, false);
        return new ViewHolder(view, this);
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public SearchRecordsModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public void onClick(View v) {
        Object obj = v.getTag();

        if (obj instanceof Integer) {
            int position = (int) obj;
            SearchRecordsModel model = getItem(position);
            switch (v.getId()) {
                case R.id.iv_left_top:
                    mContext.setPath(model.getPath());
                    break;
                default:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(this, model, position);
                    }
                    break;
            }

        }
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle,//
                mTvPath;
        private View mParents;
        private View mIvSerch, mIvLeftTop;

        private ViewHolder(View view, View.OnClickListener listener) {
            super(view);
            mParents = view;
            mParents.setOnClickListener(listener);
            mTvTitle = view.findViewById(R.id.tv_title);
            mTvPath = view.findViewById(R.id.tv_path);
            mIvSerch = view.findViewById(R.id.iv_search);
            mIvLeftTop = view.findViewById(R.id.iv_left_top);
            mIvLeftTop.setOnClickListener(listener);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchRecordsModel bean = getItem(position);
        holder.mTvTitle.setText(bean.getTitle());
        holder.mTvPath.setText(bean.getPath());
        holder.mParents.setTag(position);
        holder.mIvLeftTop.setTag(position);
    }
}
