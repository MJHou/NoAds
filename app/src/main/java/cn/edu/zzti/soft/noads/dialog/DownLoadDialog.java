package cn.edu.zzti.soft.noads.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.DecimalFormat;

import cn.edu.zzti.soft.noads.R;


/**
 * 下载对话（在底部）
 */
public class DownLoadDialog {

    private Context context;
    private Dialog dialog;
    private RelativeLayout lLayout_bg;
    private TextView mTvNumber, mTvMessage, mTvPercent;
    private ProgressBar mPb;
    private Display display;
    private Handler mViewUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TextUtils.isEmpty(mMaxString) || mMax == 0) {
                mMax = mPb.getMax();
                mMaxString = getDataSize(mMax);
            }
            if (TextUtils.isEmpty(mProgressString)) {
                mProgress = mPb.getProgress();
                mProgressString = getDataSize(mProgress);
            }
            mTvNumber.setText(mProgressString + " / " + mMaxString);
            if (mMax != 0) {
                mTvPercent.setText(String.valueOf(mProgress * 100 / mMax) + " % ");
            }else {
                mTvPercent.setText(String.valueOf("100 % "));
            }
        }
    };


    public DownLoadDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public DownLoadDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.download_alert_dialog, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg =  view.findViewById(R.id.lLayout_bg);
        mTvNumber =  view.findViewById(R.id.tv_number);
        mTvMessage =  view.findViewById(R.id.tv_message);
        mPb =  view.findViewById(R.id.pb_progress);
        mTvPercent =  view.findViewById(R.id.tv_percent);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

        return this;
    }

    /**
     * @param msg
     * @return
     */
    public DownLoadDialog setMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            mTvMessage.setText(msg);
        }
        return this;
    }

    public DownLoadDialog setMax(int maxProgress) {
        mPb.setMax(maxProgress);
        mMax = mPb.getMax();
        mMaxString = getDataSize(mMax);
        onProgressChanged();
        return this;
    }

    private String mMaxString, mProgressString;
    private int mMax, mProgress;

    public DownLoadDialog setProess(int progress) {
        mPb.setProgress(progress);
        mProgress = mPb.getProgress();
        mProgressString = getDataSize(mProgress);
        onProgressChanged();
        return this;
    }

    private void onProgressChanged() {
        if (mViewUpdateHandler != null && !mViewUpdateHandler.hasMessages(0)) {
            mViewUpdateHandler.sendEmptyMessage(0);
        }
    }

    public void show() {
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }


    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    public String getDataSize(long size) {
        if (size < 0) {
            size = 0;
        }
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return String.valueOf(size);
        }

    }
}
