package cn.edu.zzti.soft.weblib;

/**
 * 季度管理器
 */

public interface ProgressManager<T extends BaseProgressSpec> {


    T offer();
}
