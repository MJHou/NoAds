package cn.edu.zzti.soft.noads.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.sql.AdDatabase;
import cn.edu.zzti.soft.noads.sql.typeconverter.PathConverter;

/**
 * 拦截广告的数据库
 */
@Table(database = AdDatabase.class, name = AdDatabase.T_AD_DATA)
public class AdData extends BaseModel {
    //数据库的path列
    //& searchbox/image/cmsuploader/   # id1,id1     ! bottomOperateTop,float &searchbox/image/cmsuploader/   #id1,id1     !bottomOperateTop,float
    //        路径                      divId    divClass

    //域名为唯一标示
    @PrimaryKey
    @Column
    public String host;

    //使用PathConverter 进行数据转化
    @Column(typeConverter = PathConverter.class)
    ArrayList<AdPathModel> paths;

    @Column
    int source;//  0系统  1  用户

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ArrayList<AdPathModel> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<AdPathModel> paths) {
        this.paths = paths;
    }

    public void addPath(AdPathModel path) {
        if (null == paths)
            paths = new ArrayList<>();
        paths.add(path);
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "AdData{" +
                "host='" + host + '\'' +
                ", paths=" + paths +
                ", source=" + source +
                '}';
    }
}
