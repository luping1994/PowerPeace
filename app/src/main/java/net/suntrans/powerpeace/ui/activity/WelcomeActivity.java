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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import net.suntrans.looney.login.LoginActivity;
import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.MainActivity;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.StudentMainActivity;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.LoginEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/8/31.
 */

public class WelcomeActivity extends BasedActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            check();
        }
    }


    public void checkPermission() {
        final List<PermissonItem> permissionItems = new ArrayList<PermissonItem>();
        permissionItems.add(new PermissonItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储权限", R.drawable.permission_ic_memory));
        permissionItems.add(new PermissonItem(Manifest.permission.RECORD_AUDIO, "允许录制音频", R.drawable.ic_settings_voice_black_24dp));
        HiPermission.create(this)
                .permissions(permissionItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        new AlertDialog.Builder(WelcomeActivity.this)
                                .setMessage("未获得某些必要的运行权限,部分功能将不可用,是否继续?")
                                .setPositiveButton("进入应用", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        check();
                                    }
                                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create().show();

                    }

                    @Override
                    public void onFinish() {
//                        LogUtil.i("结束了我的儿子");
                        check();
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

    private void check() {

        String token = App.getSharedPreferences().getString("token", "-1");
        String userName = App.getSharedPreferences().getString("username", "-1");
        String password = App.getSharedPreferences().getString("password", "-1");

        if (userName.equals("-1")||token.equals("-1")) {
            handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
        } else {
            int role = App.getSharedPreferences().getInt("role", -1);
            System.out.println(userName+","+role);
            LoginFromServer(userName,password);
//            if (role == 0) {
//                handler.sendEmptyMessageDelayed(START_MAIN_ADMIN, 1500);
//            } else if (role == 1) {
//                String roomid = App.getSharedPreferences().getString("room_id", "-1");
//
//                if (roomid.equals("-1")) {
//                    handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
//
//                } else {
//
//                    handler.sendEmptyMessageDelayed(START_MAIN_STU, 1500);
//                }
//
//            } else {
//                handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
//            }

        }


    }


    private static final int START_LOGIN = 0;
    private static final int START_MAIN_ADMIN = 1;
    private static final int START_MAIN_STU = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_LOGIN:
                    Intent intent = new Intent(WelcomeActivity.this, Login1Activity.class);
                    intent.putExtra(
                            Login1Activity.EXTRA_TRANSITION, Login1Activity.TRANSITION_SLIDE_BOTTOM);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions transitionActivity = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this);
                        startActivity(intent, transitionActivity.toBundle());
                    } else {
                        startActivity(intent);
                    }
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
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    private void LoginFromServer(String username, final String password) {
        RetrofitHelper.getApi().login(username, password)
                .compose(this.<LoginEntity>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LoginEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
                    }

                    @Override
                    public void onNext(LoginEntity result) {
                        if (result.code == 1) {
                            App.getSharedPreferences().edit().putString("token", result.token)
                                    .putInt("role", result.info.role)
                                    .putString("username", result.info.username)
                                    .putString("password", password)
                                    .putString("user_id",result.info.userId)
                                    .commit();
                            if (result.info.role == 0) {
                                handler.sendEmptyMessageDelayed(START_MAIN_ADMIN,1000);
//                                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//                                finish();
                            } else {
                                getUserInfo(result.info.username,result.info.role+"");
                            }
                        } else {
                            handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
                        }
                    }
                });
    }


    private void getUserInfo(String userName,String role) {
        System.out.println("username="+userName+",role="+role);
        RetrofitHelper.getApi()
                .getUserInfo(userName,role)
                .compose(this.<UserInfoEntity>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<UserInfoEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
                    }

                    @Override
                    public void onNext(UserInfoEntity info) {
                        System.out.println(info.toString());
                        if (info.code == 1) {
                            App.getSharedPreferences().edit().putString("room_id", info.info.get(0).room_id + "").commit();
                            handler.sendEmptyMessageDelayed(START_MAIN_STU,1000);

                        } else {
                            handler.sendEmptyMessageDelayed(START_LOGIN, 1500);
                        }
                    }
                });
    }
}
