package net.suntrans.powerpeace.ui.activity;

import android.content.ComponentName;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.Constants;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.RoomInfoSelection;
import net.suntrans.powerpeace.bean.RoomInfolEntity;
import net.suntrans.powerpeace.databinding.ActivitySusheDetailBinding;
import net.suntrans.powerpeace.network.WebSocketService;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.ui.fragment.BasedFragment;
import net.suntrans.powerpeace.ui.fragment.SusheDetailFragment;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by Looney on 2017/9/4.
 */

public class SusheDetailActivity extends BasedActivity  implements BasedFragment.OnFragmentInteractionListener{

    private ActivitySusheDetailBinding binding;
//    private List<RoomInfoSelection> datas = new ArrayList<>();
//    private MyAdapter adapter;
//    private Map<String, String> mMeterDictionaries;//电表中英字典
//    private Map<String, String> mAccountDictionaries;//账户栏目中英字典
//    private Map<String, String> meterInfo;//电表参数map
    private String room_id;
//    private StateView stateView;
//    private String whole_name;
//    private int mRefreshType = STATE_VIEW_REFRESH;
//    private static final int SWIP_REFRESH_LAYOUT = 0x01;
//    private static final int STATE_VIEW_REFRESH = 0x02;


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


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sushe_detail);

        init();

        binding.toolbar.setTitle(getIntent().getStringExtra("title") + "详细信息");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        room_id = getIntent().getStringExtra("room_id");
        SusheDetailFragment fragment =SusheDetailFragment.newInstance(room_id, Constants.ADMIN);
        getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment).commit();
//        stateView = StateView.inject(findViewById(R.id.content));
//        stateView.setOnRetryClickListener(this);
//
//        initDictiomaries();
//
//        adapter = new MyAdapter(R.layout.item_roominfo, R.layout.item_roominfo_header, datas);
//        binding.recyclerView.setAdapter(adapter);
//        binding.recyclerView.addItemDecoration(new DefaultDecoration());
//        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                RoomInfoSelection selection = datas.get(position);
//                switch (selection.type) {
//                    case RoomInfoSelection.TYPE_ACCOUNT:
//                        LogUtil.i("您点击了账户信息");
//                        Intent intent = new Intent(SusheDetailActivity.this, PostageHisActivity.class);
//                        intent.putExtra("title", getIntent().getStringExtra("title"));
//                        startActivity(intent);
//                        break;
//                    case RoomInfoSelection.TYPE_DEV_CHANNEL:
//
//                        break;
//                    case RoomInfoSelection.TYPE_METER_INFO: {
//                        Intent intent2 = new Intent(SusheDetailActivity.this, AmmeterHisActivity.class);
//                        intent2.putExtra("title", getIntent().getStringExtra("title"));
//                        intent2.putExtra("paramName", selection.name);
//                        intent2.putExtra("room_id", room_id);
//                        startActivity(intent2);
//                        break;
//                    }
//                    case RoomInfoSelection.TYPE_ROOM_STU:
//                        Intent intent1 = new Intent(SusheDetailActivity.this, StudentInfoActivity.class);
//                        intent1.putExtra("name", selection.name);
//                        intent1.putExtra("studentID", selection.studentID);
//                        startActivity(intent1);
//                        break;
//                }
//            }
//        });
//        whole_name = getIntent().getStringExtra("whole_name");
//        binding.refreshLayout.setOnRefreshListener(this);
    }

    private void init() {

        Intent intent = new Intent();
        intent.setClass(this, WebSocketService.class);
        bindService(intent, connection, ContextWrapper.BIND_AUTO_CREATE);

    }

