package cn.edu.zzti.soft.noads.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author houmengjie
 * @date 18/4/27
 */

public class StringUtil {
    /**
     * 格式化路径，
     *
     * @param path
     * @return
     */
    public static final String formatPath(String path) {
        String[] strings = path.split("/");
        StringBuilder builder = new StringBuilder();
        for (String str : strings) {
            if (!TextUtils.isDigitsOnly(str) && str.indexOf(".") == -1) {
                builder.append(str);
                builder.append("/");
            }
        }
        return builder.toString();
    }

    /**
     * 验证是否说文件链接  .js  .css  .png  .jpg
     *
     * @param path
     * @return true 为文件链接，否则不是
     */
    public static final boolean checkPath(String path) {
        if (!TextUtils.isEmpty(path) && path.indexOf(".") != -1) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为URL
     *
     * @param urls 用户头像key
     * @return true:是URL、false:不是URL
     */
    public static boolean isHttpUrl(String urls) {
        String regex = "http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*";
        return match(regex, urls);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * public int indexOf(int ch, int fromIndex)
     * 返回在此字符串中第一次出现指定字符处的索引，从指定的索引开始搜索
     *
     * @param srcText
     * @param findText
     * @return
     */
    public static int appearNumber(String srcText, String findText, int finishIndex) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            if (index >= finishIndex)
                break;
            count++;
        }
        return count;
    }

    /**
     * public int indexOf(int ch, int fromIndex)
     * 返回在此字符串中第一次出现指定字符处的索引，从指定的索引开始搜索
     *
     * @param srcText
     * @param findText
     * @return
     */
    public static int appearNumber(String srcText, String findText, String findText1) {
        int count = 0;
        int index = 0;
        int findIndex;
        int findIndex1;
        do {
            findIndex = srcText.indexOf(findText);
            findIndex1 = srcText.indexOf(findText1);
        } while (findIndex > findIndex1);

        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }


}
