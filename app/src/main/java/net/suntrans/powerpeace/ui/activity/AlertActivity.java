package net.suntrans.powerpeace.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import net.suntrans.looney.login.LoginActivity;
import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.rx.RxBus;

/**
 * Created by Looney on 2017/9/6.
 */

public class AlertActivity extends BasedActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new AlertDialog.Builder(this)
                .setMessage("您的账号已从别处登录")
                .setCancelable(false)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit()
                                .putString("token", "-1")
                                .commit();
                        RxBus.getInstance().post("允许弹框");
                        killAll();
                    }
                })
                .setNegativeButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit()
                                .putString("token", "-1")
                                .commit();
                        RxBus.getInstance().post("允许弹框");
                        Intent intent = new Intent(AlertActivity.this, Login1Activity.class);
                        startActivity(intent);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_fade_out);
                        killAll();
                    }
                })
                .create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!dialog.isShowing())
            dialog.show();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.i("onNewIntent");
    }
}
