package cn.edu.zzti.soft.noads.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import cn.edu.zzti.soft.noads.bean.VersionInfo;
import cn.edu.zzti.soft.noads.dialog.DownLoadDialog;

/**
 * 文件
 * Created by houmengjie on 16/10/20.
 */

public class FileUtil {
    private Activity mActivity;
    private static FileUtil fileUtil;


    private static final String SIZE_FORMAT_0 = "%1d kb/%2d kb";
    private static final String SIZE_FORMAT_1 = "%1d kb/%2d kb";
    private static final String SIZE_FORMAT_2 = "%1d kb/%2d kb";
    private static final String SIZE_FORMAT_3 = "%1d kb/%2d kb";
    private static final String SIZE_FORMAT_4 = "%1d kb/%2d kb";
    private static final String SIZE_FORMAT_5 = "%1d kb/%2d kb";
    private static final String SIZE_FORMAT_6 = "%1d kb/%2d kb";


    private FileUtil(Activity activity) {
        mActivity = activity;
    }

    public static FileUtil getInstance(Activity activity) {
        if (fileUtil == null) {
            synchronized (FileUtil.class) {
                if (fileUtil == null) {
                    fileUtil = new FileUtil(activity);
                }
            }
        }
        return fileUtil;
    }

    private int requestCode = (int) SystemClock.uptimeMillis();

    /*
     * 从服务器中下载APK
	 */
    public void downLoadApk(final VersionInfo versionInfo) {
        final DownLoadDialog pd; // 进度条对话框
        pd = new DownLoadDialog(mActivity).builder();
        pd.setMessage("正在下载更新");
        try {
            pd.show();
        } catch (Exception e) {
            return;
        }

        new Thread() {
            @Override
            public void run() {
                File file = null;
                if (getTastInfos()) {
                    try {
                        file = creatSavePath(versionInfo);
                        if (file == null) return;
                        file = getFileFromServer(versionInfo, pd, file);
                        pd.dismiss(); // 结束掉进度条对话框
                        installApk(file);
                    } catch (Exception e) {
                        if (file != null && file.exists()) {
                            file.delete();
                        }
                        if (pd != null) {
                            pd.dismiss();
                        }
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mActivity.startActivity(intent);
    }

    private File creatSavePath(VersionInfo info) {
        if (info == null) return null;
        StringBuilder builder = new StringBuilder();
        builder.append("/");
        builder.append(Constant.TAG);
        builder.append("_");
        builder.append(String.valueOf(info.versionName));
        builder.append("_update.apk");
        File file = new File(Environment.getExternalStorageDirectory(), builder.toString());
        return file;
    }

    /**
     * @param info
     * @param pd
     * @return
     * @throws Exception
     */
    public File getFileFromServer(VersionInfo info, DownLoadDialog pd, File file) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            URL url = new URL(info.path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            Logger.e(Constant.TAG, conn.getContentLength() + ".......");
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProess(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            ToastUtil.showToast(mActivity, "请清理手机").show();
            return null;
        }
    }


    /**
     * 获取activity
     */
//    @TargetApi(21)
    private boolean getTastInfos() {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        Logger.e(Constant.TAG, mActivity.getClass().getName());
        Logger.e(Constant.TAG, info.topActivity.getClassName());
        if (mActivity.getClass().getName().equals(info.topActivity.getClassName())) {
            return true;
        } else {
            return false;
        }
    }

    public static void url2bitmap(final Activity activity, String url) {
        Bitmap bm;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            if (bm != null) {
                save2Album(activity, bm, url);
            }
        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }

    private static void save2Album(final Activity activity, Bitmap bitmap, String url) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "相册名称");
        if (!appDir.exists()) appDir.mkdir();
        String[] str = url.split("/");
        String fileName = str[str.length - 1];
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            onSaveSuccess(activity, file);
        } catch (IOException e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }

    private static void onSaveSuccess(final Activity activity, final File file) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean saveHtml(WebView webView, String name) {
        boolean isSuccess = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "noAd");
            //判断文件夹是否存在，如果不存在就创建，否则不创建
            if (!file.exists()) {
                //通过file的mkdirs()方法创建<span style="color:#FF0000;">目录中包含却不存在</span>的文件夹
                boolean is = file.mkdir();
                Logger.e(Constant.TAG,"is ==" + is);
            }
            file = new File(file, name);
            //通过file的mkdirs()方法创建<span style="color:#FF0000;">目录中包含却不存在</span>的文件夹
            try {
                file.createNewFile();
                webView.saveWebArchive(file.getAbsolutePath());
                isSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return isSuccess;
    }

    public static void deleteHtml(String path){
        File file = new File(path);
        file.delete();
    }
}
