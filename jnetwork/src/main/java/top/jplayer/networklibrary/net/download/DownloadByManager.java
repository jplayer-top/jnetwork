package top.jplayer.networklibrary.net.download;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import top.jplayer.networklibrary.BuildConfig;
import top.jplayer.networklibrary.R;
import top.jplayer.networklibrary.utils.SharePreUtil;

/**
 * Created by Obl on 2018/7/9.
 * top.jplayer.baseprolibrary.net.download
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class DownloadByManager {

    private WeakReference<Activity> weakReference;
    private DownloadManager mDownloadManager;
    private DownloadChangeObserver mDownLoadChangeObserver;
    private DownloadReceiver mDownloadReceiver;
    private long mReqId;
    private OnUpdateListener mUpdateListener;
    private final NotificationClickReceiver mClickReceiver;
    private int newCode;
    private String verDesc;
    private String verUrl;
    private static DownloadByManager dManager;
    private String mApkName;
    private final ProgressReceiver mProgressReceiver;

    public DownloadByManager(Activity activity) {
        weakReference = new WeakReference<>(activity);
        mDownloadManager = (DownloadManager) weakReference.get().getSystemService(Context.DOWNLOAD_SERVICE);
        mDownLoadChangeObserver = new DownloadChangeObserver(new Handler());
        mDownloadReceiver = new DownloadReceiver();
        mClickReceiver = new NotificationClickReceiver();
        mProgressReceiver = new ProgressReceiver();
        mReqId = (long) SharePreUtil.getData(weakReference.get(), "DownloadId", -1L);
    }

    public DownloadByManager bind(int newCode, String verDesc, String verUrl) {
        this.newCode = newCode;
        this.verDesc = verDesc;
        this.verUrl = verUrl;
        return this;
    }


    public DownloadByManager download() {

        Uri uri = Uri.parse(verUrl);
        String title = weakReference.get().getString(R.string.app_name);
        mApkName = title + ":" + newCode + ".apk";
        // fix bug : 装不了新版本，在下载之前应该删除已有文件
        File apkFile = new File(weakReference.get().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), mApkName);
        if (check(apkFile)) return this;
        DownloadManager.Request req = new DownloadManager.Request(uri);
        /*
         * 设置允许使用的网络类型, 可选值:
         *     NETWORK_MOBILE:      移动网络
         *     NETWORK_WIFI:        WIFI网络
         *     NETWORK_BLUETOOTH:   蓝牙网络
         * 默认为所有网络都允许
         */
        // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        /*
         * 设置在通知栏是否显示下载通知(下载进度), 有 3 个值可选:
         *    VISIBILITY_VISIBLE:                   下载过程中可见, 下载完后自动消失 (默认)
         *    VISIBILITY_VISIBLE_NOTIFY_COMPLETED:  下载过程中和下载完成后均可见
         *    VISIBILITY_HIDDEN:                    始终不显示通知
         */
        req.allowScanningByMediaScanner();
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
        req.setDestinationInExternalFilesDir(weakReference.get(), Environment.DIRECTORY_DOWNLOADS, mApkName);
        //如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true
        req.setVisibleInDownloadsUi(true);

        //通知栏标题
        req.setTitle(title);

        //通知栏描述信息
        req.setDescription(verDesc);

        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive");

        mReqId = mDownloadManager.enqueue(req);
        SharePreUtil.saveData(weakReference.get(), "DownloadId", mReqId);
        return this;
    }

    /**
     * 取消下载
     */
    public void cancel() {
        mDownloadManager.remove(mReqId);
    }

    /**
     * 对应 {@link Activity#onResume()}  }
     */
    public void onResume() {
        //设置监听Uri.parse("content://downloads/my_downloads")
        weakReference.get().getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true,
                mDownLoadChangeObserver);
        // 注册广播，监听APK下载
        weakReference.get().registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        weakReference.get().registerReceiver(mClickReceiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
        weakReference.get().registerReceiver(mProgressReceiver, new IntentFilter(weakReference.get().getPackageName() + ".download"));
    }

    /**
     * 对应{@link Activity#onPause()} ()}
     */
    public void onPause() {
        weakReference.get().getContentResolver().unregisterContentObserver(mDownLoadChangeObserver);
        weakReference.get().unregisterReceiver(mDownloadReceiver);
        weakReference.get().unregisterReceiver(mClickReceiver);
        weakReference.get().unregisterReceiver(mProgressReceiver);
    }

    private void updateView() {
        int[] bytesAndStatus = new int[]{0, 0, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mReqId);
        Cursor c = null;
        try {
            c = mDownloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (mUpdateListener != null) {
            mUpdateListener.update(bytesAndStatus[0], bytesAndStatus[1]);
        }
        onProgressListener(bytesAndStatus[0], bytesAndStatus[1]);
    }

    class DownloadChangeObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateView();
        }
    }

    public class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ((context.getPackageName() + ".download").equals(intent.getAction())) {
                {
                    long progress = intent.getLongExtra("progress", 0L);
                    long total = intent.getLongExtra("total", 0L);
                    onProgressListener(progress, total);
                    if (mUpdateListener != null) {
                        mUpdateListener.update((int) progress, (int) total);
                    }
                }
            }
        }
    }

    public void onProgressListener(long progress, long total) {
    }

    ;

    public void listener(OnUpdateListener mUpdateListener) {
        this.mUpdateListener = mUpdateListener;
    }


    public class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            boolean haveInstallPermission;
            // 兼容Android 8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (!haveInstallPermission) {//没有权限
                    Toast.makeText(context, "没有安装外部应用权限", Toast.LENGTH_SHORT).show();
                } else {
                    installApk(context, intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1));
                }
            } else {
                installApk(context, intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1));
            }
        }
    }

    /**
     * 点击通知栏下载项目，下载完成前点击都会进来，下载完成后点击不会进来。
     */
    public class NotificationClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long[] completeIds = intent.getLongArrayExtra(
                    DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //正在下载的任务ID
            if (completeIds == null || completeIds.length <= 0) {
                openDownloadsPage(context);
                return;
            }
            for (long completeId : completeIds) {
                if (completeId == mReqId) {
                    openDownloadsPage(context);
                    break;
                }
            }
        }

        /**
         * Open the Activity which shows a list of all downloads.
         *
         * @param context 上下文
         */
        private void openDownloadsPage(Context context) {
            Intent pageView = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            pageView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(pageView);
        }
    }

    public boolean check(File file) {
        //先检查本地是否已经有需要升级版本的安装包，如有就不需要再下载
        if (file.exists()) {
            PackageManager pm = weakReference.get().getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
            if (info != null) {
                int oldCode = info.versionCode;
                //比较已下载到本地的apk安装包，与服务器上apk安装包的版本号是否一致
                long downloadId = (long) SharePreUtil.getData(weakReference.get(), "DownloadId", -1L);
                if (newCode == oldCode && -1L != downloadId) {
                    Log.e("NetWork", "当前已有新版本");
                    installApk(weakReference.get(), downloadId);
                    return true;
                } else {
                    boolean isDelSuc = file.delete();
                }
            }
        }
        return false;
    }

    /**
     * 安装应用
     */
    public void installApk(Context context, File apk) {
        Uri uri;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        } else {//Android7.0之后获取uri要用contentProvider
            uri = FileProvider.getUriForFile(context, BuildConfig.APPID + ".fileProvider", apk);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 安装应用
     */
    private void installApk(Context context, long reqId) {
        Uri uri;
        Intent intentInstall = new Intent();
        intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentInstall.setAction(Intent.ACTION_VIEW);

        if (reqId == mReqId) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                uri = mDownloadManager.getUriForDownloadedFile(reqId);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // 6.0 - 7.0
                File apkFile = queryDownloadedApk(context, reqId);
                uri = Uri.fromFile(apkFile);
            } else { // Android 7.0 以上
                File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), mApkName);
                uri = FileProvider.getUriForFile(context, BuildConfig.APPID + ".fileProvider", file);
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            // 安装应用
            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intentInstall);
        }
    }

    //通过downLoadId查询下载的apk，解决6.0以后安装的问题
    private File queryDownloadedApk(Context context, long downloadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloader != null && downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    public interface OnUpdateListener {
        void update(int currentByte, int totalByte);
    }
}