//
//    private void initDictiomaries() {
//        mMeterDictionaries = new HashMap<>();
//        mAccountDictionaries = new HashMap<>();
//
//        mMeterDictionaries.put("PR_value", "功率因数");
//        mMeterDictionaries.put("E_value", "用电量");
//        mMeterDictionaries.put("I_value", "电流");
//        mMeterDictionaries.put("V_value", "电压");
//        mMeterDictionaries.put("P_value", "功率");
//
//        mAccountDictionaries.put("balans", "账户余额");
//        mAccountDictionaries.put("status", "账户状态");
//        mAccountDictionaries.put("dayuse", "当日用电量");
//        mAccountDictionaries.put("monthuse", "当月用电量");
//
//    }
//
//    @Override
//    public void onRefresh() {
//        mRefreshType = SWIP_REFRESH_LAYOUT;
//        getData(room_id);
//    }
//
//    @Override
//    public void onRetryClick() {
//        mRefreshType = STATE_VIEW_REFRESH;
//        getData(room_id);
//    }
//
//
//    class MyAdapter extends BaseSectionQuickAdapter<RoomInfoSelection, BaseViewHolder> {
//
//        public MyAdapter(int layoutResId, int sectionHeadResId, List<RoomInfoSelection> data) {
//            super(layoutResId, sectionHeadResId, data);
//        }
//
//        @Override
//        protected void convertHead(BaseViewHolder helper, RoomInfoSelection item) {
//            helper.setText(R.id.headerName, item.header);
//            helper.setText(R.id.name, item.name);
//            helper.setText(R.id.value, item.value);
//            if (item.type.equals(RoomInfoSelection.TYPE_DEV_CHANNEL)) {
//                helper.getView(R.id.normal).setVisibility(View.GONE);
//                helper.getView(R.id.switchRl).setVisibility(View.VISIBLE);
//            } else {
//                helper.getView(R.id.normal).setVisibility(View.VISIBLE);
//                helper.getView(R.id.switchRl).setVisibility(View.GONE);
//            }
//            if (item.type.equals(RoomInfoSelection.TYPE_WHOLE_NAME)) {
//                helper.getView(R.id.root).setVisibility(View.GONE);
//                helper.getView(R.id.wholeName).setVisibility(View.VISIBLE);
//                helper.setText(R.id.wholeName, whole_name);
//            } else {
//                helper.getView(R.id.root).setVisibility(View.VISIBLE);
//                helper.getView(R.id.wholeName).setVisibility(View.GONE);
//
//            }
//
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, RoomInfoSelection item) {
//            helper.setText(R.id.name, item.name);
//            helper.setText(R.id.value, item.value);
//            if (item.type.equals(RoomInfoSelection.TYPE_DEV_CHANNEL)) {
//                helper.getView(R.id.normal).setVisibility(View.GONE);
//                helper.getView(R.id.switchRl).setVisibility(View.VISIBLE);
//            } else {
//                helper.getView(R.id.normal).setVisibility(View.VISIBLE);
//                helper.getView(R.id.switchRl).setVisibility(View.GONE);
//            }
//
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getData(room_id);
//    }
//
//    private void getData(String room_id) {
//        LogUtil.i(room_id);
//        if (mRefreshType == STATE_VIEW_REFRESH) {
//            stateView.showLoading();
//            binding.recyclerView.setVisibility(View.INVISIBLE);
//        }
//        addSubscription(api.getSusheDetail(room_id), new BaseSubscriber<RoomInfolEntity>(this) {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                e.printStackTrace();
//                if (mRefreshType == STATE_VIEW_REFRESH) {
//                    stateView.showRetry();
//                } else {
//                    if (binding.refreshLayout != null)
//                        binding.refreshLayout.setRefreshing(false);
//                }
//
//            }
//
//            @Override
//            public void onNext(RoomInfolEntity info) {
//                refreshLayout(info);
//                binding.refreshLayout.setRefreshing(false);
//                binding.recyclerView.setVisibility(View.VISIBLE);
//                stateView.showContent();
//            }
//        });
//    }
//
//    private void refreshLayout(RoomInfolEntity info) {
//        datas.clear();
//        List<Map<String, String>> account = info.account;
//        List<Map<String, String>> dev_channel = info.dev_channel;
//        List<Map<String, String>> meter_info = info.meter_info;
//        List<Map<String, String>> room_stu = info.room_stu;
//
//        if (account != null) {
//            int i = 0;
//            Map<String, String> map = account.get(0);
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                RoomInfoSelection zhanhuxinxi = null;
//
//                String key = entry.getKey();
//                String value = entry.getValue();
//                if (mAccountDictionaries.get(key) != null) {
//                    if (i == 0)
//                        zhanhuxinxi = new RoomInfoSelection(true, "账户信息");
//                    else
//                        zhanhuxinxi = new RoomInfoSelection(false, "");
//                    zhanhuxinxi.type = RoomInfoSelection.TYPE_ACCOUNT;
//                    zhanhuxinxi.name = mAccountDictionaries.get(key);
//                    zhanhuxinxi.value = value;
//                    datas.add(zhanhuxinxi);
//                    i++;
//
//                }
//            }
//
//
//        }
//
//
//        if (dev_channel != null) {
//            for (int i = 0; i < dev_channel.size(); i++) {
//                Map<String, String> map = dev_channel.get(i);
//
//                RoomInfoSelection selection = null;
//                if (i == 0) {
//                    selection = new RoomInfoSelection(true, "用电状态");
//
//                } else {
//                    selection = new RoomInfoSelection(false, "用电状态");
//
//                }
//                selection.type = RoomInfoSelection.TYPE_DEV_CHANNEL;
//                selection.name = map.get("name");
//                selection.id = map.get("id");
//                selection.num = map.get("num");
//                selection.status = map.get("status").equals("true") ? true : false;
//
//                datas.add(selection);
//            }
//
//        }
//
//
//        RoomInfoSelection wholename = new RoomInfoSelection(true, "宿舍详情");
//        wholename.name = whole_name;
//        wholename.type = RoomInfoSelection.TYPE_WHOLE_NAME;
//        datas.add(wholename);
//
//
//        if (meter_info != null) {
//            meterInfo = meter_info.get(0);
//            int i = 0;
//            for (Map.Entry<String, String> entry : meterInfo.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                if (key.equals("id")) {
//                    continue;
//                }
//                RoomInfoSelection selection = null;
//
//                if (i == 0) {
//                    selection = new RoomInfoSelection(true, "电量信息");
//                } else {
//                    selection = new RoomInfoSelection(false, "电量信息");
//                }
//                selection.type = RoomInfoSelection.TYPE_METER_INFO;
//                selection.name = mMeterDictionaries.get(key);
//                selection.value = value;
//                datas.add(selection);
//                i++;
//            }
//        }
//
//        if (room_stu != null) {
//            int i = 0;
//
//            for (Map<String, String> stu :
//                    room_stu) {
//                RoomInfoSelection selection = null;
//
//                if (i == 0) {
//                    selection = new RoomInfoSelection(true, "学生信息");
//                } else {
//                    selection = new RoomInfoSelection(false, "学生信息");
//                }
//                selection.type = RoomInfoSelection.TYPE_ROOM_STU;
//                selection.name = stu.get("name");
//                selection.studentID = stu.get("studentID");
//                datas.add(selection);
//                i++;
//            }
//        }
//
//
//        adapter.notifyDataSetChanged();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pay) {
            startActivity(new Intent(this, PayActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void sendOrder(String s) {

    }
}
