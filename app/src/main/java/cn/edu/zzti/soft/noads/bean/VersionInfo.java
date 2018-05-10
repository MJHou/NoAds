package cn.edu.zzti.soft.noads.bean;

/**
 * 版本信息
 */

public class VersionInfo {
    public String path;//Apk 下载路径
    public int force;//是否强制更新  0 不强制  1 强制
    public String content;//更新内容
    public int versionName;//服务端的apk 版本号
    public int update;//0 不提示  1 提示更新   2  强制更新
}
