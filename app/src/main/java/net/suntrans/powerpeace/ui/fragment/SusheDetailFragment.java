package net.suntrans.powerpeace.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.RoomInfoSelection;
import net.suntrans.powerpeace.bean.RoomInfolEntity;
import net.suntrans.powerpeace.databinding.FragmentSusheDetailBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.activity.AmmeterHisActivity;
import net.suntrans.powerpeace.ui.activity.PostageHisActivity;
import net.suntrans.powerpeace.ui.activity.StudentInfoActivity;
import net.suntrans.powerpeace.ui.activity.SusheDetailActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.databinding.DataBindingUtil.inflate;

/**
 * Created by Looney on 2017/9/13.
 */

public class SusheDetailFragment extends BasedFragment implements StateView.OnRetryClickListener, SwipeRefreshLayout.OnRefreshListener {

    private FragmentSusheDetailBinding binding;
    private List<RoomInfoSelection> datas = new ArrayList<>();
    private MyAdapter adapter;
    private Map<String, String> mMeterDictionaries;//电表中英字典
    private Map<String, String> mAccountDictionaries;//账户栏目中英字典
    private Map<String, String> meterInfo;//电表参数map
    private String param;
    private StateView stateView;
    private String whole_name;
    private int mRefreshType = STATE_VIEW_REFRESH;
    private static final int SWIP_REFRESH_LAYOUT = 0x01;
    private static final int STATE_VIEW_REFRESH = 0x02;
    private String role;

    public static SusheDetailFragment newInstance(String param,String role){
        SusheDetailFragment fragment = new SusheDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param",param);
        bundle.putString("role",role);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sushe_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDictiomaries();
        init(view);
    }

    private void init(View view) {
        param = getArguments().getString("param");
        role = getArguments().getString("role");
        whole_name = getActivity().getIntent().getStringExtra("whole_name");
        stateView = StateView.inject(view.findViewById(R.id.content));
        stateView.setOnRetryClickListener(this);
        binding.refreshLayout.setOnRefreshListener(this);

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
                        LogUtil.i("您点击了账户信息");
                        Intent intent = new Intent(getActivity(), PostageHisActivity.class);
                        intent.putExtra("title", getActivity().getIntent().getStringExtra("title"));
                        startActivity(intent);
                        break;
                    case RoomInfoSelection.TYPE_DEV_CHANNEL:

                        break;
                    case RoomInfoSelection.TYPE_METER_INFO: {
                        Intent intent2 = new Intent(getActivity(), AmmeterHisActivity.class);
                        intent2.putExtra("title", getActivity().getIntent().getStringExtra("title"));
                        intent2.putExtra("paramName", selection.name);
                        intent2.putExtra("room_id", param);
                        startActivity(intent2);
                        break;
                    }
                    case RoomInfoSelection.TYPE_ROOM_STU:
                        Intent intent1 = new Intent(getActivity(), StudentInfoActivity.class);
                        intent1.putExtra("name", selection.name);
                        intent1.putExtra("studentID", selection.studentID);
                        startActivity(intent1);
                        break;
                }
            }
        });

    }


    private void initDictiomaries() {
        mMeterDictionaries = new HashMap<>();
        mAccountDictionaries = new HashMap<>();

        mMeterDictionaries.put("PR_value", "功率因数");
        mMeterDictionaries.put("E_value", "用电量");
        mMeterDictionaries.put("I_value", "电流");
        mMeterDictionaries.put("V_value", "电压");
        mMeterDictionaries.put("P_value", "功率");

        mAccountDictionaries.put("balans", "账户余额");
        mAccountDictionaries.put("status", "账户状态");
        mAccountDictionaries.put("dayuse", "当日用电量");
        mAccountDictionaries.put("monthuse", "当月用电量");

    }

    @Override
    public void onRetryClick() {
        mRefreshType = STATE_VIEW_REFRESH;
        getData(param);
    }

    @Override
    public void onRefresh() {
        mRefreshType = SWIP_REFRESH_LAYOUT;
        getData(param);
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
                helper.setText(R.id.wholeName, whole_name);
            } else {
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

    private void getData(String room_id) {
        LogUtil.i(room_id);
        if (mRefreshType == STATE_VIEW_REFRESH) {
            stateView.showLoading();
            binding.recyclerView.setVisibility(View.INVISIBLE);
        }
        addSubscription(api.getSusheDetail(room_id), new BaseSubscriber<RoomInfolEntity>(getContext()) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                if (mRefreshType == STATE_VIEW_REFRESH) {
                    stateView.showRetry();
                } else {
                    if (binding.refreshLayout != null)
                        binding.refreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onNext(RoomInfolEntity info) {
                refreshLayout(info);
                binding.refreshLayout.setRefreshing(false);
                binding.recyclerView.setVisibility(View.VISIBLE);
                stateView.showContent();
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
            int i = 0;
            Map<String, String> map = account.get(0);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                RoomInfoSelection zhanhuxinxi = null;

                String key = entry.getKey();
                String value = entry.getValue();
                if (mAccountDictionaries.get(key) != null) {
                    if (i == 0)
                        zhanhuxinxi = new RoomInfoSelection(true, "账户信息");
                    else
                        zhanhuxinxi = new RoomInfoSelection(false, "");
                    zhanhuxinxi.type = RoomInfoSelection.TYPE_ACCOUNT;
                    zhanhuxinxi.name = mAccountDictionaries.get(key);
                    zhanhuxinxi.value = value;
                    datas.add(zhanhuxinxi);
                    i++;

                }
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
                selection.studentID = stu.get("studentID");
                datas.add(selection);
                i++;
            }
        }


        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        getData(param);
    }
}
