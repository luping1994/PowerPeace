package net.suntrans.powerpeace.ui.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;

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
        HiPermission.create(this)
                .checkSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {
                        LogUtil.i("权限授予完成");
                    }

                    @Override
                    public void onDeny(String permisson, int position) {
                        UiUtils.showToast("您已经拒绝授予存储权限,应用无法运行");
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                        LogUtil.i("权限通过");
                        check();
                    }
                });
    }

    private void check() {
        boolean isSignOut = App.getSharedPreferences().getBoolean("isSignOut", true);

        String token = App.getSharedPreferences().getString("token", "-1");
        if (token.equals("-1")) {
            handler.sendEmptyMessageDelayed(0, 1500);

        } else {
            int role = App.getSharedPreferences().getInt("role", -1);
            if (role == 0) {
                handler.sendEmptyMessageDelayed(1, 1500);
            } else if (role == 1) {
                handler.sendEmptyMessageDelayed(2, 1500);

            } else {
                handler.sendEmptyMessageDelayed(0, 1500);

            }

        }


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
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
                case 1:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case 2:
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
