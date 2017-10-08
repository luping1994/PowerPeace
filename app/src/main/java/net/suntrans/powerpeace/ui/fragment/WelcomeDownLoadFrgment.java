package net.suntrans.powerpeace.ui.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.pgyersdk.update.UpdateManagerListener;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.NumberProgressBar;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.Version;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Looney on 2017/9/28.
 */

public class WelcomeDownLoadFrgment extends DialogFragment {

    private Version.VersionInfo info;
    private String apkName;


    private DownloadManager downloadManager;
    private long downloadId=-1l;
    private File file;

    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private NumberProgressBar mNumberProgressBar;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    private DownLoadChangedObserver downloadObserver;
    private String result;
    private TextView totalSize;


    public static WelcomeDownLoadFrgment newInstance(Version.VersionInfo info, String apkName, String result) {
        WelcomeDownLoadFrgment frgment = new WelcomeDownLoadFrgment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("info", info);
        bundle.putString("apkName", apkName);
        bundle.putString("result", result);
        frgment.setArguments(bundle);
        return frgment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = getArguments().getParcelable("info");
        apkName = getArguments().getString("apkName");
        result = getArguments().getString("result");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);

    }

    @Override
    public void onStart() {
        super.onStart();
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(false);
        //是否可以取消,会影响上面那条属性
//        setCancelable(false);
//        //window外可以点击,不拦截窗口外的事件
//        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.8f);
        dialogWindow.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_update, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();

    }

    private LinearLayout mLlClose;
    //默认色
    private int mDefaultColor = 0xffe94339;
    private int mDefaultPicResId = R.drawable.lib_update_app_top_bg;
    private ImageView mTopIv;
    private TextView mIgnore;

    private void initView(View view) {
        //提示内容
        mContentTextView = (TextView) view.findViewById(R.id.tv_update_info);
        //标题
        mTitleTextView = (TextView) view.findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = (Button) view.findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = (NumberProgressBar) view.findViewById(R.id.npb);
        //关闭按钮
        mIvClose = (ImageView) view.findViewById(R.id.iv_close);
        //关闭按钮+线 的整个布局
        mLlClose = (LinearLayout) view.findViewById(R.id.ll_close);
        //顶部图片
        mTopIv = (ImageView) view.findViewById(R.id.iv_top);
        //忽略
        mIgnore = (TextView) view.findViewById(R.id.tv_ignore);

        totalSize = (TextView) view.findViewById(R.id.size);
        mUpdateOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownLoad();
                mUpdateOkButton.setVisibility(View.GONE);
                mNumberProgressBar.setVisibility(View.VISIBLE);
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener!=null){
                    listener.onCancle();

//                    downloadManager.remove(downloadId);
                }
            }
        });

    }

    public void setListener(UpdateTaskListener listener) {
        this.listener = listener;
    }

    private UpdateTaskListener listener;
    public interface UpdateTaskListener{
        void onFinish();
        void onCancle();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initData() {
        mTitleTextView.setText("发现新版本" + info.versionName + "");
        mContentTextView.setText(info.releaseNote);
        getActivity().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void startDownLoad() {
        mIvClose.setVisibility(View.INVISIBLE);
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


    //普通安装
    private  void installNormal(Context context, String apkPath) {
        updateView();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
        context.startActivity(intent);

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
                result[0]=totalBytes;
                result[1]=progress;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            LogUtil.i("onReceive");
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
                    installNormal(getActivity(), file.getPath());
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    UiUtils.showToast("下载失败");
                    break;
            }
        }
        c.close();
    }

    private void updateView() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //        dismiss();
                mUpdateOkButton.setVisibility(View.VISIBLE);
                mNumberProgressBar.setVisibility(View.GONE);
                mNumberProgressBar.setProgress(0);
                mIvClose.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
        handler.removeCallbacksAndMessages(null);
        if (downloadObserver != null)
            getContext().getContentResolver().unregisterContentObserver(downloadObserver);
//        if (downloadId!=-1)
//        downloadManager.remove(downloadId);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                System.out.println(msg.arg1);
                float[] obj = (float[]) msg.obj;
                mNumberProgressBar.setProgress((int) obj[1]);
                float totalSizef = obj[0]/1024/1024;
                DecimalFormat decimalFormat=new DecimalFormat(".00");
                String format = decimalFormat.format(totalSizef);
                String total = format+"MB";
                totalSize.setText(total);
            }
        }
    };

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
            handler.sendMessage(Message.obtain(handler, 1, progress));
//            scheduledExecutorService.scheduleAtFixedRate(progressRunnable,0,2, TimeUnit.SECONDS);
        }

    }


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

}
