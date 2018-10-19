package net.suntrans.powerpeace.ui.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.BuildingManagerMainActivity;
import net.suntrans.powerpeace.Constants;
import net.suntrans.powerpeace.FloorMainActivity;
import net.suntrans.powerpeace.LeaderMainActivity;
import net.suntrans.powerpeace.MainActivity;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.StudentMainActivity;
import net.suntrans.powerpeace.api.Api;
import net.suntrans.powerpeace.api.ApiException;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.LoginEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.bean.Version;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.fragment.WelcomeDownLoadFrgment;
import net.suntrans.powerpeace.utils.NetworkUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/8/31.
 */

public class WelcomeActivity extends BasedActivity implements WelcomeDownLoadFrgment.UpdateTaskListener {

    private Version version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        checkAppData();

    }


    public void checkPermission() {
        final List<PermissonItem> permissionItems = new ArrayList<PermissonItem>();
        permissionItems.add(new PermissonItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.perssopn_cunchu), R.drawable.permission_ic_memory));
        permissionItems.add(new PermissonItem(Manifest.permission.READ_PHONE_STATE, getString(R.string.permission_phone), R.drawable.ic_phone_per));
        HiPermission.create(this)
                .permissions(permissionItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        new AlertDialog.Builder(WelcomeActivity.this)
                                .setMessage(R.string.tips_perssion)
                                .setPositiveButton(R.string.enter_app, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkNetwork();
                                    }
                                }).setNegativeButton(R.string.title_exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create().show();

                    }

                    @Override
                    public void onFinish() {
//                        LogUtil.i("结束了我的儿子");
                        checkNetwork();
                    }

                    @Override
                    public void onDeny(String permisson, int position) {
                        LogUtil.i("拒绝了权限" + permisson + "," + position);
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                        LogUtil.i("允许：" + permisson);
                    }
                });
    }

    private void checkNetwork() {
//        check();
        check();
//        String[] urls = {"gszydx.suntrans-cloud.com", "gszydx.suntrans-cloud1.com"};
//        Observable.from(urls)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .filter(new Func1<String, Boolean>() {
//                    @Override
//                    public Boolean call(String url) {
//                        return NetworkUtils.ping(url);
//                    }
//                })
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        check();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        System.out.println(s);
//                    }
//                });
    }

    private void check() {

        String token = App.getSharedPreferences().getString("token", "-1");
        String userName = App.getSharedPreferences().getString("username", "-1");
        String password = App.getSharedPreferences().getString("password", "-1");

        if (userName.equals("-1") || token.equals("-1")) {
            handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
        } else {
            long expires_in = App.getSharedPreferences().getLong("expires_in", 0l);
            long loginedTime = App.getSharedPreferences().getLong("currentTimeMillis", 0l);
            long currentTimeMillis = System.currentTimeMillis();

            if (currentTimeMillis - 3600 * 1000 >= (loginedTime + expires_in * 1000)) {    //token过期了
                handler.sendEmptyMessageDelayed(START_LOGIN, 1800);
                return;
            }
            int role = App.getSharedPreferences().getInt("role", -1);
            int what;
            switch (role) {

                case Constants.ROLE_APARTMENT://公寓管理人员
                    what = START_BUILDING_MANAGER_MAIN;
                    break;
                case Constants.ROLE_MANAGER://运维人员

                case Constants.ROLE_ADMIN://管理员
                    what = START_MAIN_ADMIN;

                    break;
                case Constants.ROLE_OFFICER://办公人员
                case Constants.ROLE_SCHOOL_LEADER://校领导
                    what = START_MAIN_LEADER;

                    break;
                case Constants.ROLE_FLOOR://楼栋管理员
                    what = START_MAIN_FLOOR_MANAGER;

                    break;
                case Constants.ROLE_STUDENT://学生
                    what = START_MAIN_STU;

                    break;
                default:
                    what = START_LOGIN;
            }
            handler.sendEmptyMessageDelayed(what, 1800);

        }


    }


    private static final int START_LOGIN = 0;
    private static final int START_MAIN_ADMIN = 1;
    private static final int START_MAIN_STU = 2;
    private static final int START_MAIN_LEADER = 3;
    private static final int START_MAIN_FLOOR_MANAGER = 4;
    private static final int START_BUILDING_MANAGER_MAIN = 5;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_LOGIN:
                    Intent intent = new Intent(WelcomeActivity.this, Login1Activity.class);
                    intent.putExtra(
                            Login1Activity.EXTRA_TRANSITION, Login1Activity.TRANSITION_SLIDE_BOTTOM);
                    startActivity(intent);
                    finish();
                    break;
                case START_MAIN_ADMIN:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case START_MAIN_STU:
                    startActivity(new Intent(WelcomeActivity.this, StudentMainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case START_MAIN_LEADER:
                    startActivity(new Intent(WelcomeActivity.this, LeaderMainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case START_MAIN_FLOOR_MANAGER:
                    startActivity(new Intent(WelcomeActivity.this, FloorMainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case START_BUILDING_MANAGER_MAIN:
                    startActivity(new Intent(WelcomeActivity.this, BuildingManagerMainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
//        try {
//            PgyUpdateManager.unregister();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private int loginCount = 0;

    private Api api = RetrofitHelper.getLoginApi();

    private void LoginFromServer(final String username, final String password) {
        api.login(username, password, "password", "100001", "peS4zinqLC2x5pSc2Li98whTbSaC0d1OwrYsqQpL")
                .compose(this.<LoginEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LoginEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                            UiUtils.showToast("网络出错!");
                        } else if (e instanceof IOException) {
                            UiUtils.showToast(getResources().getString(R.string.tips_net_work_is_unused));
                        } else {
                            super.onError(e);
                        }
                        handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
                    }

                    @Override
                    public void onNext(LoginEntity result) {
                        if (result.getAccess_token() != null) {
                            App.getSharedPreferences().edit().putString("token", result.getAccess_token())
                                    .apply();
                            getUserInfo();
                        } else {
                            handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
                            UiUtils.showToast(getResources().getString(R.string.username_password_is_error));
                        }

                    }
                });
    }


    private void getUserInfo() {
        mCompositeSubscription.add(RetrofitHelper.getApi().getUserInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserInfoEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
                    }

                    @Override
                    public void onNext(UserInfoEntity info) {
                        LogUtil.i(info.toString());
                        if (info.code == 1) {

                            App.getSharedPreferences().edit().putString("room_id", info.info.room_id + "")
                                    .putString("studentID", info.info.id)
                                    .putString("username", info.info.username)
                                    .putInt("role", info.info.role_id)
                                    .putString("floor_id", info.info.floor_id)
                                    .commit();
                            switch (info.info.role_id) {
                                case Constants.ROLE_APARTMENT://公寓管理人员
                                    handler.sendEmptyMessage(START_BUILDING_MANAGER_MAIN);
                                    break;
                                case Constants.ROLE_MANAGER://运维人员
//                                    UiUtils.showToast("暂不支持" + info.info.role_name + "登录本系统");
//                                    break;
                                case Constants.ROLE_ADMIN://管理员
                                    handler.sendEmptyMessage(START_MAIN_ADMIN);
                                    break;
                                case Constants.ROLE_OFFICER://办公人员
                                case Constants.ROLE_SCHOOL_LEADER://校领导
                                    handler.sendEmptyMessage(START_MAIN_LEADER);
                                    break;
                                case Constants.ROLE_FLOOR://楼栋管理员
                                    handler.sendEmptyMessage(START_MAIN_FLOOR_MANAGER);
                                    break;
                                case Constants.ROLE_STUDENT://学生
                                    handler.sendEmptyMessage(START_MAIN_STU);

                                    break;
                                default:
                                    handler.sendEmptyMessage(START_LOGIN);

                            }

                        } else {
                            handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
                        }
                    }
                }));
    }

//    private void checkUpdate() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkAppData();
//                try {
//                    PgyUpdateManager.unregister();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 2000);
//        PgyUpdateManager.register(this, "net.suntrans.powerpeace.fileProvider", new UpdateManagerListener() {
//            @Override
//            public void onNoUpdateAvailable() {
//                handler.removeCallbacksAndMessages(null);
//                checkAppData();
//            }
//
//            @Override
//            public void onUpdateAvailable(String s) {
//                handler.removeCallbacksAndMessages(null);
//                version = JSON.parseObject(s, Version.class);
//                showUpdateDialog(s);
//            }
//        });
//    }

    private void checkAppData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            checkNetwork();
        }
    }

    private void showUpdateDialog(String result) {
        String apkName = "hp_" + version.data.versionName + "_" + version.data.versionCode + ".apk";

        WelcomeDownLoadFrgment frgment = WelcomeDownLoadFrgment.newInstance(version.data, apkName, result);
        frgment.setListener(this);
        frgment.show(getSupportFragmentManager(), "DownloadFragment");
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onCancle() {
        checkAppData();
    }
}
