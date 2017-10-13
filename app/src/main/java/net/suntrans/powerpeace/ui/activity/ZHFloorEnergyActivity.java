package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.ZHBFloorEntity;
import net.suntrans.powerpeace.bean.ZHEnergyEntity;
import net.suntrans.powerpeace.databinding.ActivityZongheEnergyBinding;
import net.suntrans.powerpeace.databinding.ActivityZongheFloorBinding;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Looney on 2017/7/24.
 */

public class ZHFloorEnergyActivity extends BasedActivity {
    private ActivityZongheEnergyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_zonghe_energy);

        StatusBarCompat.compat(binding.headerView);
        binding.toolbar.setTitle(getIntent().getStringExtra("title"));

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();


        getFloor();
    }

    private void init() {
//        datas = new ArrayList<>();

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFloor();
            }
        });
    }



    private void getFloor() {
        api.getZongheBuildingEnergy("ammeter3",getIntent().getStringExtra("floor_ammeter3_id"))
                .compose(this.<ZHEnergyEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZHEnergyEntity>() {
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
                    public void onNext(ZHEnergyEntity zhbFloorEntity) {
                        if (binding.refreshLayout != null)
                            binding.refreshLayout.setRefreshing(false);
                        ZHEnergyEntity.DataBean info = zhbFloorEntity.data.get(0);
                        binding.yongdian.EDayValue.setText(info.ElectricityDAY+"度");
                        binding.yongdian.EMonthValue.setText(info.ElectricityMonth+"度");
                        binding.yongdian.EValue.setText(info.ElectricityTotal+"度");

                        binding.dianfei.benrixiaofei.setText(info.DayCost+"元");
                        binding.dianfei.benyuexiaofei.setText(info.MonthCost+"元");
                        binding.dianfei.benniandianfei.setText(info.YearCost+"元");
                        binding.dianfei.zongdianfei.setText(info.TotalCost+"元");
                        binding.dianfei.benriyucun.setText(info.DayStore+"元");
                        binding.dianfei.benyueyucun.setText(info.MonthStore+"元");
                        binding.dianfei.bennianyucun.setText(info.YearStore+"元");

                        binding.sunhao.daySuohao.setText(info.DayLoss+"度");
                        binding.sunhao.daySunhaoPer.setText(info.DayLossPercent+"%");

                        binding.sunhao.monthSuohao.setText(info.MonthLoss+"度");
                        binding.sunhao.monthSunhaoPer.setText(info.MonthLossPercent+"%");

                        binding.sunhao.allSuohao.setText(info.TotalLoss+"度");
                        binding.sunhao.zongSunhaoPer.setText(info.TotalLossPercent+"%");

                    }
                });
    }

}
