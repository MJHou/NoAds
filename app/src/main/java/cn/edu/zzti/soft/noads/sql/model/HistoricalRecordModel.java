package cn.edu.zzti.soft.noads.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cn.edu.zzti.soft.noads.sql.UserRecordsDatabase;

/**
 * 浏览历史纪录数据库
 */
@Table(database = UserRecordsDatabase.class, name = UserRecordsDatabase.T_USER_HISTORICAL_RECORDS)
public class HistoricalRecordModel extends BaseModel {
    //域名为唯一标示
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String title;//head

    @Column
    String path;//具体的url

    @Column
    long date;//查询的时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
