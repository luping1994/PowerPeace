package net.suntrans.powerpeace.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.ZHEnergyEntity;
import net.suntrans.powerpeace.bean.ZHEnergyShishiEntity;
import net.suntrans.powerpeace.databinding.FragmentZhPart1Binding;
import net.suntrans.powerpeace.databinding.FragmentZhPart2Binding;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/10/16.
 * Des:
 */

public class ZongheFragmentPart2 extends BasedFragment {


    private String sno;

    public static ZongheFragmentPart2 newInstace(String floor_ammeter3_id) {
        ZongheFragmentPart2 fragmentPart1 = new ZongheFragmentPart2();
        Bundle bundle = new Bundle();
        bundle.putString("sno", floor_ammeter3_id);
        fragmentPart1.setArguments(bundle);
        return fragmentPart1;
    }

    private FragmentZhPart2Binding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zh_part2, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sno = getArguments().getString("sno");
        System.out.println(sno);

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
        api.getZHEnergyShishi(sno)
                .compose(this.<ZHEnergyShishiEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZHEnergyShishiEntity>() {
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
                    public void onNext(ZHEnergyShishiEntity zhbFloorEntity) {
                        if (binding.refreshLayout != null)
                            binding.refreshLayout.setRefreshing(false);
                        ZHEnergyShishiEntity.InfoBean infoBean = zhbFloorEntity.info.get(0);
                        binding.sanxiang.AV.setText(infoBean.VolA + "V");
                        binding.sanxiang.BV.setText(infoBean.VolB + "V");
                        binding.sanxiang.CV.setText(infoBean.VolC + "V");
                        binding.sanxiang.aAValue.setText(infoBean.IA + "A");
                        binding.sanxiang.bAValue.setText(infoBean.IB + "A");
                        binding.sanxiang.cAValue.setText(infoBean.IC + "A");
                        binding.sanxiang.aPValue.setText(infoBean.ActivePower + "W");
                        binding.sanxiang.rPValue.setText(infoBean.ReactivePower + "kvar");


                    }
                });
    }

}
