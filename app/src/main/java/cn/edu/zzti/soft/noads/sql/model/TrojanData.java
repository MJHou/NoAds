package cn.edu.zzti.soft.noads.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cn.edu.zzti.soft.noads.sql.AdDatabase;

/**
 * 拦截链接的url
 */
@Table(database = AdDatabase.class, name = AdDatabase.T_TROJAN_DATA)
public class TrojanData extends BaseModel {
    //域名为唯一标示
    @PrimaryKey
    @Column
    public String path;

    @Column
    int source;// 0系统  1  用户


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column
    int type;// 0 其它 1 木马和钓鱼网站  2不良网站


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public boolean isCarriedOut =false;

}
