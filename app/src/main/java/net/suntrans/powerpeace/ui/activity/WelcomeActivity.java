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

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;

/**
 * Created by Looney on 2017/8/31.
 */

public class WelcomeActivity extends AppCompatActivity {
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
                        LogUtil.i("拒绝了权限" + permisson+","+position);
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                        LogUtil.i("允许：" + permisson);
                    }
                });
    }

    private void check() {

        String token = App.getSharedPreferences().getString("token", "-1");
        if (token.equals("-1")) {
            handler.sendEmptyMessageDelayed(START_LOGIN, 1500);

        } else {
            int role = App.getSharedPreferences().getInt("role", -1);
            if (role == 0) {
                handler.sendEmptyMessageDelayed(START_MAIN_ADMIN, 1500);
            } else if (role == 1) {
                handler.sendEmptyMessageDelayed(START_MAIN_STU, 1500);

            } else {
                handler.sendEmptyMessageDelayed(START_LOGIN, 1500);

            }

        }


    }


    private static final int START_LOGIN=0;
    private static final int START_MAIN_ADMIN=1;
    private static final int START_MAIN_STU=2;
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


}
