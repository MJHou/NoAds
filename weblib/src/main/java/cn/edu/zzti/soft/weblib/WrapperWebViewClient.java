package cn.edu.zzti.soft.weblib;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.logging.Logger;

/**
 * 支持WebViewClient
 */
public class WrapperWebViewClient extends WebViewClient {

    private WebViewClient mWebViewClient;

    WrapperWebViewClient(WebViewClient client) {
        this.mWebViewClient = client;
    }

    /**
     * 拦截 url 跳转,在里边添加点击链接跳转或者操作
     */
    @Deprecated
    public boolean shouldOverrideUrlLoading(WebView view, String url) {


        if (mWebViewClient != null) {
            return mWebViewClient.shouldOverrideUrlLoading(view, url);
        }

        return false;
    }

//    /**
//     * 拦截 sdk 24+ 跳转,在里边添加点击链接跳转或者操作
//     */
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//        LogUtils.i("Info", "loading request");
//        if (mWebViewClient != null) {
//            return mWebViewClient.shouldOverrideUrlLoading(view, request);
//        }
//        return super.shouldOverrideUrlLoading(view, request);
//    }

    /**
     * 在开始加载网页时会回调
     */
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        if (mWebViewClient != null) {
            mWebViewClient.onPageStarted(view, url, favicon);
            return;
        }
        super.onPageStarted(view, url, favicon);
    }

    /**
     * 在结束加载网页时会回调
     */
    public void onPageFinished(WebView view, String url) {
        if (mWebViewClient != null) {
            mWebViewClient.onPageFinished(view, url);
            return;
        }
        super.onPageFinished(view, url);
    }

    /**
     * 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
     */
    public void onLoadResource(WebView view, String url) {
        if (mWebViewClient != null) {
            mWebViewClient.onLoadResource(view, url);
            return;
        }
        super.onLoadResource(view, url);
    }


    public void onPageCommitVisible(WebView view, String url) {
        if (mWebViewClient != null) {
            mWebViewClient.onPageCommitVisible(view, url);
            return;
        }
        super.onPageCommitVisible(view, url);
    }

    /**
     * 在每一次请求资源时，都会通过这个函数来回调
     */
    @Deprecated
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (mWebViewClient != null) {
            return mWebViewClient.shouldInterceptRequest(view, url);
        }
        return super.shouldInterceptRequest(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (mWebViewClient != null) {
            return mWebViewClient.shouldInterceptRequest(view, request);
        }
        return shouldInterceptRequest(view, request.getUrl().toString());
    }


    @Deprecated
    public void onTooManyRedirects(WebView view, Message cancelMsg,
                                   Message continueMsg) {
        if (mWebViewClient != null) {
            mWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
            return;
        }
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    /**
     * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
     */
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {

        if (mWebViewClient != null) {
            mWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
            return;
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedError(view, request, error);
            return;
        }

        super.onReceivedError(view, request, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedHttpError(view, request, errorResponse);
            return;
        }
        super.onReceivedHttpError(view, request, errorResponse);
    }

    /**
     * 是否重发POST请求数据，默认不重发。
     */
    public void onFormResubmission(WebView view, Message dontResend,
                                   Message resend) {

        if (mWebViewClient != null) {
            mWebViewClient.onFormResubmission(view, dontResend, resend);
            return;
        }
        super.onFormResubmission(view, dontResend, resend);
    }

    /**
     * 更新访问历史
     */
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {

        if (mWebViewClient != null) {
            mWebViewClient.doUpdateVisitedHistory(view, url, isReload);
            return;
        }
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    /**
     * 当接收到https错误时，会回调此函数，在其中可以做错误处理
     */
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedSslError(view, handler, error);
            return;
        }
        super.onReceivedSslError(view, handler, error);
    }

    /**
     * 通知主程序处理SSL客户端认证请求。如果需要提供密钥，主程序负责显示UI界面。
     * 有三个响应方法：proceed(), cancel() 和 ignore()。
     * 如果调用proceed()和cancel()，webview将会记住response，
     * 对相同的host和port地址不再调用onReceivedClientCertRequest方法。
     * 如果调用ignore()方法，webview则不会记住response。该方法在UI线程中执行，
     * 在回调期间，连接被挂起。默认cancel()，即无客户端认证
     */
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedClientCertRequest(view, request);
            return;
        }
        super.onReceivedClientCertRequest(view, request);
    }


    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
            return;
        }
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    /**
     * 重写此方法才能够处理在浏览器中的按键事件。
     * 是否让主程序同步处理Key Event事件，如过滤菜单快捷键的Key Event事件。
     * 如果返回true，WebView不会处理Key Event，
     * 如果返回false，Key Event总是由WebView处理。默认：false
     */
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (mWebViewClient != null) {
            return mWebViewClient.shouldOverrideKeyEvent(view, event);

        }

        return super.shouldOverrideKeyEvent(view, event);
    }

    /**
     * 通知主程序输入事件不是由WebView调用。是否让主程序处理WebView未处理的Input Event。
     * 除了系统按键，WebView总是消耗掉输入事件或shouldOverrideKeyEvent返回true。
     * 该方法由event 分发异步调用。注意：如果事件为MotionEvent，则事件的生命周期只存在方法调用过程中，
     * 如果WebViewClient想要使用这个Event，则需要复制Event对象。
     */
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {

        if (mWebViewClient != null) {
            mWebViewClient.onUnhandledKeyEvent(view, event);
            return;
        }
        super.onUnhandledKeyEvent(view, event);
    }


    public void onScaleChanged(WebView view, float oldScale, float newScale) {

        if (mWebViewClient != null) {
            mWebViewClient.onScaleChanged(view, oldScale, newScale);
            return;
        }
        super.onScaleChanged(view, oldScale, newScale);
    }

    /**
     * 通知主程序执行了自动登录请求。
     */
    public void onReceivedLoginRequest(WebView view, String realm,
                                       String account, String args) {

        if (mWebViewClient != null) {
            mWebViewClient.onReceivedLoginRequest(view, realm, account, args);
            return;
        }
        super.onReceivedLoginRequest(view, realm, account, args);
    }
}
