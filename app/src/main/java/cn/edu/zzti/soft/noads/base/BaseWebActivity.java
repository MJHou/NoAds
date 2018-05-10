package cn.edu.zzti.soft.noads.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.zzti.soft.noads.R;
import cn.edu.zzti.soft.noads.Setting;
import cn.edu.zzti.soft.noads.SettingActivity;
import cn.edu.zzti.soft.noads.adapter.SearchRecordsAdapter;
import cn.edu.zzti.soft.noads.bean.MessageEvent;
import cn.edu.zzti.soft.noads.bean.PopBean;
import cn.edu.zzti.soft.noads.dialog.ActionSheetDialog;
import cn.edu.zzti.soft.noads.dialog.IosAlertDialog;
import cn.edu.zzti.soft.noads.handler.WebHandler;
import cn.edu.zzti.soft.noads.jsinterface.HtmlJsInterface;
import cn.edu.zzti.soft.noads.jsinterface.JsCode;
import cn.edu.zzti.soft.noads.listener.OnRecyclerViewItemClickListener;
import cn.edu.zzti.soft.noads.sql.SqlHelper;
import cn.edu.zzti.soft.noads.sql.model.AdData;
import cn.edu.zzti.soft.noads.sql.model.AdPathModel;
import cn.edu.zzti.soft.noads.sql.model.OfflineFileModel;
import cn.edu.zzti.soft.noads.sql.model.SearchRecordsModel;
import cn.edu.zzti.soft.noads.sql.model.TrojanData;
import cn.edu.zzti.soft.noads.utils.Constant;
import cn.edu.zzti.soft.noads.utils.FileUtil;
import cn.edu.zzti.soft.noads.utils.Logger;
import cn.edu.zzti.soft.noads.utils.StringUtil;
import cn.edu.zzti.soft.noads.utils.ToastUtil;
import cn.edu.zzti.soft.noads.web.webclient.InterceptionAdWebViewClient;
import cn.edu.zzti.soft.weblib.AgentWeb;
import cn.edu.zzti.soft.weblib.AgentWebUtils;
import cn.edu.zzti.soft.weblib.ChromeClientCallbackManager;

/**
 * WebView的页面
 */
public class BaseWebActivity extends Activity implements View.OnClickListener {

    //当前页面的html
    private StringBuilder mHtmlString = new StringBuilder();
    //查询历史纪录的适配器
    private SearchRecordsAdapter mSearchRecordsAdapter;
    private AgentWeb mAgentWeb;
    //UI控件
    private TextView mTvTitle;
    private View mIvUrlStatus;
    private View mIvRefresh;
    private View mIvBack;
    private View mIvGo;
    private View mIvHome;
    private View mIvSetting;
    private View mInputLayout;
    private EditText mEtTitle;
    //提示对话框
    private IosAlertDialog mAlertDialog;
    //获取android本地资源文件的对象
    private Resources mResources;
    //异步处理消息{@link Handler}
    private Handler mHandler;
    //屏蔽弹窗广告和过滤木马链接的WebViewClient
    private InterceptionAdWebViewClient mWebViewClient;
    private HtmlJsInterface mHtmlJs;

    /**
     * 设置输入路径
     *
     * @param path
     */
    public void setPath(String path) {
        mEtTitle.setText(path);
        mEtTitle.setSelection(mEtTitle.length());
    }

    /**
     * 异步查询 搜索纪录的监听
     */
    private TransactionListener<List<SearchRecordsModel>> listener = new TransactionListener<List<SearchRecordsModel>>() {
        @Override
        public void onResultReceived(List<SearchRecordsModel> result) {
            mSearchRecordsAdapter.setData((ArrayList<SearchRecordsModel>) result);
        }

        @Override
        public boolean onReady(BaseTransaction<List<SearchRecordsModel>> transaction) {
            return true;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<SearchRecordsModel>> transaction, List<SearchRecordsModel> result) {
            return true;
        }
    };

