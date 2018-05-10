package cn.edu.zzti.soft.noads.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import cn.edu.zzti.soft.noads.bean.VersionInfo;
import cn.edu.zzti.soft.noads.dialog.IosAlertDialog;
import cn.edu.zzti.soft.noads.utils.FileUtil;
import cn.edu.zzti.soft.noads.utils.NetUtil;

/**
 *
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //显示下载的Dialog
    private void showDialog(final VersionInfo info) {
        if (info == null) return;
        StringBuilder builder = new StringBuilder();

        builder.append("版本更新\r\n(当前环境为");
        if (NetUtil.isWifi(this)) {
            builder.append("wifi");
        } else {
            builder.append("移动网络");
        }
        builder.append(")");
        IosAlertDialog iosAlertDialog = new IosAlertDialog(this).builder();
        iosAlertDialog.setMsg(info.content);
        iosAlertDialog.setTitle(builder.toString());
        switch (info.force) {
            case 0:
                iosAlertDialog.setNegativeButton("忽略", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                iosAlertDialog.setPositiveButton("更新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileUtil.getInstance(BaseActivity.this).downLoadApk(info);
                    }
                });
                break;
            case 1:
                iosAlertDialog.setCancelable(false);
                iosAlertDialog.setCanceledOnTouchOutside(false);
                iosAlertDialog.setCloseButton("更新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileUtil.getInstance(BaseActivity.this).downLoadApk(info);
                    }
                });
                break;
        }
        iosAlertDialog.show();
    }


    public interface OnPullRefreshListener {
        void onPullRefresh();
    }

    public interface OnNoNetRefreshListener {
        void onNoNetRefresh();
    }

    public interface PermissionAction {
        void done(boolean confirm);
    }
}

