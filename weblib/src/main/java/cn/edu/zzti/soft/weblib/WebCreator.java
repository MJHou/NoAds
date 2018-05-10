package cn.edu.zzti.soft.weblib;

import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * web创建器的接口
 */

public interface WebCreator extends ProgressManager {
    WebCreator create();

    WebView get();

    ViewGroup getGroup();
}
