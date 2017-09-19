package net.suntrans.powerpeace.ui.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivitySettingBinding;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import static net.suntrans.powerpeace.R.id.signOut;

public class SettingActivity extends BasedActivity {

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("设置");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case signOut:
                new AlertDialog.Builder(this)
                        .setMessage("注销登录?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();
                break;
            case R.id.checkVersion:
                UiUtils.showToast("当前应用已经是最新版本");
                break;
            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.profile:
                startActivity(new Intent(this, PersonActivity.class));
                break;
        }
    }

    private void signOut() {
        App.getSharedPreferences().edit().putString("token","-1").commit();
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
}
