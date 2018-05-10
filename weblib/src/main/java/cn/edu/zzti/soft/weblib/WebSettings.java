package cn.edu.zzti.soft.weblib;

import android.webkit.WebView;

/**
 * 设置webSetting的接口
 */

public interface WebSettings <T extends android.webkit.WebSettings>{
    /**
     * 设置webSetting
     * @param webView 设置的对象
     * @return
     */
    WebSettings toSetting(WebView webView);

    T getWebSettings();
}
