package cn.edu.zzti.soft.weblib;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 *
 */

public class WebViewClientCallbackManager {
    private PageLifeCycleCallback mPageLifeCycleCallback;

    public PageLifeCycleCallback getPageLifeCycleCallback() {
        return mPageLifeCycleCallback;
    }

    public void setPageLifeCycleCallback(PageLifeCycleCallback pageLifeCycleCallback) {
        mPageLifeCycleCallback = pageLifeCycleCallback;
    }

    /**
     * 页面生命周期的回调
     */
    public interface PageLifeCycleCallback {
        /**
         * 开始加载页面
         * @param view 加载到的webView
         * @param url 加载的目标url
         * @param favicon
         */
        void onPageStarted(WebView view, String url, Bitmap favicon);

        /**
         * 加载完成
         * @param view 加载到的webView
         * @param url 加载的目标url
         */
        void onPageFinished(WebView view, String url);

    }
}
