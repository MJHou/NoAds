package cn.edu.zzti.soft.noads.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

import cn.edu.zzti.soft.noads.sql.UserRecordsDatabase;

/**
 * 搜索历史纪录
 */
@Table(database = UserRecordsDatabase.class, name = UserRecordsDatabase.T_USER_SEARCH_RECORDS)
public class SearchRecordsModel extends BaseModel {
    //域名为唯一标示
    @PrimaryKey
    String path;//输入路径

    @Column
    String title;//标题
    @Column
    Date date;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
