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

import cn.edu.zzti.soft.noads.adapter.OfflineAdapter;
import cn.edu.zzti.soft.noads.bean.MessageEvent;
import cn.edu.zzti.soft.noads.listener.OnRecyclerViewItemClickListener;
import cn.edu.zzti.soft.noads.sql.SqlHelper;
import cn.edu.zzti.soft.noads.sql.model.OfflineFileModel;
import cn.edu.zzti.soft.noads.utils.Constant;

/**
 * 离线网页
 */
public class OfflineActivity extends Activity {

    private OfflineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager gridLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManger);
        mAdapter = new OfflineAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<OfflineFileModel>() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, OfflineFileModel date, int position) {
                EventBus.getDefault().post(new MessageEvent("", date.getPath(), Constant.Event.EVENT_OPEN_HISTORICAL_RECORDS));
                finish();
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        SqlHelper.getAsynOfflineFileModel(new TransactionListener<List<OfflineFileModel>>() {
            @Override
            public void onResultReceived(List<OfflineFileModel> result) {
                if (null != result) {
                    mAdapter.setData((ArrayList<OfflineFileModel>) result);
                }
            }

            @Override
            public boolean onReady(BaseTransaction<List<OfflineFileModel>> transaction) {
                return true;
            }

            @Override
            public boolean hasResult(BaseTransaction<List<OfflineFileModel>> transaction, List<OfflineFileModel> result) {
                return true;
            }
        });
    }
}
