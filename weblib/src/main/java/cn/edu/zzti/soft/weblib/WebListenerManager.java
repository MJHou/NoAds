package cn.edu.zzti.soft.weblib;

import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * web的监听管理器
 */

public interface WebListenerManager {

    WebListenerManager setWebChromeClient(WebView webview, WebChromeClient webChromeClient);
    WebListenerManager setWebViewClient(WebView webView, WebViewClient webViewClient);
    WebListenerManager setDownLoader(WebView webView, DownloadListener downloadListener);
}
