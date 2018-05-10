package cn.edu.zzti.soft.noads.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.bean.DialogBean;

/**
 * dialog 内容列表的适配器
 */
public class DialogContentAdapter extends VBaseAdapter {
    private Context mContext;
    private ArrayList<DialogBean> mData;
    private LayoutInflater mInflater;
    private boolean isMergeTitle = true;//标识是否合并标题  true  合并  false  合并

    public DialogContentAdapter(Context context, ArrayList<DialogBean> data) {
        if (context == null)
            throw new RuntimeException("上下文为空");
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public DialogBean getItem(int position) {
        return mData.get(position);
    }

    /**
     * 数据与视图进行绑定
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DialogView dialogView;
        if (convertView == null) {
            //创建ViewHolder，并绑定对应的视图
            dialogView = new DialogView();
            convertView = mInflater.inflate(R.layout.item_ios_alert_content, null);
            dialogView.mTvTitle = convertView.findViewById(R.id.tv_item_title);
            dialogView.mTvContent = convertView.findViewById(R.id.tv_item_content);
            convertView.setTag(dialogView);
        } else {
            //复用ViewHolder;
            dialogView = (DialogView) convertView.getTag();
        }
        //数据绑定
        DialogBean bean = getItem(position);
        if (!TextUtils.isEmpty(bean.getTitle())) {
            if (isMergeTitle) {
                if (position == 0 || !TextUtils.equals(mData.get(position - 1).getTitle(), bean.getTitle())) {
                    dialogView.mTvTitle.setText(bean.getTitle());
                    dialogView.mTvTitle.setVisibility(View.VISIBLE);
                } else {
                    dialogView.mTvTitle.setVisibility(View.GONE);
                }
            } else {
                dialogView.mTvTitle.setText(bean.getTitle());
                dialogView.mTvTitle.setVisibility(View.VISIBLE);
            }
        } else {
            dialogView.mTvTitle.setVisibility(View.GONE);
        }
        dialogView.mTvContent.setText(bean.getContent());
        return convertView;
    }

    static class DialogView {
        TextView mTvTitle;
        TextView mTvContent;
    }
}
