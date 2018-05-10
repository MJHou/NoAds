package cn.edu.zzti.soft.noads.sql.model;

import android.text.TextUtils;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.utils.StringUtil;

/**
 * 路径所对应的divId 和class
 */
public class AdPathModel {
    //路径
    private String path;
    //div 的id集合
    private ArrayList<String> divIds;
    //div的class集合
    private ArrayList<String> divClasss;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = StringUtil.formatPath(path);
    }

    public ArrayList<String> getDivIds() {
        return divIds;
    }

    public void setDivIds(ArrayList<String> divIds) {
        this.divIds = divIds;
    }
    public void addDivId(String divId) {
        if (TextUtils.isEmpty(divId))return;
        if (divIds == null)
            divIds = new ArrayList<>();
        this.divIds.add(divId);
    }
    public void addDivClass(String divClass) {
        if (TextUtils.isEmpty(divClass))return;
        if (divClasss == null)
            divClasss = new ArrayList<>();
        this.divClasss.add(divClass);
    }

    public ArrayList<String> getDivClasss() {
        return divClasss;
    }

    public void setDivClasss(ArrayList<String> divClasss) {
        this.divClasss = divClasss;
    }

    @Override
    public String toString() {
        return "AdPathModel{" +
                "path='" + path + '\'' +
                ", divIds=" + divIds +
                ", divClasss=" + divClasss +
                '}';
    }
}
