package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.fragments.DataPickerDialogFragment;
import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.ApiHelper;
import net.suntrans.powerpeace.bean.LogInfoEntity;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.bean.PostageEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.databinding.ActivityLogBinding;
import net.suntrans.powerpeace.databinding.ActivityPostageHisBinding;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.utils.StatusBarCompat;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.data;

public class LogActivity extends BasedActivity implements View.OnClickListener, DataPickerDialogFragment.OnDateSetChangerListener, ApiHelper.OnDataGetListener, StateView.OnRetryClickListener {

    private ActivityLogBinding binding;
    private String date;
    private int mYear;
    private int mMonth;
    private int mDay;
    private List<LogInfoEntity.LogInfo> datas;
    private List<LogInfoEntity.LogInfo> copy;
    private Myadapter adapter;
    private ApiHelper helper;
    private String room_id;
    private StateView stateView;
    private int currentCheckedId = R.id.radio0;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log);

        StatusBarCompat.compat(binding.headerView);

        stateView = StateView.inject(binding.content);
        stateView.setOnRetryClickListener(this);

        String title = getIntent().getStringExtra("title");
        if (title == null)
            title = "我的";
        binding.toolbar.setTitle(title + "宿舍状态日志");
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding.time.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        binding.year.setText(mYear + "年");
        binding.month.setText(mMonth + "");

        time = new StringBuilder()
                .append(mYear)
                .append("-")
                .append(pad(mMonth))
                .append("-")
                .append(pad(mDay))
                .toString();
        datas = new ArrayList<>();
        copy = new ArrayList<>();
        room_id = getIntent().getStringExtra("room_id");
//        for (int i = 0; i < 8; i++) {
//            boolean chongzhi = i % 2 == 0;
//            PostageEntity.PostageInfo info = new PostageEntity.PostageInfo();
//            if (!chongzhi) {
//                info.money = "-" + i + ".00元";
//                info.msg = "宿舍用电20.23度";
//                info.type = "1";
//                info.created_at = mYear + "年" + mMonth + "月" + mDay + "日";
//                datas.add(info);
//            } else {
//                info.money = "+" + i + ".00元";
//                info.msg = "王晓庆充值30元";
//                info.type = "2";
//                info.created_at = mYear + "年" + mMonth + "月" + mDay + "日";
//                datas.add(info);
//            }
//
//        }
        copy.addAll(datas);
        adapter = new Myadapter(R.layout.item_log_his, copy);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        binding.recyclerView.setAdapter(adapter);
        binding.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                currentCheckedId = checkedId;
                switch (checkedId) {
                    case R.id.radio0:
                        copy.clear();
                        for (LogInfoEntity.LogInfo info :
                                datas) {
                            if (info.type.equals("1")) {
                                copy.add(info);
                            }
                        }
                        break;
                    case R.id.radio1:
                        copy.clear();
                        copy.addAll(datas);
                        break;
                    case R.id.radio2:
                        copy.clear();
                        for (LogInfoEntity.LogInfo info :
                                datas) {
                            if (info.type.equals("2")) {
                                copy.add(info);
                            }
                        }
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
        binding.radio0.setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 10; i < 18; i++) {
            LogInfoEntity.LogInfo info = new LogInfoEntity.LogInfo();
            if (i % 2 == 0)
                info.message = "打开了照明";
            else
                info.message = "关闭了照明";
            info.name = "黄小玲";
            info.type = "1";
            info.created_at = "2017-09-24 18:" + i + ":23";
            datas.add(info);
        }
        copy.addAll(datas);

        adapter.notifyDataSetChanged();
//        getData(time);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.time) {
            DataPickerDialogFragment fragment = (DataPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("dateDialogFragment");
            if (fragment == null) {
                fragment = new DataPickerDialogFragment();
                fragment.setListener(this);
            }
            fragment.show(getSupportFragmentManager(), "dateDialogFragment");
        }
    }

    @Override
    public void onDateSet(int year, int month, int day, String format) {
        date = format;
        binding.year.setText(year + "年");
        binding.month.setText(month + "");
        time = format;
        getData(time);
    }

    @Override
    public void onRetryClick() {
        getData(time);
    }


    class Myadapter extends BaseQuickAdapter<LogInfoEntity.LogInfo, BaseViewHolder> {

        public Myadapter(@LayoutRes int layoutResId, @Nullable List<LogInfoEntity.LogInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LogInfoEntity.LogInfo item) {
            String action  =  item.status ? "打开" : "关闭";
            helper.setText(R.id.msg, item.name + item.message);
            helper.setText(R.id.created_at, item.created_at);
        }
    }

    private void getData(String time) {
        if (helper == null)
            helper = new ApiHelper();
        stateView.showLoading();
        binding.recyclerView.setVisibility(View.INVISIBLE);
        helper.getLogInfo(room_id, time, this);
    }


    @Override
    public void onUserInfoReturned(UserInfoEntity infoEntity) {

    }

    @Override
    public void onLogInfoReturned(LogInfoEntity infoEntity) {
        stateView.showContent();
        binding.recyclerView.setVisibility(View.VISIBLE);
        datas.clear();
        datas.addAll(infoEntity.info);

        int caozuoCount = 0;
        for (LogInfoEntity.LogInfo info :
                infoEntity.info) {
            if (info.type.equals("1")) {
                caozuoCount++;
            }
        }
        binding.caozuoCount.setText(caozuoCount + "");
        refreshLayout();
    }

    private void refreshLayout() {
        switch (currentCheckedId) {
            case R.id.radio0:
                copy.clear();
                for (LogInfoEntity.LogInfo info :
                        datas) {
                    if (info.type.equals("1")) {
                        copy.add(info);
                    }
                }
                break;
            case R.id.radio1:
                copy.clear();
                copy.addAll(datas);
                break;
            case R.id.radio2:
                copy.clear();
                for (LogInfoEntity.LogInfo info :
                        datas) {
                    if (info.type.equals("2")) {
                        copy.add(info);
                    }
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        stateView.showRetry();
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
