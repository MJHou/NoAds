package cn.edu.zzti.soft.noads;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zzti.soft.noads.adapter.HistoricalRecordAdapter;
import cn.edu.zzti.soft.noads.bean.MessageEvent;
import cn.edu.zzti.soft.noads.listener.OnRecyclerViewItemClickListener;
import cn.edu.zzti.soft.noads.sql.SqlHelper;
import cn.edu.zzti.soft.noads.sql.model.HistoricalRecordModel;
import cn.edu.zzti.soft.noads.utils.Constant;

public class HistoricalRecordActivity extends Activity {

    private HistoricalRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_record);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager gridLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManger);
        mAdapter = new HistoricalRecordAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<HistoricalRecordModel>() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, HistoricalRecordModel date, int position) {
                EventBus.getDefault().post(new MessageEvent("",date.getPath(), Constant.Event.EVENT_OPEN_HISTORICAL_RECORDS));
                finish();
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        SqlHelper.getAsyncHistoricalRecordModel(new TransactionListener<List<HistoricalRecordModel>>() {
            @Override
            public void onResultReceived(List<HistoricalRecordModel> result) {
                if (null != result) {
                    mAdapter.setData((ArrayList<HistoricalRecordModel>) result);
                }
            }

            @Override
            public boolean onReady(BaseTransaction<List<HistoricalRecordModel>> transaction) {
                return true;
            }

            @Override
            public boolean hasResult(BaseTransaction<List<HistoricalRecordModel>> transaction, List<HistoricalRecordModel> result) {
                return true;
            }
        });
    }
}
