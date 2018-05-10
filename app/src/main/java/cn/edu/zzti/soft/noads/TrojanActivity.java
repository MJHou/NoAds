package cn.edu.zzti.soft.noads;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zzti.soft.noads.adapter.TrojanAdapter;
import cn.edu.zzti.soft.noads.bean.MessageEvent;
import cn.edu.zzti.soft.noads.sql.SqlHelper;
import cn.edu.zzti.soft.noads.sql.model.TrojanData;
import cn.edu.zzti.soft.noads.utils.Constant;
import cn.edu.zzti.soft.noads.utils.SpUtil;
import cn.edu.zzti.soft.noads.widget.DrawableSwitch;

public class TrojanActivity extends Activity {
    private TrojanAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trojan);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrojanActivity.this,AddTrojanActivity.class));
            }
        });
        DrawableSwitch drawableSwitch = findViewById(R.id.drawableSwitch);
        drawableSwitch.setSwitchOn(Setting.IS_START_TROJAB_INTERCEPTION);
        drawableSwitch.setListener(new DrawableSwitch.MySwitchStateChangeListener() {
            @Override
            public void mySwitchStateChanged(boolean isSwitchOn) {
                Setting.IS_START_INTERCEPTION = isSwitchOn;
                SpUtil.getInstance(TrojanActivity.this).save(Constant.SP.SP_TROJAN_INTERCEPTION, isSwitchOn);

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager gridLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManger);
        mAdapter = new TrojanAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //异步查询数据库
        SqlHelper.getAsyncTrojan(new TransactionListener<List<TrojanData>>() {
            @Override
            public void onResultReceived(List<TrojanData> result) {
                if (null != result) {
                    mAdapter.setData((ArrayList<TrojanData>) result);
                }
            }

            @Override
            public boolean onReady(BaseTransaction<List<TrojanData>> transaction) {
                return true;
            }

            @Override
            public boolean hasResult(BaseTransaction<List<TrojanData>> transaction, List<TrojanData> result) {
                return true;
            }
        });
    }

    @Subscribe
    public void eventBus(MessageEvent event) {
        switch (event.getType()) {
            case Constant.Event.EVENT_ADD_TROJAN_PATH:
                mAdapter.addTrojan((TrojanData)event.getObj());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
