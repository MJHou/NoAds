package cn.edu.zzti.soft.weblib;

/**
 * 进度条的基础方法
 */

public interface BaseProgressSpec extends ProgressSpec {
    /**
     * 显示进度条
     */
    void show();

    /**
     * 隐藏进度条
     */
    void hide();
}
