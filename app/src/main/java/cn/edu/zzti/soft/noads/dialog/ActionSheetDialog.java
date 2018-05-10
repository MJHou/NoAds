package cn.edu.zzti.soft.noads.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zzti.soft.noads.R;

/**
 * 仿ios弹窗(在中件)
 */
public class ActionSheetDialog {
    private Context mContext;
    private Dialog mDialog;
    private TextView mTvTitle;
    private TextView mTvCancel;
    private LinearLayout mLayoutContent;
    private RelativeLayout mLlParent;
    private ScrollView mScrollView;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;
    private OnCancelListener onCancelListener;

    public ActionSheetDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.ios_dialog_middle, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        view.setMinimumHeight(display.getHeight());
        // 获取自定义Dialog布局中的控件
        mScrollView =  view.findViewById(R.id.sLayout_content);
        mLlParent =  view.findViewById(R.id.rl_parent);
        mLayoutContent =  view.findViewById(R.id.lLayout_content);
        mTvTitle =  view.findViewById(R.id.txt_title);
        mTvCancel =  view.findViewById(R.id.txt_cancel);
        mTvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (onCancelListener != null)onCancelListener.onCancel();
            }
        });
        // 定义Dialog布局和参数
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        mDialog.setContentView(view);

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        mLlParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (onCancelListener != null)onCancelListener.onCancel();
            }
        });

        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    if (onCancelListener != null)onCancelListener.onCancel();
                    return true;
                }
                return false;
            }
        });
        return this;
    }

    public ActionSheetDialog setTitle(String title) {
        showTitle = true;
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(title);
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        mDialog.setCancelable(cancel);
        return this;
    }

    /**
     * 隐藏对话框
     */
    public void dismiss() {
        if (mDialog != null)
            mDialog.dismiss();
    }

    /**
     * 判断Dialog对话框是否显示
     *
     * @return
     */
    public boolean ishowing() {
        return mDialog != null ? mDialog.isShowing() : false;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * @param strItem  条目名称
     * @param color    条目字体颜色，设置null则默认蓝色
     * @param listener
     * @return
     */
    public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color,
                                          OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        sheetItemList.add(new SheetItem(strItem, color, listener));
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

        // 添加条目过多的时候控制高度(高度控制，非最佳解决办法)
        if (size >= 7) {
//            LinearLayout.LayoutParams params = (LayoutParams) sLayout_content
//                    .getLayoutParams();
//            params.height = display.getHeight() / 2;
//            sLayout_content.setLayoutParams(params);
            ViewGroup.LayoutParams params =  mScrollView.getLayoutParams();
            params.height = display.getHeight() / 2;
            mScrollView.setLayoutParams(params);
        }

        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;

            TextView textView = new TextView(mContext);
            textView.setText(strItem);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            textView.setGravity(Gravity.CENTER);

            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_shap);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
            }

            // 字体颜色
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }

            // 高度
            float scale = mContext.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, height));

            // 点击事件
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index);
                    mDialog.dismiss();
                }
            });

            mLayoutContent.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        mDialog.show();
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }
    public interface OnCancelListener {
        void onCancel();
    }


    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        public SheetItem(String name, SheetItemColor color,
                         OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    public enum SheetItemColor {
        Blue("#282828"), Red("#FF5A5A"), Back("#333333"), Gray("#909090");

        private String name;

        private     SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
