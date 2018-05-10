package cn.edu.zzti.soft.noads.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.Setting;
import cn.edu.zzti.soft.noads.bean.MessageEvent;
import cn.edu.zzti.soft.noads.sql.model.TrojanData;
import cn.edu.zzti.soft.noads.utils.Constant;

/**
 * 管理用户标示木马的适配器
 */

public class TrojanAdapter extends RecyclerView.Adapter<TrojanAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<TrojanData> mData;

    //设置数据
    public void setData(ArrayList<TrojanData> data) {
        if (mData == null) {
            mData = data;
        } else {
//            mData.clear();
            if (data != null) mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    //设置数据
    public void addTrojan(TrojanData data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyItemChanged(mData.size() - 1);
    }

    /**
     * 初始化广告的适配器
     *
     * @param context 上下文
     */
    public TrojanAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 将视图和ViewHolder进行绑定
     *
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
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 根据position获取对应的AdData 对象
     *
     * @param position
     * @return
     */
    public TrojanData getItem(int position) {
        return mData.get(position);
    }

    @Override
    public void onClick(View v) {
        Object obj = v.getTag();
        if (obj instanceof Integer) {
            int position = (int) obj;
            TrojanData adData = mData.remove(position);
            switch (v.getId()) {
                case R.id.tv_delete:
                    adData.delete();
                    notifyItemRemoved(position);
                    //EventBus第三方组件，进行页面与页面的消息传递
                    MessageEvent event = new MessageEvent(adData, Constant.Event.EVENT_DELETE_TROJAN_PATH);
                    EventBus.getDefault().post(event);
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
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrojanData bean = getItem(position);
        String path = bean.getPath();
        Uri uri = Uri.parse(path);
        if (null != uri && !TextUtils.isEmpty(uri.getHost())) {
            path = uri.getHost();
        }
        holder.mTvTitle.setText(path);
        holder.mTvTitle.setEnabled(Setting.IS_START_INTERCEPTION);
        holder.mTvDelete.setEnabled(Setting.IS_START_INTERCEPTION);
        holder.mTvDelete.setTag(position);
    }
}
