package net.suntrans.powerpeace.ui.activity;

import android.app.ActivityOptions;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.pgyersdk.Pgy;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;


import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.BuildConfig;
import net.suntrans.powerpeace.MyService;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.Version;
import net.suntrans.powerpeace.databinding.ActivitySettingBinding;
import net.suntrans.powerpeace.ui.fragment.DownLoadFrgment;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import java.io.File;
import java.sql.SQLOutput;

import static net.suntrans.powerpeace.R.id.signOut;

public class SettingActivity extends BasedActivity {

    private ActivitySettingBinding binding;
    private String versionName;
    private boolean hasNewVersion;
    private DownloadManager downloadManager;
    private Version.VersionInfo versionInfo;
    private long downloadId;
    private File file;
    private String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle(R.string.title_setting);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        try {
            PgyUpdateManager.unregister();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void init() {
        hasNewVersion = false;
        versionName = BuildConfig.VERSION_NAME;

        PgyUpdateManager.register(this, "net.suntrans.powerpeace.fileProvider", new UpdateManagerListener() {

            @Override
            public void onNoUpdateAvailable() {
                hasNewVersion = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.version.setText(R.string.is_the_lasted_version);
                    }
                });
            }

            @Override
            public void onUpdateAvailable(String s) {
                hasNewVersion = true;
                try {
//                    System.out.println(s);
                    Version version = JSON.parseObject(s, Version.class);


                    versionInfo = version.data;
                    versionName = versionInfo.versionName;
                    result = s;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.version.setText(getString(R.string.find_new_version) + versionName);
                            binding.arrow.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case signOut:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.login_out)
                        .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                            }
                        })
                        .setNegativeButton(R.string.qvxiao, null)
                        .create().show();
                break;
            case R.id.checkVersion:
                if (hasNewVersion) {
                    if (versionInfo == null || versionInfo.downloadURL == null) {
                        break;
                    }

                    showUpdateDialog(result);
//                    startDownLoad();
                } else {
                    UiUtils.showToast(getString(R.string.current_is_new));
                }
                break;
            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.profile:
                startActivity(new Intent(this, PersonActivity.class));
                break;
        }
    }

    private void startDownLoad() {
        String apkName = "hp_" + versionInfo.versionName + "_" + versionInfo.versionCode + ".apk";

        file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
        if (file.exists()) {
            installNormal(this, file.getPath());
            return;
        }
        if (downloadManager == null)
            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //使用DownLoadManager来下载
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionInfo.downloadURL));
        //将文件下载到自己的Download文件夹下,必须是External的

        request.setDestinationUri(Uri.fromFile(file));
        //添加请求 开始下载
        downloadId = downloadManager.enqueue(request);
    }

    private void signOut() {
        App.getSharedPreferences().edit().putString("token", "-1")
                .putString("password","")
                .commit();
        killAll();
        Intent intent = new Intent(this, Login1Activity.class);
        intent.putExtra(
                Login1Activity.EXTRA_TRANSITION, Login1Activity.TRANSITION_SLIDE_BOTTOM);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions transitionActivity = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, transitionActivity.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private Handler handler = new Handler();


    /**
     * 获取进度信息
     *
     * @param downloadId 要获取下载的id
     * @return 进度信息 max-100
     */
    public int getProgress(long downloadId) {
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
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return progress;
    }

    //普通安装
    private static void installNormal(Context context, String apkPath) {
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
        context.startActivity(intent);
    }


    private void showUpdateDialog(String result) {
        String apkName = "hp_" + versionInfo.versionName + "_" + versionInfo.versionCode + ".apk";

        DownLoadFrgment frgment = DownLoadFrgment.newInstance(versionInfo, apkName, result);
        frgment.show(getSupportFragmentManager(), "DownloadFragment");
    }


//    private void checkUpdateStatus(){
//        api
//    }
}

