package cn.edu.zzti.soft.noads.web.webclient;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.edu.zzti.soft.noads.Setting;
import cn.edu.zzti.soft.noads.base.BaseWebActivity;
import cn.edu.zzti.soft.noads.dialog.IosAlertDialog;
import cn.edu.zzti.soft.noads.jsinterface.JsCode;
import cn.edu.zzti.soft.noads.sql.SqlHelper;
import cn.edu.zzti.soft.noads.sql.model.AdData;
import cn.edu.zzti.soft.noads.sql.model.AdPathModel;
import cn.edu.zzti.soft.noads.sql.model.HistoricalRecordModel;
import cn.edu.zzti.soft.noads.sql.model.TrojanData;
import cn.edu.zzti.soft.noads.utils.StringUtil;
import cn.edu.zzti.soft.noads.utils.ToastUtil;

/**
 * 自定义WebViewClient,在此进行广告的屏蔽，木马的拦截，以及追加或html源码的js
 */

public class InterceptionAdWebViewClient extends WebViewClient {
    //缓存中木马拦截的集合
    private ArrayList<TrojanData> mTrojanDataList = new ArrayList<>();
    //缓存中广告屏蔽的集合
    private HashMap<String, AdData> mAdDataMap = new HashMap<>();

    /**
     * 根据host获取缓存中广告屏蔽的对象
     * @param host
     * @return
     */
    public AdData getAdData(String host) {
        if (TextUtils.isEmpty(host)) return null;
        return mAdDataMap.get(host);
    }

    /**
     * 根据host获取缓存中广告屏蔽的对象
     * @param host
     * @return
     */
    public AdData removeAdData(String host) {
        if (TextUtils.isEmpty(host)) return null;
        return mAdDataMap.remove(host);
    }
    /**
     * 根据host获取缓存中广告屏蔽的对象
     * @param host
     * @return
     */
    public boolean removeTrojan(TrojanData host) {
        return mTrojanDataList.remove(host);
    }
    /**
     * 将广告屏蔽的对象添加到缓存中
     * @param adData
     * @return
     */
    public void setAdData(AdData adData) {
        if (null == adData || TextUtils.isEmpty(adData.getHost())) return;
        mAdDataMap.put(adData.getHost(), adData);
    }

    //异步查询过历史纪录的集合
    private ArrayList<String> historyList = new ArrayList<>();
    private Handler mHandler;

    //设置webView
    public void setWebView(WebView mWebView) {
        this.mWebView = mWebView;
    }

    private WebView mWebView;
    private HashMap<String, String> mWaitMap = new HashMap<>();


