package cn.edu.zzti.soft.noads.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.adapter.DialogContentAdapter;
import cn.edu.zzti.soft.noads.bean.DialogBean;
import cn.edu.zzti.soft.noads.listener.OnDismissListener;


/**
 * 仿ios弹窗（在底部）
 */
public class IosAlertDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg, mLlTwoBtn;
    private TextView mTvTitle, mTvContent, mTvAffirm, mTvCancel, mTvOneAffirm;
    private ListView mListView;
    private Display display;
    private View mViewTitle;

    public IosAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IosAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.ios_alert_dialog, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg = view.findViewById(R.id.lLayout_bg);
        mLlTwoBtn = view.findViewById(R.id.ll_two_btn);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvTitle.setVisibility(View.GONE);
        mViewTitle = view.findViewById(R.id.view_title);
        mViewTitle.setVisibility(View.GONE);
        mTvContent = view.findViewById(R.id.tv_content);
        mTvContent.setVisibility(View.GONE);
        mListView = view.findViewById(R.id.listview);
        mListView.setVisibility(View.GONE);
        mTvAffirm = view.findViewById(R.id.tv_affirm);
        mTvCancel = view.findViewById(R.id.tv_cancel);
        mTvOneAffirm = view.findViewById(R.id.tv_one_affirm);
        mTvOneAffirm.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

        return this;
    }

    public IosAlertDialog setCanceledOnTouchOutside(boolean flag) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(flag);
        }
        return this;
    }


    public IosAlertDialog setTitle(String title) {
        if (TextUtils.isEmpty(title))
            return this;
        if (mTvTitle.getVisibility() != View.VISIBLE) {
            mTvTitle.setVisibility(View.VISIBLE);
        }
        mTvTitle.setText(title);
        return this;
    }

    public IosAlertDialog setMsg(String msg) {
        return setMsg(msg, true);
    }

    /**
     * @param msg
     * @param show 显示位置  true  剧中显示
     * @return
     */
    public IosAlertDialog setMsg(String msg, boolean show) {
        if (TextUtils.isEmpty(msg)) {
            throw new RuntimeException("请输入正确的内容");
        }
        if (mTvContent.getVisibility() != View.VISIBLE) {
            mTvContent.setVisibility(View.VISIBLE);
        }
        if (show) {
            mTvContent.setGravity(Gravity.CENTER);
        } else {
            mTvContent.setGravity(Gravity.LEFT);
        }
        mTvContent.setText(msg);
        return this;
    }

    public IosAlertDialog setListContent(ArrayList<DialogBean> listContent) {
        if (listContent == null || listContent.size() < 1) {
            throw new RuntimeException("请输入正确的内容");
        }
        if (mListView.getVisibility() != View.VISIBLE) {
            mListView.setVisibility(View.VISIBLE);
        }
        mListView.setAdapter(new DialogContentAdapter(context, listContent));
        return this;
    }

    public IosAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 显示按钮
     *
     * @param type
     */
    private void showButton(int type) {
        switch (type) {
            case 1://显示一个按钮
                if (mLlTwoBtn.getVisibility() == View.VISIBLE || mLlTwoBtn.getVisibility() == View.INVISIBLE) {
                    mLlTwoBtn.setVisibility(View.GONE);
                }
                if (mTvOneAffirm.getVisibility() != View.VISIBLE)
                    mTvOneAffirm.setVisibility(View.VISIBLE);
                break;
            case 2://显示两个按钮
                if (mTvOneAffirm.getVisibility() == View.VISIBLE || mTvOneAffirm.getVisibility() == View.INVISIBLE) {
                    mTvOneAffirm.setVisibility(View.GONE);
                }
                if (mLlTwoBtn.getVisibility() != View.VISIBLE)
                    mLlTwoBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 取消按钮
     *
     * @param text
     * @param listener
     * @return
     */
    public IosAlertDialog setNegativeButton(String text,
                                            final OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            mTvCancel.setText(R.string.cancel);
        } else {
            mTvCancel.setText(text);
        }
        showButton(2);
        mTvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * 确认按钮
     *
     * @param text
     * @param listener
     * @return
     */
    public IosAlertDialog setPositiveButton(String text, final OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            mTvAffirm.setText(R.string.affirm);
        } else {
            mTvAffirm.setText(text);
        }
        showButton(2);
        mTvAffirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public IosAlertDialog setCloseButton(String text,
                                         final OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            mTvOneAffirm.setText(R.string.cancel);
        } else {
            mTvOneAffirm.setText(text);
        }
        showButton(1);
        mTvOneAffirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public void show() {
        if (mTvTitle != null && mTvTitle.getVisibility() == View.GONE && mTvContent != null) {
            mTvContent.setMinLines(2);
        }
        dialog.show();
    }

    public void dismiss() {
        if (dialog == null) return;
        dialog.dismiss();
    }

    public boolean isShow() {
        if (dialog == null)
            return false;
        return dialog.isShowing();
    }

    public IosAlertDialog setDismiss(final OnDismissListener listener) {
        if (listener == null)
            return this;
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                listener.onDismiss();
            }
        });
        return this;
    }

}
