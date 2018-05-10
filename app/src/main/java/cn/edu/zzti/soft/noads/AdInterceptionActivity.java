package cn.edu.zzti.soft.noads;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zzti.soft.noads.adapter.AdInterceptionAdapter;
import cn.edu.zzti.soft.noads.sql.SqlHelper;
import cn.edu.zzti.soft.noads.sql.model.AdData;
import cn.edu.zzti.soft.noads.utils.Constant;
import cn.edu.zzti.soft.noads.utils.SpUtil;
import cn.edu.zzti.soft.noads.widget.DrawableSwitch;

/**
 * 广告拦截页面
 */
public class AdInterceptionActivity extends Activity implements View.OnClickListener {
    private AdInterceptionAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_interception);
        findViewById(R.id.iv_back).setOnClickListener(this);
        DrawableSwitch drawableSwitch = findViewById(R.id.drawableSwitch);
        //设置开关状态
        drawableSwitch.setSwitchOn(Setting.IS_START_INTERCEPTION);
        drawableSwitch.setListener(new DrawableSwitch.MySwitchStateChangeListener() {
            @Override
            public void mySwitchStateChanged(boolean isSwitchOn) {
                //当状态改变，修改广告拦截设置
                Setting.IS_START_INTERCEPTION = isSwitchOn;
                //并保存到本地
                SpUtil.getInstance(AdInterceptionActivity.this).save(Constant.SP.SP_AD_INTERCEPTION,isSwitchOn);
                //更新视图
                mAdapter.notifyDataSetChanged();

            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager gridLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManger);
        mAdapter = new AdInterceptionAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //异步查询数据库
        SqlHelper.getAsyncAdData(new TransactionListener<List<AdData>>() {
            @Override
            public void onResultReceived(List<AdData> result) {
                if (null != result) {
                    mAdapter.setData((ArrayList<AdData>) result);
                }
            }

            @Override
            public boolean onReady(BaseTransaction<List<AdData>> transaction) {
                return true;
            }

            @Override
            public boolean hasResult(BaseTransaction<List<AdData>> transaction, List<AdData> result) {
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
