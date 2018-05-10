package cn.edu.zzti.soft.weblib;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.List;

/**
 * WebView的配置
 */

public class AgentWebConfig {


    static final String AGENTWEB_CACHE_PATCH="/agentweb_cache";

    static final String DOWNLOAD_PATH="download";

    //低于SDK19
    static final boolean isKikatOrBelowKikat= Build.VERSION.SDK_INT<=Build.VERSION_CODES.KITKAT;
//    static final boolean isKikatOrBelowKikat= true;


    //WebView默认类型
    public static final int WEBVIEW_DEFAULT_TYPE=1;
    //AgentWeb的安全类型
    public static final int WEBVIEW_AGENTWEB_SAFE_TYPE =2;
    //自定义WebView
    public static final int WEBVIEW_CUSTOM_TYPE=3;

    static  int WEBVIEW_TYPE =WEBVIEW_DEFAULT_TYPE;


    /**
     * cookie同步
     */
    public static void syncCookieToWebView(Context context, List<String> cookies, String url) {

        if (CookieSyncManager.getInstance() == null)
            CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
        cm.setAcceptCookie(true);
        if (cookies != null) {
            for (String cookie : cookies) {
                cm.setCookie(url, cookie);
            }
        }
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 获取webView的缓存路径
     * @param context
     * @return
     */
    public static String getCachePath(Context context){
        return context.getCacheDir().getAbsolutePath()+AGENTWEB_CACHE_PATCH;
    }

    /**
     * 获取数据库的缓存路径
     * @param context
     * @return
     */
    public static String getDatabasesCachePath(Context context){
        return context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
    }

    /**
     * 移除所有cookies
     * @param context
     */
    public static void removeAllCookies(Context context){
        if (CookieSyncManager.getInstance() == null)
            CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
    }

    /**
     * 同步cookies
     * @param url 需要同步的路径
     * @param cookies 同步的cookies
     */
    public static void syncCookieToWebView(String url, String cookies) {
        CookieManager mCookieManager=CookieManager.getInstance();
        mCookieManager.setCookie(url,cookies);
    }


}
