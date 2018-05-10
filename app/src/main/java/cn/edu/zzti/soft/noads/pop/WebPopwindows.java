package cn.edu.zzti.soft.noads.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.bean.PopBean;
import cn.edu.zzti.soft.weblib.AgentWebUtils;

/**
 * 未使用
 */
public class
WebPopwindows {
    private ArrayList<PopBean> mData;

    private LayoutInflater mInflater;
    private Context mContext;
    private View mDownView;//pop 显示在次试图下方;
    private PopupWindow mPop;


    private ListView mList;
    private WebPopwindows(Context context){
        mContext = context;
    }
    public WebPopwindows(Context context, View view, ArrayList<PopBean> data){
        if (null == context)
            throw new RuntimeException("");
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        init(view,data);
    }
    //初始化数据 和
    private void init(View view,ArrayList<PopBean> data) {
        mDownView = view;
        mData = data;
        final View contView = mInflater.inflate(R.layout.web_pop_list,null);
        mPop = new PopupWindow(contView, AgentWebUtils.dp2px(mContext,200), ViewGroup.LayoutParams.WRAP_CONTENT);
        mPop.setBackgroundDrawable(new BitmapDrawable());
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
        mList =  contView.findViewById(R.id.list);
        mAdapter = new PopAdapter();
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > -1 && position < mData.size()) {
                    if (null != mPopWindowListener) {
                        mPopWindowListener.selectTest(mData.get(position), position);
                        hidePop();
                    }
                }
            }
        });

        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (null != mPopWindowListener){
                    mPopWindowListener.close();
                }
            }
        });
    }
    public void show(int x,int y){
        if (mPop == null)
            init(mDownView,mData);
//        mPop.showAsDropDown(mDownView);
        mPop.showAtLocation(mDownView, Gravity.NO_GRAVITY,x,y);
    }
    public boolean isShowing(){
        return mPop == null? false : mPop.isShowing();
    }
    public void hidePop(){
        if (null != mPop && mPop.isShowing())
            mPop.dismiss();
    }

    private PopAdapter mAdapter;

    private PopWindowListener mPopWindowListener;
    public void setPopWindowListener(PopWindowListener popWindowListener){
        mPopWindowListener = popWindowListener;
    }
    public interface PopWindowListener{
        void close();
        void selectTest(PopBean bean, int position);
    }
    private class PopAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public PopBean getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.item_pop_text,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.tv =  convertView.findViewById(R.id.tv);
                viewHolder.view_bottom =  convertView.findViewById(R.id.view_bottom);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            PopBean item = getItem(position);
            viewHolder.tv.setText(item.text);
            if (position == mData.size() -1){
                viewHolder.view_bottom.setVisibility(View.GONE);
            }else {
                viewHolder.view_bottom.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }
    private static class ViewHolder {
        TextView tv;
        View view_bottom;
    }
}
