package cn.edu.zzti.soft.weblib;

/**
 * 进度条
 */

public interface ProgressSpec {

    /**
     *  重置进度条
     */
    void reset();

    /**
     * 设置进度
     * @param newProgress 进度
     */
    void setProgress(int newProgress);


}
