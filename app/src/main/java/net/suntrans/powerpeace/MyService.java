package net.suntrans.powerpeace;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.pgyersdk.update.UpdateManagerListener;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.bean.Version;
import net.suntrans.powerpeace.rx.RxBus;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static net.suntrans.looney.utils.UiUtils.getContext;

/**
 * Created by Looney on 2017/9/7.
 */

public class MyService extends Service {
    private boolean isShowAlert = true;

    private Version.VersionInfo info;
    private String apkName;
    private String result;


    private DownloadManager downloadManager;
    private long downloadId = -1l;
    private File file;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        initDownLoadInfo(intent);
        return new MyBinder();
    }

    private void initDownLoadInfo(Intent intent) {
        info = intent.getParcelableExtra("info");
        apkName = intent.getStringExtra("apkName");
        result = intent.getStringExtra("result");

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    class MyBinder extends Binder {
        public void startDownload() {
            String apkName = "hp_" + info.versionName + "_" + info.versionCode + ".apk";

            file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
            if (file.exists()) {
                installNormal(getContext(), file.getPath());
                return;
            }
            if (downloadManager == null)
                downloadManager = (DownloadManager) getContext().getSystemService(ContextWrapper.DOWNLOAD_SERVICE);
            //使用DownLoadManager来下载
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.downloadURL));
            //将文件下载到自己的Download文件夹下,必须是External的

            request.setDestinationUri(Uri.fromFile(file));
            //添加请求 开始下载
            downloadId = downloadManager.enqueue(request);
            registerContentObserver();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxBus.getInstance().toObserverable(Intent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Intent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Intent intent) {
                        if (isShowAlert) {
                            isShowAlert = false;
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra(
//                                    "notTrans",true);
                            startActivity(intent);

                        }
                    }
                });
        RxBus.getInstance().toObserverable(String.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("允许弹框")) {
                            isShowAlert = true;
                        }
                    }
                });
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            System.out.println("onReceive");
            if (completeDownloadId == downloadId) {
                checkStatus();
            }
        }
    };

    //检查下载状态
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
//                    System.out.println("zhengzaixiazai");
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    UpdateManagerListener.updateLocalBuildNumber(result);
                    installNormal(getApplicationContext(), file.getPath());
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    UiUtils.showToast("下载失败");
                    break;
            }
        }
        c.close();
    }


    class DownLoadChangedObserver extends ContentObserver {

        private final ScheduledExecutorService scheduledExecutorService;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownLoadChangedObserver(Handler handler) {
            super(handler);
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            float[] progress = getProgress(downloadId);
//            System.out.println("onChanged");
//            handler.sendMessage(Message.obtain(handler, 1, progress));
//            scheduledExecutorService.scheduleAtFixedRate(progressRunnable,0,2, TimeUnit.SECONDS);
        }

    }

    private DownLoadChangedObserver downloadObserver;
    private Handler handler = new Handler();

    /**
     * 注册ContentObserver
     */
    private void registerContentObserver() {
        /** observer download change **/
        if (downloadObserver == null) {
            downloadObserver = new DownLoadChangedObserver(handler);
        }
        Uri uri = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getContext(), "net.suntrans.powerpeace.fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        getContext().getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, downloadObserver);
    }

    /**
     * 获取进度信息
     *
     * @param downloadId 要获取下载的id
     * @return 进度信息 max-100
     */
    public float[] getProgress(long downloadId) {
        float[] result = new float[2];
        //查询进度
        DownloadManager.Query query = new DownloadManager.Query()
                .setFilterById(downloadId);
        Cursor cursor = null;
        int progress = 0;
        try {
            cursor = downloadManager.query(query);//获得游标
            if (cursor != null && cursor.moveToFirst()) {
                //当前的下载量
                int downloadSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //文件总大小
                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                progress = (int) (downloadSoFar * 1.0f / totalBytes * 100);
                result[0] = totalBytes;
                result[1] = progress;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }


    //普通安装
    private void installNormal(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = (new File(apkPath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "net.suntrans.powerpeace.fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                    "application/vnd.android.package-archive");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    @Override
    public void unbindService(ServiceConnection conn) {
        handler.removeCallbacksAndMessages(null);
        if (downloadObserver != null)
            getContentResolver().unregisterContentObserver(downloadObserver);
        unregisterReceiver(receiver);
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
