package cn.edu.zzti.soft.noads.handler;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import cn.edu.zzti.soft.noads.base.BaseWebActivity;
import cn.edu.zzti.soft.noads.utils.Constant;

/**
 * BaseWebActivity的handle
 */

public final class WebHandler extends Handler {
    private final BaseWebActivity activity;

    public WebHandler(BaseWebActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.Message.MESSAGE_LONG_CLICK://网页长按，如果未找到对应类型，则会发出此类行的消息
                String src = msg.getData().getString("src");
                if (TextUtils.isEmpty(src)) {
                    src = msg.getData().getString("url");
                }
                if (!TextUtils.isEmpty(src)) {
                    activity.showSrcDialog(src);//显示
                }

                break;
            default:
                super.handleMessage(msg);
                break;
        }
        //如文档所述，返回三个键：url，src和title
        //但是在kitkat 4.4中url和src是相同的
        //在4.2.2和4.3上写入文档url指向锚的href属性，src指向img.src属性
        for (String s : msg.getData().keySet()) {
            System.out.println("ImagePickerActivity.LongClickHandler.handleMessage()" + s + " = " + msg.getData().get(s));
        }
    }
}
