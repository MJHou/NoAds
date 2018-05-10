package cn.edu.zzti.soft.noads.jsinterface;

import java.util.ArrayList;

import cn.edu.zzti.soft.noads.sql.model.AdPathModel;

/**
 * 一些注入的js代码
 */

public class JsCode {
    /**
     * 根据 divClass删除div
     * @param className div的class
     * @return js
     */
    public static String getClearClassDivJs(String className) {
        StringBuilder js = new StringBuilder("javascript:");
        js.append("var elements = document.getElementsByClassName('");
        js.append(className);
        js.append("');");
        js.append("while(elements.length > 0){elements[0].parentNode.removeChild(elements[0]);}");
        js.append("");
        return js.toString();
    }
    /**
     * @return 获取html源码的js
     */
    public static String getHtmlCore() {
        StringBuilder js = new StringBuilder("javascript:window.HTMLOUT.processHTML('<head>'+ document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        return js.toString();
    }

    /**
     * 屏蔽当前host的所有保存的divId和divClass
     * @param model
     * @return js
     */
    public static String getAdPathModelJs(AdPathModel model) {
        if (model == null) return "";
        StringBuilder builder = new StringBuilder("javascript:");
        int length = builder.length();
        if (model.getDivClasss() != null && !model.getDivClasss().isEmpty()) {
            for (String str : model.getDivClasss()) {
                builder.append("var elements");
                builder.append(str);
                builder.append(" = document.getElementsByClassName('");
                builder.append(str);
                builder.append("');");
                builder.append("while(elements");
                builder.append(str);
                builder.append(".length > 0){elements");
                builder.append(str);
                builder.append("[0].parentNode.removeChild(elements");
                builder.append(str);
                builder.append("[0]);}");
            }
        }
        if (model.getDivIds() != null && !model.getDivIds().isEmpty()) {
            for (String str : model.getDivIds()) {
                builder.append("var elements");
                builder.append(str);
                builder.append(" = document.getElementsByClassName('");
                builder.append(str);
                builder.append("');");
                builder.append("while(elements");
                builder.append(str);
                builder.append(".length > 0){elements");
                builder.append(str);
                builder.append("[0].parentNode.removeChild(elements");
                builder.append(str);
                builder.append("[0]);}");
            }
        }
        return builder.length() > length ? builder.toString() : "";
    }

    /**
     * 根据角标删除div
     * @param url
     * @param index
     * @return js
     */
    public static String getDivIdOrClass(String url,int index) {
        index--;
        StringBuilder builder = new StringBuilder("javascript:");
        builder.append("var div = document.getElementsByTagName('div')[");
        builder.append(index);
        builder.append("]; ");
        builder.append("var divId = div.id;");
        builder.append("var divClass = div.className;");
        builder.append("if(divId || divClass){");
        builder.append("}else{ var divParent = div.parentNode;");
        builder.append("divId = divParent.id;");
        builder.append("divClass = divParent.className;}");
        //调用HTMLOUT setDivIdClass
        builder.append("window.HTMLOUT.setDivIdClass('");
        builder.append(url);
        builder.append("',divId,divClass);");
        return builder.toString();
    }


    public static String getAIdOrClass(String url,int index) {
        index--;
        StringBuilder builder = new StringBuilder("javascript:");
        builder.append("var div = document.getElementsByTagName('a')[");
        builder.append(index);
        builder.append("]; ");
        builder.append("var divId = div.id;");
        builder.append("var divClass = div.className;");
        builder.append("if(divId || divClass){");
        builder.append("}else{ var divParent = div.parentNode;");
        builder.append("divId = divParent.id;");
        builder.append("divClass = divParent.className;}");
        builder.append("window.HTMLOUT.setDivIdClass('");
        builder.append(url);
        builder.append("',divId,divClass);");
        return builder.toString();
    }

    /**
     * 获取删除div
     * @param id
     * @return
     */
    public static String getClearIdDivJs(String id) {
        StringBuilder js = new StringBuilder("javascript:");
        js.append("var adDiv = document.getElementById('");
        js.append(id);
        js.append("');if(adDiv != null)adDiv.parentNode.removeChild(adDiv);");
        return js.toString();
    }

    public static String getClearIdDivJs(ArrayList<String> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder builder = new StringBuilder("javascript:");
        for (String str : list) {
            builder.append("var adDiv");
            builder.append(str);
            builder.append(" = document.getElementById('");
            builder.append(str);
            builder.append("');if(adDiv");
            builder.append(str);
            builder.append(" != null)adDiv.parentNode.removeChild(adDiv);");
        }
        return builder.toString();
    }
}