    private AdData mAdData;
    //异步查询监听
    private TransactionListener<AdData> mTransactionListener = new TransactionListener<AdData>() {
        @Override
        public void onResultReceived(AdData result) {
            if (null == result) {
                mWaitMap.clear();
                return;
            }
            if (!mAdDataMap.containsKey(result.getHost())) {
                mAdDataMap.put(result.getHost(), result);
            }
            String path = mWaitMap.remove(result.getHost());
            if (!TextUtils.isEmpty(path)) {
                for (final AdPathModel model : result.getPaths()) {
                    if (TextUtils.equals(model.getPath(), path)) {
                        final String jsCode = JsCode.getAdPathModelJs(model);
                        if (!TextUtils.isEmpty(jsCode) && null != mWebView) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mWebView.loadUrl(jsCode);
                                }
                            });
                        }
                    }
                }
            }
        }

        @Override
        public boolean onReady(BaseTransaction<AdData> transaction) {
            return true;
        }

        @Override
        public boolean hasResult(BaseTransaction<AdData> transaction, AdData result) {
            return true;
        }
    };
    public BaseWebActivity mActivity;

    public InterceptionAdWebViewClient(BaseWebActivity activity, Handler handler) {
        this.mHandler = handler;
        mActivity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TrojanData> list = SqlHelper.getSyncListTrojan();
                mTrojanDataList.addAll(list);
            }
        }).start();
        //将集合对象变成安全的
        Collections.synchronizedMap(mAdDataMap);
        Collections.synchronizedMap(mWaitMap);
        Collections.synchronizedList(historyList);
    }

    /**
     * 该方法返回 true ,则说明由应用的代码处理该 url,WebView 不处理,也就是程序员自己做处理。
     * 该方法返回 false,则说明由 WebView 处理该 url,即用 WebView 加载该 url。
     *
     * @param view
     * @param url
     * @return
     */
    //在此拦截 木马网页
    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        if (Setting.IS_START_TROJAB_INTERCEPTION) {
            return interceptionTrojan(view, url);
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    /**
     * 屏蔽病毒广告
     * @param view
     * @param url
     * @return
     */
    private boolean interceptionTrojan(final WebView view, final String url) {
        TrojanData trojanData = null;
        //从缓存中寻找当前url是否屏蔽
        for (TrojanData data : mTrojanDataList) {
            if (TextUtils.equals(data.getPath(), url)) {
                trojanData = data;
                break;
            }
        }
        //如果不再缓存中，同步查找木马拦截表
        if (null == trojanData) {
            trojanData = SqlHelper.getSyncTrojanData(url);
        }
        if (null != trojanData) {//存在，表示拦截广告
            //根据权限不同设置不同的提示方案
            switch (trojanData.getType()) {
                case 1://直接拦截
                    ToastUtil.showToast(mActivity, "此链接木马病毒,系统自动拦截");
                    return true;
                case 2://提示不良网站
                    if (!trojanData.isCarriedOut) {
                        final TrojanData data = trojanData;
                        new IosAlertDialog(mActivity)
                                .builder()
                                .setTitle("温馨提示")
                                .setMsg("此链接又可能是不良网站，是否继续浏览")
                                .setNegativeButton("离开", null)
                                .setPositiveButton("继续浏览", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        data.isCarriedOut = true;
                                        view.loadUrl(url);
                                    }
                                })
                                .show();
                        return true;
                    } else {
                        return false;
                    }
                default://不做任何处理
                    break;

            }
        }
        return false;
    }

    //打开网页
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        //开始加载新的页面，清除其缓存
        mActivity.clearNeedAds();
        mActivity.clearHtmlString();
        if (mWebView == null) mWebView = view;
    }

    //网页打开完成
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //设置前进后退状态
        mActivity.setGoStatus(view.canGoForward());
        mActivity.setBackStatus(view.canGoBack());
        //向网页追加获取html源码的js
        view.loadUrl(JsCode.getHtmlCore());
        //保存浏览历史纪录
        HistoricalRecordModel model = new HistoricalRecordModel();
        model.setDate(System.currentTimeMillis());
        model.setPath(url);
        model.setTitle(view.getTitle());
        model.save();
    }


    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
    }

    /**
     * 在每一次请求资源时，都会通过这个函数来回调
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse webResourceResponse = interceptionPopAd(view, url);
        if (webResourceResponse == null) {
            return super.shouldInterceptRequest(view, url);
        }
        return webResourceResponse;
    }

    /**
     * 拦截弹窗广告
     *
     * @param view
     * @param url
     * @return
     */
    private WebResourceResponse interceptionPopAd(@NonNull final WebView view, String url) {
        if (!Setting.IS_START_INTERCEPTION || TextUtils.isEmpty(url)) return null;//判断是否启用广告拦截
        Uri uri = Uri.parse(url);
        if (null == uri) return null;
        boolean isFile = StringUtil.checkPath(uri.getPath());
        if (uri != null) {//从缓存查找是否存在屏蔽的对象
            mAdData = mAdDataMap.get(uri.getHost());
        }
        if (mAdData == null) {
            //如果以前没用查询过，则异步去本地数据库查找
            if (!historyList.contains(uri.getHost())) {
                SqlHelper.getAsyncAdData(url, mTransactionListener);
                if (mWaitMap.containsKey(uri.getHost())) {
                    mWaitMap.remove(uri.getHost());
                }
                historyList.add(uri.getHost());
                mWaitMap.put(uri.getHost(), StringUtil.formatPath(uri.getPath()));
            }
        } else if (mAdData.getPaths() == null || mAdData.getPaths().isEmpty()) {//数据库不拦截此文件
            return null;
        } else {
            String path = StringUtil.formatPath(uri.getPath());
            for (final AdPathModel model : mAdData.getPaths()) {
                if (TextUtils.equals(model.getPath(), path)) {
                    final String jsCode = JsCode.getAdPathModelJs(model);
                    if (!TextUtils.isEmpty(jsCode)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                view.loadUrl(jsCode);
                            }
                        });
                    }
                    if (isFile) {
                        return new WebResourceResponse(null, null, null);//拦截
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // 通知宿主程序存在大量的HTTP重定向。宿主程序如果想继续加载资源的话可以选择发送继续的msg，默认的是发送取消的
    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    //加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    /**
     * 通知宿主程序在加载一个资源的时候从服务器接收到了一个HTTP的错误。这个错误的错误码会>=400.这个回调在任何资
     * 源加载出错的时候都会回调，而不仅限于主page。因此，建议在本方法中执行最低要求（？）的工作。
     * 请注意，服务器响应内容不能在errorResponse参数中设置。
     *
     * @param view
     * @param request
     * @param errorResponse
     */
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
    }

    /**
     * 如果浏览器需要重新发送POST请求，可以通过这个时机来处理。默认是不重新发送数据
     *
     * @param view
     * @param dontResend
     * @param resend
     */
    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        super.onFormResubmission(view, dontResend, resend);
    }

    /**
     * 通知主机应用程序更新其访问的链接数据库。
     *
     * @param view
     * @param url
     * @param isReload
     */
    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    /**
     * 通知宿主程序加载资源的时候发生了一个SSL错误。宿主程序必须调用handler.cancel()或者handler.proceed()。
     * 注意，你的选择可能会被保存，用于处理接下来的SSL错误。默认的行为是取消加载-handler.cancel()
     *
     * @param view
     * @param handler
     * @param error
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        super.onReceivedClientCertRequest(view, request);
    }

    /**
     * 通知宿主程序接收到了一个HTTP认证的请求。宿主程序可以使用参数中的HttpAuthHandler来处理这个请求。
     * 默认的行为是取消请求。
     *
     * @param view
     * @param handler
     * @param host
     * @param realm
     */
    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    /**
     * 重写此方法才能够处理在浏览器中的按键事件。
     * 是否让主程序同步处理Key Event事件，如过滤菜单快捷键的Key Event事件。
     * 如果返回true，WebView不会处理Key Event，
     * 如果返回false，Key Event总是由WebView处理。默认：false
     */
    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }

    /**
     * 通知宿主程序有按键事件没有被webview处理。除非是系统的按键事件，webview会消费正常流程的按键事件和
     * shouldOverrideKeyEvent方法返回true的按键事件。在事件被分发的时候这个方法会被异步调用。这给了宿主程序一
     * 个机会去处理那些没有处理的按键事件。
     *
     * @param view
     * @param event
     */
    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        super.onUnhandledKeyEvent(view, event);
    }

    /**
     * 通知宿主程序WebView的缩放比例发生了改变。
     *
     * @param view
     * @param oldScale
     * @param newScale
     */
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    /**
     * 通知宿主程序自动登录用户的请求已被处理.
     *
     * @param view
     * @param realm
     * @param account
     * @param args
     */
    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        return super.onRenderProcessGone(view, detail);
    }
}
