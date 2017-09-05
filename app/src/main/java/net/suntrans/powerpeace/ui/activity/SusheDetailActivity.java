package net.suntrans.powerpeace.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.RoomInfoSelection;
import net.suntrans.powerpeace.bean.RoomInfolEntity;
import net.suntrans.powerpeace.databinding.ActivitySusheDetailBinding;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
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

public class SusheDetailActivity extends BasedActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivitySusheDetailBinding binding;
    private List<RoomInfoSelection> datas = new ArrayList<>();
    private MyAdapter adapter;
    private Map<String, String> mMeterDictionaries;
    private Map<String, String> meterInfo;//电表参数map
    private String room_id;
    private StateView stateView;
    private String whole_name;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sushe_detail);
        binding.toolbar.setTitle(getIntent().getStringExtra("title") + "详细信息");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        room_id = getIntent().getStringExtra("room_id");
        stateView = StateView.inject(findViewById(R.id.content));

        initDictiomaries();
        adapter = new MyAdapter(R.layout.item_roominfo, R.layout.item_roominfo_header, datas);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RoomInfoSelection selection = datas.get(position);
                switch (selection.type) {
                    case RoomInfoSelection.TYPE_ACCOUNT:
                        UiUtils.showToast("您点击了账户信息");
                        Intent intent = new Intent(SusheDetailActivity.this,PostageHisActivity.class);
                        intent.putExtra("title",getIntent().getStringExtra("title"));
                        startActivity(intent);
                        break;
                    case RoomInfoSelection.TYPE_DEV_CHANNEL:

                        break;
                    case RoomInfoSelection.TYPE_METER_INFO: {
                        break;
                    }
                    case RoomInfoSelection.TYPE_ROOM_STU:
                        break;
                }
                UiUtils.showToast("您点击了" + selection.name);
            }
        });
        whole_name = getIntent().getStringExtra("whole_name");
        binding.refreshLayout.setOnRefreshListener(this);
    }

    private void initDictiomaries() {
        mMeterDictionaries = new HashMap<>();
        mMeterDictionaries.put("PR_value", "功率因数");
        mMeterDictionaries.put("E_value", "用电量");
        mMeterDictionaries.put("I_value", "电压");
        mMeterDictionaries.put("V_value", "电流");
        mMeterDictionaries.put("P_value", "功率");
    }

    @Override
    public void onRefresh() {
        getData(room_id);
    }


    class MyAdapter extends BaseSectionQuickAdapter<RoomInfoSelection, BaseViewHolder> {

        public MyAdapter(int layoutResId, int sectionHeadResId, List<RoomInfoSelection> data) {
            super(layoutResId, sectionHeadResId, data);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, RoomInfoSelection item) {
            helper.setText(R.id.headerName, item.header);
            helper.setText(R.id.name, item.name);
            helper.setText(R.id.value, item.value);
            if (item.type.equals(RoomInfoSelection.TYPE_DEV_CHANNEL)) {
                helper.getView(R.id.normal).setVisibility(View.GONE);
                helper.getView(R.id.switchRl).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.normal).setVisibility(View.VISIBLE);
                helper.getView(R.id.switchRl).setVisibility(View.GONE);
            }
            if (item.type.equals(RoomInfoSelection.TYPE_WHOLE_NAME)) {
                helper.getView(R.id.root).setVisibility(View.GONE);
                helper.getView(R.id.wholeName).setVisibility(View.VISIBLE);
                helper.setText(R.id.wholeName,whole_name);
            }else {
                helper.getView(R.id.root).setVisibility(View.VISIBLE);
                helper.getView(R.id.wholeName).setVisibility(View.GONE);

            }

        }

        @Override
        protected void convert(BaseViewHolder helper, RoomInfoSelection item) {
            helper.setText(R.id.name, item.name);
            helper.setText(R.id.value, item.value);
            if (item.type.equals(RoomInfoSelection.TYPE_DEV_CHANNEL)) {
                helper.getView(R.id.normal).setVisibility(View.GONE);
                helper.getView(R.id.switchRl).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.normal).setVisibility(View.VISIBLE);
                helper.getView(R.id.switchRl).setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(room_id);
    }

    private void getData(String room_id) {
        LogUtil.i(room_id);
        addSubscription(api.getSusheDetail(room_id), new Subscriber<RoomInfolEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(RoomInfolEntity info) {
                refreshLayout(info);
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshLayout(RoomInfolEntity info) {
        datas.clear();
        List<Map<String, String>> account = info.account;
        List<Map<String, String>> dev_channel = info.dev_channel;
        List<Map<String, String>> meter_info = info.meter_info;
        List<Map<String, String>> room_stu = info.room_stu;

        if (account != null) {
            for (int i = 0; i < account.size(); i++) {
                Map<String, String> map = account.get(i);
                RoomInfoSelection selection = null;
                if (i == 0) {
                    selection = new RoomInfoSelection(true, "账户信息");

                } else {
                    selection = new RoomInfoSelection(false, "账户信息");

                }
                selection.type = RoomInfoSelection.TYPE_ACCOUNT;
                selection.name = "账户余额";
                selection.value = map.get("balans");
                datas.add(selection);
            }
        }


        if (dev_channel != null) {
            for (int i = 0; i < dev_channel.size(); i++) {
                Map<String, String> map = dev_channel.get(i);

                RoomInfoSelection selection = null;
                if (i == 0) {
                    selection = new RoomInfoSelection(true, "用电状态");

                } else {
                    selection = new RoomInfoSelection(false, "用电状态");

                }
                selection.type = RoomInfoSelection.TYPE_DEV_CHANNEL;
                selection.name = map.get("name");
                selection.id = map.get("id");
                selection.num = map.get("num");
                selection.status = map.get("status").equals("true") ? true : false;

                datas.add(selection);
            }

        }




        RoomInfoSelection wholename = new RoomInfoSelection(true, "宿舍详情");
        wholename.name = whole_name;
        wholename.type = RoomInfoSelection.TYPE_WHOLE_NAME;
        datas.add(wholename);


        if (meter_info != null) {
            meterInfo = meter_info.get(0);
            int i = 0;
            for (Map.Entry<String, String> entry : meterInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals("id")) {
                    continue;
                }
                RoomInfoSelection selection = null;

                if (i == 0) {
                    selection = new RoomInfoSelection(true, "电量信息");
                } else {
                    selection = new RoomInfoSelection(false, "电量信息");
                }
                selection.type = RoomInfoSelection.TYPE_METER_INFO;
                selection.name = mMeterDictionaries.get(key);
                selection.value = value;
                datas.add(selection);
                i++;
            }
        }

        if (room_stu != null) {
            int i = 0;

            for (Map<String, String> stu :
                    room_stu) {
                RoomInfoSelection selection = null;

                if (i == 0) {
                    selection = new RoomInfoSelection(true, "学生信息");
                } else {
                    selection = new RoomInfoSelection(false, "学生信息");
                }
                selection.type = RoomInfoSelection.TYPE_ROOM_STU;
                selection.name = stu.get("name");
                datas.add(selection);
                i++;
            }
        }


        adapter.notifyDataSetChanged();

    }

}
