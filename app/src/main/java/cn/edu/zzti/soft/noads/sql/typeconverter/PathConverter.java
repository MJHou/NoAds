package cn.edu.zzti.soft.noads.sql.typeconverter;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.sql.model.AdPathModel;

/**
 * @author houmengjie
 * @date 18/4/26
 */
public class PathConverter extends TypeConverter<String, ArrayList<AdPathModel>> {


    @Override
    public String getDBValue(ArrayList<AdPathModel> model) {
        if (null == model && model.isEmpty()) return null;
        StringBuilder builder = new StringBuilder();
        for (AdPathModel adPathModel : model) {
            if (TextUtils.isEmpty(adPathModel.getPath())) continue;
            builder.append("&");
            builder.append(adPathModel.getPath());
            if (null != adPathModel.getDivIds()) {
                builder.append("#");
                builder.append(assemblyString(adPathModel.getDivIds()));
            }
            if (null != adPathModel.getDivClasss()) {
                builder.append("!");
                builder.append(assemblyString(adPathModel.getDivClasss()));
            }
        }
        return builder.toString();
    }

    private String assemblyString(ArrayList<String> model) {
        if (null == model && model.isEmpty()) return null;
        StringBuilder builder = new StringBuilder();
        for (String str : model) {
            builder.append(str);
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    @Override
    public ArrayList<AdPathModel> getModelValue(String data) {
        if (TextUtils.isEmpty(data)) return null;
        String[] values = data.split("&");
        ArrayList<AdPathModel> adPathModels = new ArrayList<>(values.length);
        AdPathModel adPathModel;
        for (String str : values) {
            if (TextUtils.isEmpty(str)) continue;
            String[] path = str.split("#");
            adPathModel = new AdPathModel();
            if (path.length == 2) {
                adPathModel.setPath(path[0]);
                path = path[1].split("!");
                adPathModel.setDivIds(splitArray(path[0]));
                if (path.length > 1) {
                    adPathModel.setDivClasss(splitArray(path[1]));
                }
            } else {
                path = path[0].split("!");
                adPathModel.setPath(path[0]);
                if (path.length > 1) {
                    adPathModel.setDivClasss(splitArray(path[1]));
                }
            }
            adPathModels.add(adPathModel);
        }
        return adPathModels;
    }

    private ArrayList<String> splitArray(String data) {
        if (TextUtils.isEmpty(data)) return null;
        String[] values = data.split(",");
        ArrayList<String> arrayList = new ArrayList<>(values.length);
        for (String str : values) {
            arrayList.add(str);
        }
        return arrayList;
    }
}