    /**
     * EventBus的消息
     * 处理从其它页面发过的消息
     * See{@link cn.edu.zzti.soft.noads.adapter.AdInterceptionAdapter}
     * See{@link cn.edu.zzti.soft.noads.adapter.OfflineAdapter}
     *
     * @param event
     */
    @Subscribe
    public void eventBus(MessageEvent event) {
        switch (event.getType()) {
            case Constant.Event.EVENT_DELETE_TROJAN_PATH:
                mWebViewClient.removeTrojan((TrojanData) event.getObj());
                break;
            case Constant.Event.EVENT_DELETE_AD_HOST:
                mWebViewClient.removeAdData(event.getHost());
                break;
            case Constant.Event.EVENT_OPEN_HISTORICAL_RECORDS:
                mAgentWeb.getWebCreator().get().loadUrl(event.getPath());
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus
        mHandler = new WebHandler(this);//初始化WebHandler
        setContentView(R.layout.activity_base_web);//设置加载的布局
        //初始化UI控件，并设置监听
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setOnClickListener(this);
        mIvUrlStatus = findViewById(R.id.iv_url_status);
        mIvRefresh = findViewById(R.id.iv_refresh);
        mIvRefresh.setOnClickListener(this);
        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIvGo = findViewById(R.id.iv_go);
        mIvGo.setOnClickListener(this);
        mIvHome = findViewById(R.id.iv_home);
        mIvHome.setOnClickListener(this);
        mIvSetting = findViewById(R.id.iv_setting);
        mIvSetting.setOnClickListener(this);
        mResources = getResources();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //recyclerView 的布局管理器
        LinearLayoutManager gridLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManger);
        mSearchRecordsAdapter = new SearchRecordsAdapter(this);
        recyclerView.setAdapter(mSearchRecordsAdapter);
        //在recyclerView的item上添加横线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //搜索历史纪录的点击监听
        mSearchRecordsAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<SearchRecordsModel>() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, SearchRecordsModel date, int position) {
                hideKeyboard();
                mEtTitle.setText(date.getPath());
                mTvTitle.setText(date.getPath());
                mInputLayout.setVisibility(View.GONE);
                mAgentWeb.getWebCreator().get().loadUrl(date.getPath());
                date.setDate(new Date());
                date.update();//更新当前纪录的使用时间
            }
        });
        mInputLayout = findViewById(R.id.ll_input);
        mEtTitle = findViewById(R.id.et_title);
        mEtTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //处理软键盘的 回车和搜索事件
                if (keyCode == event.KEYCODE_ENTER || keyCode == event.KEYCODE_SEARCH) {
                    // do some your things
                    hideKeyboard();
                    mInputLayout.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(mEtTitle.getText().toString())) {
                        SearchRecordsModel model = new SearchRecordsModel();
                        model.setTitle(mEtTitle.getText().toString());
                        model.setPath(mEtTitle.getText().toString());
                        model.setDate(new Date());
                        model.save();//保存搜索纪录
                        mAgentWeb.getWebCreator().get().loadUrl(mEtTitle.getText().toString());//加载数据的url
                    }
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.iv_clear).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        LinearLayout linearLayout = findViewById(R.id.ll_content);
        mWebViewClient = new InterceptionAdWebViewClient(this, mHandler);
        mHtmlJs = new HtmlJsInterface(this);
        long p = System.currentTimeMillis();
        //初始化WebView并加载网页（可以说是运用第三方）
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                .defaultProgressBarColor()
                .setReceivedTitleCallback(mCallback)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .addJavascriptInterface("HTMLOUT", mHtmlJs)
                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()
                .go(getUrl());
        long n = System.currentTimeMillis();
        mWebViewClient.setWebView(mAgentWeb.getWebCreator().get());
        //WebView的长按监听
        mAgentWeb.getWebCreator().get().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!(v instanceof WebView)) return false;
                WebView webView = (WebView) v;
                Intent intent;
                String[] strings = null;
                int[] positions = null;
                Uri uri;
                WebView.HitTestResult result = webView.getHitTestResult();
                if (null == result) return false;
                switch (result.getType()) {
                    case WebView.HitTestResult.EDIT_TEXT_TYPE:// 选中的文字类型
                        break;
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        intent = new Intent(Intent.ACTION_DIAL);
                        if (result.getExtra().startsWith("tel:")) {
                            uri = Uri.parse(result.getExtra());
                        } else {
                            uri = Uri.parse("tel:" + result.getExtra());
                        }
                        intent.setData(uri);
                        return true;
                    case WebView.HitTestResult.EMAIL_TYPE://处理Email
                        uri = Uri.parse(result.getExtra());
                        String[] email = {result.getExtra()};
                        intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
                        return true;
                    case WebView.HitTestResult.GEO_TYPE://地图类型
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE:// 超链接
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:// 带有链接的图片类型
                        strings = mResources.getStringArray(R.array.a_strings);
                        positions = mResources.getIntArray(R.array.a_position);
                        break;
                    case WebView.HitTestResult.IMAGE_TYPE:// 处理长按图片的菜单项
                        showSrcDialog(result.getExtra());
                        break;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        Message msg = mHandler.obtainMessage(Constant.Message.MESSAGE_LONG_CLICK);
                        webView.requestFocusNodeHref(msg);
                        return true;
                }
                //根据长按不同的类型，显示对应的对话框
                if (null != strings && strings.length > 0) {
                    ArrayList<PopBean> popBeans = new ArrayList<>();
                    PopBean popBean;
                    for (int i = 0; i < strings.length; i++) {
                        popBean = new PopBean();
                        popBean.text = strings[i];
                        popBean.id = positions[i];
                        popBeans.add(popBean);
                    }
                    showIosDialog(popBeans, result.getExtra());
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 显示长按图片的对话框
     *
     * @param result
     */
    public void showSrcDialog(String result) {
        String[] strings = mResources.getStringArray(R.array.src_strings);
        int[] positions = mResources.getIntArray(R.array.src_position);
        ArrayList<PopBean> popBeans = new ArrayList<>();
        PopBean popBean;
        for (int i = 0; i < strings.length; i++) {
            popBean = new PopBean();
            popBean.text = strings[i];
            popBean.id = positions[i];
            popBeans.add(popBean);
        }
        showIosDialog(popBeans, result);
    }

    /**
     * 显示对话框
     *
     * @param beans  数据集合
     * @param result 操作的字符串（url）
     */
    private void showIosDialog(final ArrayList<PopBean> beans, final String result) {
        //不开启弹窗屏蔽，移除对应屏蔽广告功能
        for (PopBean bean : beans) {
            if (!Setting.IS_START_INTERCEPTION && bean.id == 3) {
                beans.remove(bean);
                break;
            }
        }
        ActionSheetDialog dialog = new ActionSheetDialog(this).builder();
        for (int i = 0; i < beans.size(); i++) {
            dialog.addSheetItem(beans.get(i).text, null, new ActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    int type = beans.get(which - 1).id;
                    switch (type) {
                        case 2:
                            AgentWebUtils.copy(BaseWebActivity.this, result);
                            ToastUtil.showToast(BaseWebActivity.this.getApplicationContext(), "已复制链接,长按输入框可粘贴");
                            break;
                        case 3:
                            shieldAd(result);
                            break;
                        case 4://保存网页
                            saveHtml();
                            break;
                        case 1:
                        case 5:
                            ToastUtil.showToast(BaseWebActivity.this, "暂不支持此功能");
                            break;
                        case 6:
                            //保存图片到相册
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    FileUtil.url2bitmap(BaseWebActivity.this, result);
                                }
                            }).start();
                            break;
                    }
                }
            });
        }
        dialog.show();
    }

    //保存网页
    private void saveHtml() {
        StringBuilder builder = new StringBuilder();
        builder.append(Uri.decode(mTvTitle.getText().toString()));
        builder.append("_");
        builder.append(System.currentTimeMillis());
        builder.append(".mht");
        if (FileUtil.saveHtml(mAgentWeb.getWebCreator().get(), builder.toString())) {
            OfflineFileModel model = new OfflineFileModel();
            model.setDate(System.currentTimeMillis());
            model.setTitle(mTvTitle.getText().toString());
            model.setPath(builder.toString());
            model.save();
            ToastUtil.showToast(this, "保存成功");
        }
    }

    /**
     * 需要屏蔽的广告
     */
    private ArrayList<String> mNeedAds = new ArrayList<>();

    public ArrayList<String> getNeedAds() {
        return mNeedAds;
    }

    //清空列表
    public void clearNeedAds() {
        mNeedAds.clear();
    }


    /**
     * 屏蔽广告
     *
     * @param result
     */
    public void shieldAd(final String result) {
        if (TextUtils.isEmpty(mHtmlString.toString())) {
            ToastUtil.showToast(this, "请稍后,页面未加载完成，当页面加载完成后自动屏蔽该广告");
            mNeedAds.add(result);
        } else if (mHtmlString.toString().indexOf(result) == -1) {
            ToastUtil.showToast(this, "未找到需要屏蔽的广告");
        } else {
            int index = mHtmlString.toString().indexOf(result);
            int divIndex = StringUtil.appearNumber(mHtmlString.toString(), "<div", index);
            final String js;
            if (divIndex > 0) {
                js = JsCode.getDivIdOrClass(result, divIndex);
            } else {
                ToastUtil.showToast(this, "未找到需要屏蔽的div和a标签");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mAgentWeb.getWebCreator().get().loadUrl(js);
                }
            });

        }
    }
    //设置前进的状态
    public void setGoStatus(boolean status) {
        mIvGo.setEnabled(status);
    }

    //设置后退的状态
    public void setBackStatus(boolean status) {
        mIvBack.setEnabled(status);
    }

    //设置主页路径
    public String getUrl() {
        return "https://m.baidu.com";
    }

    //网页加载进度
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Logger.i(Constant.TAG, "progress:" + newProgress);
        }

    };

    /**
     * 网页的head title
     */
    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (TextUtils.equals(mTvTitle.getText().toString(), view.getUrl())) {
                SearchRecordsModel model = new SearchRecordsModel();
                model.setPath(mTvTitle.getText().toString());
                model.setDate(new Date());
                model.setTitle(title);
                model.save();//将搜索纪录的标题保存到数据库
            }
            if (mTvTitle != null)
                mTvTitle.setText(title);
        }

    };

    //当用户点击返回按钮时，如果网页可以回退，则网页回退，否则退出应用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.i(Constant.TAG, "result:" + requestCode + " result:" + resultCode);
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    //隐藏键盘
    private void hideKeyboard() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(mEtTitle.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title://点击标题，显示输入地址布局，并加载搜索历史纪录
                SqlHelper.getAsyncListSearch("", listener);
                mInputLayout.setVisibility(View.VISIBLE);
                showSoftInputFromWindow(this, mEtTitle);
                mEtTitle.setText(mAgentWeb.getWebCreator().get().getUrl());
                mEtTitle.setSelection(mEtTitle.getText().toString().length());
                break;
            case R.id.tv_cancel://隐藏输入地址布局
                hideKeyboard();
                mInputLayout.setVisibility(View.GONE);
                break;
            case R.id.iv_clear://清空地址栏
                mEtTitle.setText("");
                break;
            case R.id.iv_refresh://刷新当前页面
                mAgentWeb.getWebCreator().get().reload();
                break;
            case R.id.iv_back://返回
                if (mAgentWeb.getWebCreator().get().canGoBack()) {
                    mAgentWeb.getWebCreator().get().goBack();
                }
                break;
            case R.id.iv_go://前进
                if (mAgentWeb.getWebCreator().get().canGoForward()) {
                    mAgentWeb.getWebCreator().get().goForward();
                }
                break;
            case R.id.iv_home://回首页
                mAgentWeb.getWebCreator().get().loadUrl(getUrl());
                break;
            case R.id.iv_setting://启动设置页面
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    /**
     * 清除上一个页面的HTML
     */
    public void clearHtmlString() {
        mHtmlString.setLength(0);
    }

    /**
     * 保存当前页面的html
     * @param htmlString
     */
    public void setHtmlString(String htmlString) {
        clearHtmlString();
        this.mHtmlString.append(htmlString);
    }

    /**
     * 屏蔽广告
     * @param divId 根据div的id屏蔽广告
     */
    private void interceptionForId(final String divId) {
        if (TextUtils.isEmpty(divId)) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAgentWeb.getWebCreator().get().loadUrl(JsCode.getClearIdDivJs(divId));
                mAgentWeb.getWebCreator().get().loadUrl(JsCode.getHtmlCore());
            }
        });

    }
    /**
     * 屏蔽广告
     * @param divClass 根据div的class屏蔽广告
     */
    private void interceptionForClass(final String divClass) {
        if (TextUtils.isEmpty(divClass)) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAgentWeb.getWebCreator().get().loadUrl(JsCode.getClearClassDivJs(divClass));
                mAgentWeb.getWebCreator().get().loadUrl(JsCode.getHtmlCore());
            }
        });


    }

    /**
     * 添加屏蔽纪录
     * @param url
     * @param divId
     * @param divClass
     */
    public void addAdRecords(String url, String divId, String divClass) {
        AdPathModel adPathModel = new AdPathModel();
        ArrayList<String> divs = new ArrayList();
        boolean isHaveId = false;
        boolean isHaveClass = false;
        //存在divId根据id删除，如果不存在再根据class屏蔽，否则提示不能屏蔽
        if (!TextUtils.isEmpty(divId)) {
            interceptionForId(divId);
            divs.add(divId);
            isHaveId = true;
            adPathModel.setDivIds(divs);
        } else if (!TextUtils.isEmpty(divClass)) {
            divs.add(divClass);
            isHaveClass = true;
            adPathModel.setDivClasss(divs);
            interceptionForClass(divClass);
        } else {
            ToastUtil.showToast(this, "该广告暂时不能屏蔽");
            return;
        }
        Uri uri = Uri.parse(url);
        if (uri == null && TextUtils.isEmpty(uri.getHost())) return;
        adPathModel.setPath(StringUtil.formatPath(uri.getPath()));
        AdData adData = mWebViewClient.getAdData(uri.getHost());
        if (adData == null) { //未加载到内存
            adData = SqlHelper.getSyncAdData(uri);
        }

        if (adData == null) {//数据库不存在该host的纪录，将该纪录保存在数据库
            adData = new AdData();
            adData.setHost(uri.getHost());
            adData.setSource(1);
            ArrayList<AdPathModel> pathModels = new ArrayList<>();
            pathModels.add(adPathModel);
            adData.setPaths(pathModels);
            adData.save();
            mWebViewClient.setAdData(adData);
        } else {
            boolean isHavePath = false;
            a:
            for (AdPathModel pathModel : adData.getPaths()) {//获取对应的路径对应divId 或divClass
                if (TextUtils.equals(pathModel.getPath(), adPathModel.getPath())) {
                    isHavePath = true;
                    if (isHaveId) {
                        if (null != pathModel.getDivIds() && !pathModel.getDivIds().isEmpty()) {
                            for (String strId : pathModel.getDivIds()) {
                                if (TextUtils.equals(divId, strId)) {
                                    break a;
                                }
                            }
                        }
                        pathModel.addDivId(divId);
                        adData.update();
                    } else if (isHaveClass) {
                        if (null != pathModel.getDivClasss() && !pathModel.getDivClasss().isEmpty()) {
                            for (String strClass : pathModel.getDivClasss()) {
                                if (TextUtils.equals(divClass, strClass)) {
                                    break a;
                                }
                            }
                        }
                        pathModel.addDivClass(divClass);
                        adData.update();
                    }
                    break a;
                }
            }
            if (!isHavePath) {
                adData.addPath(adPathModel);
                adData.setSource(1);
                adData.update();
            }
        }

    }
}
