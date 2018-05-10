package cn.edu.zzti.soft.noads.jsinterface;

import android.webkit.JavascriptInterface;

import cn.edu.zzti.soft.noads.base.BaseWebActivity;
import cn.edu.zzti.soft.noads.utils.Constant;
import cn.edu.zzti.soft.weblib.LogUtils;

/**
 * Android与JS的相互调用
 * 提供给js调用的方法
 */

public class HtmlJsInterface {

    private BaseWebActivity mActivity;

    public HtmlJsInterface(BaseWebActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 此方法是在网页加载完成时调用（在
     * {@link cn.edu.zzti.soft.noads.web.webclient.InterceptionAdWebViewClient onPageFinished()}
     * 方法中追加了获取当前页面的html的js）
     * @param html
     */
    @JavascriptInterface
    @SuppressWarnings("unused")
    public void processHTML(String html) {
        // 在这里处理html源码
        mActivity.setHtmlString(html);
        //如果网页没有加载完成时，用户屏蔽了
        if (!mActivity.getNeedAds().isEmpty()) {
            mActivity.shieldAd(mActivity.getNeedAds().get(0));
        }
    }


    @JavascriptInterface
    @SuppressWarnings("unused")
    public void setDivIdClass(final String url, final String divId, final String divClass) {
        LogUtils.i(Constant.TAG, "url =" + url);
        LogUtils.i(Constant.TAG, "divId =" + divId);
        LogUtils.i(Constant.TAG, "divClass =" + divClass);
        mActivity.addAdRecords(url, divId, divClass);
    }
}
