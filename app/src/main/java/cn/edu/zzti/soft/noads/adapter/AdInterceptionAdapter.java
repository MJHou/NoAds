package cn.edu.zzti.soft.noads.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.Setting;
import cn.edu.zzti.soft.noads.bean.MessageEvent;
import cn.edu.zzti.soft.noads.sql.model.AdData;
import cn.edu.zzti.soft.noads.utils.Constant;

/**
 * 管理用户标示的广告的适配器
 */

public class AdInterceptionAdapter extends RecyclerView.Adapter<AdInterceptionAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<AdData> mData;
    //设置数据
    public void setData(ArrayList<AdData> data) {
        if (mData == null) {
            mData = data;
        } else {
            mData.clear();
            if (data != null) mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 初始化广告的适配器
     * @param context 上下文
     */
    public AdInterceptionAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 将视图和ViewHolder进行绑定
     * @param parent
     * @param viewType 加载的view的类型（多种视图使用）
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_ad_interception, parent, false);
        return new ViewHolder(view, this);
    }

    /**
     * 获取mData的大小
     * @return
     */
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 根据position获取对应的AdData 对象
     * @param position
     * @return
     */
    public AdData getItem(int position) {
        return mData.get(position);
    }

    @Override
    public void onClick(View v) {
        Object obj = v.getTag();
        if (obj instanceof Integer) {
            int position = (int) obj;
            AdData adData = mData.remove(position);
            switch (v.getId()) {
                case R.id.tv_delete:
                    adData.delete();
                    notifyItemRemoved(position);
                    //EventBus第三方组件，进行页面与页面的消息传递
                    EventBus.getDefault().post(new MessageEvent(adData.getHost(), Constant.Event.EVENT_DELETE_AD_HOST));

                    break;
                default:
                    break;
            }

        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle,//
                mTvDelete;

        //ViewHolder
        private ViewHolder(View view, View.OnClickListener listener) {
            super(view);
            mTvTitle = view.findViewById(R.id.tv_title);
            mTvDelete = view.findViewById(R.id.tv_delete);
            mTvDelete.setOnClickListener(listener);
        }
    }

    /**
     * 将数据和试图进行绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AdData bean = getItem(position);
        holder.mTvTitle.setText(bean.getHost());
        holder.mTvTitle.setEnabled(Setting.IS_START_INTERCEPTION);
        holder.mTvDelete.setEnabled(Setting.IS_START_INTERCEPTION);
        holder.mTvDelete.setTag(position);
    }
}
