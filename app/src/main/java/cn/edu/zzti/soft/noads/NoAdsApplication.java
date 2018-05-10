package cn.edu.zzti.soft.noads;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

import cn.edu.zzti.soft.noads.utils.Constant;
import cn.edu.zzti.soft.noads.utils.SpUtil;

/**
 * 每个应用的Application对象
 *
 * 初始化DBFlow数据库，以及获取用户是设置木马拦截和广告屏蔽
 */

public class NoAdsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(getApplicationContext());
        Setting.IS_START_INTERCEPTION = SpUtil.getInstance(this).getBooleanValue(Constant.SP.SP_AD_INTERCEPTION,true);
        Setting.IS_START_TROJAB_INTERCEPTION = SpUtil.getInstance(this).getBooleanValue(Constant.SP.SP_TROJAN_INTERCEPTION,true);
    }
}
