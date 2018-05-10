package cn.edu.zzti.soft.noads.sql;

import android.net.Uri;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import cn.edu.zzti.soft.noads.sql.model.AdData;
import cn.edu.zzti.soft.noads.sql.model.AdData_Table;
import cn.edu.zzti.soft.noads.sql.model.HistoricalRecordModel;
import cn.edu.zzti.soft.noads.sql.model.OfflineFileModel;
import cn.edu.zzti.soft.noads.sql.model.SearchRecordsModel;
import cn.edu.zzti.soft.noads.sql.model.SearchRecordsModel_Table;
import cn.edu.zzti.soft.noads.sql.model.TrojanData;
import cn.edu.zzti.soft.noads.sql.model.TrojanData_Table;

/**
 * 数据查询的帮助类
 */

public class SqlHelper {

    public static AdData getSyncAdData(Uri uri) {
        return uri == null ? null : getSyncAdDataModel(uri.getHost());
    }


    private static AdData getSyncAdDataModel(String url) {
        if (TextUtils.isEmpty(url)) return null;
        return SQLite.select().from(AdData.class).where(AdData_Table.host.eq(url)).querySingle();
    }

    public static void getAsyncAdData(String url, TransactionListener<AdData> listener) {
        if (TextUtils.isEmpty(url)) return;
        getAsyncAdData(Uri.parse(url), listener);
    }

    public static void getAsyncAdData(TransactionListener<List<AdData>> listener) {
        SQLite.select()
                .from(AdData.class)
                .where(AdData_Table.source.eq(1))
                .async()
                .queryList(listener);
    }
    public static void getAsyncTrojan(TransactionListener<List<TrojanData>> listener) {
        SQLite.select()
                .from(TrojanData.class)
                .where(TrojanData_Table.source.eq(1))
                .async()
                .queryList(listener);
    }

    public static void getAsyncHistoricalRecordModel(TransactionListener<List<HistoricalRecordModel>> listener) {
        SQLite.select()
                .from(HistoricalRecordModel.class)
                .where()
                .async()
                .queryList(listener);
    }
    public static void getAsynOfflineFileModel(TransactionListener<List<OfflineFileModel>> listener) {
        SQLite.select()
                .from(OfflineFileModel.class)
                .where()
                .async()
                .queryList(listener);
    }

    public static void getAsyncAdData(Uri uri, TransactionListener<AdData> listener) {
        if (null == uri) return;
        getAsyncUrlModel(uri.getHost(), listener);
    }

    private static void getAsyncUrlModel(String url, TransactionListener<AdData> listener) {
        if (TextUtils.isEmpty(url)) return;
        SQLite.select()
                .from(AdData.class)
                .where(AdData_Table.host.eq(url))
                .async()
                .querySingle(listener);
    }


    /**
     * 获取指定链接是否存在木马库内
     *
     * @param url
     * @return
     */
    public static TrojanData getSyncTrojanData(String url) {
        if (TextUtils.isEmpty(url)) return null;
        return SQLite.select().from(TrojanData.class).where(TrojanData_Table.path.eq(url)).querySingle();
    }

    /**
     * 同步获取所有的木马链接
     *
     * @return
     */
    public static List<TrojanData> getSyncListTrojan() {
        return SQLite.select()
                .from(TrojanData.class)
                .queryList();
    }

    public static void getAsyncListSearch(String path, TransactionListener<List<SearchRecordsModel>> listener) {
        if (TextUtils.isEmpty(path)) {
            SQLite.select().from(SearchRecordsModel.class)
                    .orderBy(SearchRecordsModel_Table.date, false)
                    .async()
                    .queryList(listener);
        } else {
            SQLite.select().from(SearchRecordsModel.class).where(SearchRecordsModel_Table.path.like(""))
                    .orderBy(SearchRecordsModel_Table.date, false)
                    .async()
                    .queryList(listener);
        }
    }

}
