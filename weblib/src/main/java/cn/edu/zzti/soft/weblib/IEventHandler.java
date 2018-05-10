package cn.edu.zzti.soft.weblib;

import android.view.KeyEvent;

/**
 * 事件处理程序
 */
public interface IEventHandler {
    /**
     * 按下
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyDown(int keyCode, KeyEvent event);

    /**
     * 返回
     * @return
     */
    boolean back();
}
