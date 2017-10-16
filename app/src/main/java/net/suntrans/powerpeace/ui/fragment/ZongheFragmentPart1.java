package net.suntrans.powerpeace.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.ZHEnergyEntity;
import net.suntrans.powerpeace.databinding.FragmentZhPart1Binding;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/10/16.
 * Des:
 */

public class ZongheFragmentPart1 extends BasedFragment {


    private String floor_ammeter3_id;
    public static ZongheFragmentPart1 newInstace(String floor_ammeter3_id) {
        ZongheFragmentPart1 fragmentPart1 = new ZongheFragmentPart1();
        Bundle bundle = new Bundle();
        bundle.putString("floor_ammeter3_id",floor_ammeter3_id);
        fragmentPart1.setArguments(bundle);
        return fragmentPart1;
    }

    private FragmentZhPart1Binding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zh_part1,container,false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        floor_ammeter3_id = getArguments().getString("floor_ammeter3_id");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFloor();
            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        getFloor();
    }

    private void getFloor() {
        binding.refreshLayout.setRefreshing(true);
        api.getZongheBuildingEnergy("ammeter3", floor_ammeter3_id)
                .compose(this.<ZHEnergyEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
                        binding.yongdian.EDayValue.setText(info.ElectricityDAY + "度");
                        binding.yongdian.EMonthValue.setText(info.ElectricityMonth + "度");
                        binding.yongdian.EValue.setText(info.ElectricityTotal + "度");

                        binding.dianfei.benrixiaofei.setText(info.DayCost + "元");
                        binding.dianfei.benyuexiaofei.setText(info.MonthCost + "元");
                        binding.dianfei.benniandianfei.setText(info.YearCost + "元");
                        binding.dianfei.zongdianfei.setText(info.TotalCost + "元");
                        binding.dianfei.benriyucun.setText(info.DayStore + "元");
                        binding.dianfei.benyueyucun.setText(info.MonthStore + "元");
                        binding.dianfei.bennianyucun.setText(info.YearStore + "元");

                        binding.sunhao.daySuohao.setText(info.DayLoss + "度");
                        binding.sunhao.daySunhaoPer.setText(info.DayLossPercent + "%");

                        binding.sunhao.monthSuohao.setText(info.MonthLoss + "度");
                        binding.sunhao.monthSunhaoPer.setText(info.MonthLossPercent + "%");

                        binding.sunhao.allSuohao.setText(info.TotalLoss + "度");
                        binding.sunhao.zongSunhaoPer.setText(info.TotalLossPercent + "%");

                    }
                });
    }

}
