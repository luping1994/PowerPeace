package net.suntrans.powerpeace;

import android.content.ComponentName;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pgyersdk.update.PgyUpdateManager;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.adapter.NavViewAdapter;
import net.suntrans.powerpeace.bean.Version;
import net.suntrans.powerpeace.databinding.ActivityStuMainBinding;
import net.suntrans.powerpeace.network.WebSocketService;
import net.suntrans.powerpeace.rx.RxBus;
import net.suntrans.powerpeace.ui.activity.AboutActivity;
import net.suntrans.powerpeace.ui.activity.BasedActivity;
import net.suntrans.powerpeace.ui.activity.HelpActivity;
import net.suntrans.powerpeace.ui.activity.MsgCenterActivity;
import net.suntrans.powerpeace.ui.activity.PayActivity;
import net.suntrans.powerpeace.ui.activity.PersonActivity;
import net.suntrans.powerpeace.ui.activity.SettingActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.ui.fragment.BasedFragment;
import net.suntrans.powerpeace.ui.fragment.DownLoadFrgment;
import net.suntrans.powerpeace.ui.fragment.SusheDetailFragment;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import org.json.JSONObject;

import rx.Subscriber;

import static net.suntrans.powerpeace.BuildConfig.DEBUG;

/**
 * Created by Looney on 2017/9/15.
 */

public class StudentMainActivity extends BasedActivity implements View.OnClickListener, BasedFragment.OnFragmentInteractionListener {

    private ActivityStuMainBinding binding;
    private WebSocketService.ibinder ibinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ibinder = (WebSocketService.ibinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private String room_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stu_main);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("我的宿舍");

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawer.setDrawerListener(toggle);
        toggle.syncState();
        room_id = App.getSharedPreferences().getString("room_id", "-1");
        SusheDetailFragment fragment = SusheDetailFragment.newInstance(room_id, String.valueOf(Constants.ROLE_ADMIN));
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

        initRecyclerView();

        if (!DEBUG) {
            PgyUpdateManager.register(this, "net.suntrans.powerpeace.fileProvider");
        }

//        , new UpdateManagerListener() {
//
//            @Override
//            public void onNoUpdateAvailable() {
//
//            }
//
//            @Override
//            public void onUpdateAvailable(String s) {
//                try {
//                    Version version = JSON.parseObject(s, Version.class);
//                    showUpdateDialog(s, version.data);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    private void showUpdateDialog(String result, Version.VersionInfo versionInfo) {
        String apkName = "hp_" + versionInfo.versionName + "_" + versionInfo.versionCode + ".apk";

        DownLoadFrgment frgment = DownLoadFrgment.newInstance(versionInfo, apkName, result);
        frgment.show(getSupportFragmentManager(), "DownloadFragment");
    }

    private void initRecyclerView() {

        RxBus.getInstance()
                .toObserverable(String.class)
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
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
                        if (s.equals(WebSocketService.CONNECT_SUCCESS)) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("dev", "4100");
                                jsonObject.put("ac", "gs");
                                jsonObject.put("rd", Integer.valueOf(room_id));
                                sendOrder(jsonObject.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

//        Intent intent = new Intent();
//        intent.setClass(this, WebSocketService.class);
//        bindService(intent, connection, ContextWrapper.BIND_AUTO_CREATE);

        NavViewAdapter navViewAdapter = new NavViewAdapter(NavViewAdapter.getLayoutRes(), NavViewAdapter.getItems());
        navViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                binding.drawer.closeDrawers();

                switch (position) {
                    case 0:
                        break;
                    case 1:
                        handler.sendEmptyMessageDelayed(START_MSG_ACTIVITY, 400);
                        break;
                    case 2:
                        handler.sendEmptyMessageDelayed(START_INTERNET_ACTIVITY, 400);

                        break;
                    case 3:
                        handler.sendEmptyMessageDelayed(START_SETTING_ACTIVITY, 400);

                        break;
                    case 4:
                        handler.sendEmptyMessageDelayed(START_FEEDBACK_ACTIVITY, 400);

                        break;

                    case 5:
                        handler.sendEmptyMessageDelayed(START_ABOUT_ACTIVITY, 400);

                        break;
                    case 6:
                        android.os.Process.killProcess(android.os.Process.myPid());
                        break;
                }
            }
        });
        binding.navView.recyclerView.setAdapter(navViewAdapter);
        binding.navView.recyclerView.addItemDecoration(new DefaultDecoration(UiUtils.dip2px(0.5f, this), getResources().getColor(R.color.nav_divider)));

//        binding.navView.recyclerView.addItemDecoration(new DefaultDecoration(UiUtils.dip2px(0.5f,this), Color.parseColor("#d9d9d9")));
        binding.navView.exit.setOnClickListener(this);
        binding.navView.setting.setOnClickListener(this);
        binding.navView.header.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentMainActivity.this, PersonActivity.class));
            }
        });
        String username = App.getSharedPreferences().getString("username", "--");
        binding.navView.header.username.setText(username);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.setting:
                binding.drawer.closeDrawers();
                handler.sendEmptyMessageDelayed(START_SETTING_ACTIVITY, 400);
                break;
        }
    }


    private long[] mHits = new long[2];

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawers();
                return true;
            }
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
//                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                UiUtils.showToast("再按一次退出");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(connection);
        handler.removeCallbacksAndMessages(null);
//        try {
//            if (!DEBUG)
//                PgyUpdateManager.unregister();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    private static final int START_MSG_ACTIVITY = 0;
    private static final int START_SETTING_ACTIVITY = 1;
    private static final int START_HELP_ACTIVITY = 2;
    private static final int START_INTERNET_ACTIVITY = 3;
    private static final int START_ABOUT_ACTIVITY = 4;
    private static final int START_FEEDBACK_ACTIVITY = 5;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Intent intent = new Intent();

            switch (msg.what) {

                case START_MSG_ACTIVITY:
                    intent.setClass(StudentMainActivity.this, MsgCenterActivity.class);
                    break;
                case START_SETTING_ACTIVITY:
                    intent.setClass(StudentMainActivity.this, SettingActivity.class);
                    break;

                case START_HELP_ACTIVITY:
                    intent.setClass(StudentMainActivity.this, HelpActivity.class);
                    break;

                case START_ABOUT_ACTIVITY:
                    intent.setClass(StudentMainActivity.this, AboutActivity.class);
                    break;
                case START_FEEDBACK_ACTIVITY:
                    intent.setClass(StudentMainActivity.this, HelpActivity.class);
                    break;
                case START_INTERNET_ACTIVITY:
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://www.suntrans.net");
                    intent.setData(content_url);
                    break;

            }
            startActivity(intent);
        }
    };

    @Override
    public void sendOrder(String s) {
        if (ibinder != null)
            ibinder.sendOrder(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pay) {
            startActivity(new Intent(this, PayActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_pay, menu);
//        return true;
//    }

}
