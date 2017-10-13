package net.suntrans.powerpeace;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pgyersdk.update.PgyUpdateManager;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.adapter.FragmentAdapter;
import net.suntrans.powerpeace.adapter.NavViewAdapter;
import net.suntrans.powerpeace.bean.Version;
import net.suntrans.powerpeace.databinding.ActivityFloorMainBinding;
import net.suntrans.powerpeace.network.WebSocketService;
import net.suntrans.powerpeace.ui.activity.AboutActivity;
import net.suntrans.powerpeace.ui.activity.BasedActivity;
import net.suntrans.powerpeace.ui.activity.HelpActivity;
import net.suntrans.powerpeace.ui.activity.MsgCenterActivity;
import net.suntrans.powerpeace.ui.activity.PersonActivity;
import net.suntrans.powerpeace.ui.activity.SearchActivity;
import net.suntrans.powerpeace.ui.activity.SettingActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.ui.fragment.BasedFragment;
import net.suntrans.powerpeace.ui.fragment.DownLoadFrgment;
import net.suntrans.powerpeace.ui.fragment.FloorManagerFragment;
import net.suntrans.powerpeace.ui.fragment.SusheFragment;
import net.suntrans.powerpeace.ui.fragment.ZongHeFragmentCopy;
import net.suntrans.powerpeace.ui.fragment.ZongheFragmentNew;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import static net.suntrans.powerpeace.BuildConfig.DEBUG;

/**
 * 楼栋管理员
 */
public class FloorMainActivity extends BasedActivity implements View.OnClickListener
        , BasedFragment.OnFragmentInteractionListener {

    private ActivityFloorMainBinding binding;
    private WebSocketService.ibinder ibinder;

    private String username;
    private int role;
    private String room_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_floor_main);
        initRecyclerView();
        StatusBarCompat.compat(binding.headerView);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawer.setDrawerListener(toggle);

        toggle.syncState();
        init();
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
//                    System.out.println(s);
//                    Version version = JSON.parseObject(s, Version.class);
//                    showUpdateDialog(s, version.data);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        if (!DEBUG) {
//            PgyUpdateManager.setIsForced(false);
            PgyUpdateManager.register(this, "net.suntrans.powerpeace.fileProvider");
        }
    }

    private void showUpdateDialog(String result, Version.VersionInfo versionInfo) {
        String apkName = "hp_" + versionInfo.versionName + "_" + versionInfo.versionCode + ".apk";

        DownLoadFrgment frgment = DownLoadFrgment.newInstance(versionInfo, apkName, result);
        frgment.show(getSupportFragmentManager(), "DownloadFragment");
    }

    private void initRecyclerView() {

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
        binding.navView.recyclerView.addItemDecoration(new DefaultDecoration(UiUtils.dip2px(0.5f,this), Color.parseColor("#f2f4f5")));
        binding.navView.exit.setOnClickListener(this);
        binding.navView.setting.setOnClickListener(this);
        binding.navView.header.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FloorMainActivity.this, PersonActivity.class));
            }
        });
    }


    private void init() {
        username = App.getSharedPreferences().getString("username", "-1");
        role = App.getSharedPreferences().getInt("role", 1);
        room_id = App.getSharedPreferences().getString("room_id", "-1");

        String floor_id = App.getSharedPreferences().getString("floor_id","-1");

        binding.viewpager.setOffscreenPageLimit(2);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        FloorManagerFragment floorManagerFragment = FloorManagerFragment.newInstance(floor_id);
        SusheFragment susheFragment = new SusheFragment();
        ZongheFragmentNew zongHeFragmentCopy = new ZongheFragmentNew();

        adapter.addFragment(floorManagerFragment, "floorManager");
        adapter.addFragment(susheFragment, "sushe");
        adapter.addFragment(zongHeFragmentCopy, "Zonghe");
        binding.viewpager.setAdapter(adapter);
        binding.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.radio0) {
                    binding.viewpager.setCurrentItem(0, false);
                }
                if (checkedId == R.id.radio1) {
                    binding.viewpager.setCurrentItem(1, false);
                }
                if (checkedId == R.id.radio2) {
                    binding.viewpager.setCurrentItem(2, false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.setting:
                binding.drawer.closeDrawers();
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

    @Override
    public void sendOrder(String s) {
        if (ibinder != null)
            ibinder.sendOrder(s);
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
                    intent.setClass(FloorMainActivity.this, MsgCenterActivity.class);
                    break;
                case START_SETTING_ACTIVITY:
                    intent.setClass(FloorMainActivity.this, SettingActivity.class);
                    break;
                case START_HELP_ACTIVITY:
                    intent.setClass(FloorMainActivity.this, HelpActivity.class);
                    break;
                case START_ABOUT_ACTIVITY:
                    intent.setClass(FloorMainActivity.this, AboutActivity.class);
                    break;
                case START_FEEDBACK_ACTIVITY:
                    intent.setClass(FloorMainActivity.this, HelpActivity.class);
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
    protected void onResume() {
        super.onResume();
//        getUserInfo(username, role + "");
    }


}
