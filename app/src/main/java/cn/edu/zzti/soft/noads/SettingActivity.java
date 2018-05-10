package cn.edu.zzti.soft.noads;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.edu.zzti.soft.noads.bean.MessageEvent;
import cn.edu.zzti.soft.noads.utils.Constant;

public class SettingActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.ll_ad_interception).setOnClickListener(this);
        findViewById(R.id.ll_trojan_interception).setOnClickListener(this);
        findViewById(R.id.ll_historical_record).setOnClickListener(this);
        findViewById(R.id.ll_offline).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void eventBus(MessageEvent event) {
        switch (event.getType()) {
            case Constant.Event.EVENT_OPEN_HISTORICAL_RECORDS:
                finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_ad_interception:
                startActivity(new Intent(this, AdInterceptionActivity.class));
                break;
            case R.id.ll_trojan_interception:
                startActivity(new Intent(this, TrojanActivity.class));
                break;
            case R.id.ll_historical_record:
                startActivity(new Intent(this, HistoricalRecordActivity.class));
                break;
            case R.id.ll_offline:
                startActivity(new Intent(this, OfflineActivity.class));
                break;
        }
    }
}
