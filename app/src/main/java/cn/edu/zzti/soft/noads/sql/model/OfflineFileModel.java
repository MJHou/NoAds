package cn.edu.zzti.soft.noads.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cn.edu.zzti.soft.noads.sql.UserRecordsDatabase;

/**
 * 离线网页
 */
@Table(database = UserRecordsDatabase.class, name = UserRecordsDatabase.T_USER_OFFLINE_FILE)
public class OfflineFileModel extends BaseModel {
    //域名为唯一标示
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String title;//标题

    @Column
    String path;//保存本地的路径

    @Column
    long date;//保存时间

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
