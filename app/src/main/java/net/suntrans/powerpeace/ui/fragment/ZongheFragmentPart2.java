package net.suntrans.powerpeace.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.ZHEnergyEntity;
import net.suntrans.powerpeace.bean.ZHEnergyShishiEntity;
import net.suntrans.powerpeace.databinding.FragmentZhPart1Binding;
import net.suntrans.powerpeace.databinding.FragmentZhPart2Binding;
import net.suntrans.powerpeace.ui.activity.ZHCurHisActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/10/16.
 * Des:
 */

public class ZongheFragmentPart2 extends BasedFragment {


    private String sno;
    private List<ZHEnergyShishiEntity.InfoBean> datas;
    private Myadapter adapter;
    private  Map<String, Integer> map;

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
        initData();
    }

    private void initData() {
        map = new HashMap<>();
        map.put("V",R.drawable.ic_dianya);
        map.put("A",R.drawable.ic_dianliu);
        map.put("KW",R.drawable.ic_gonglv);
        map.put("KVAR",R.drawable.ic_gonglv);
        map.put("",R.drawable.ic_gonglvyinsu);
        map.put("℃",R.drawable.ic_wendu);
        map.put("度",R.drawable.ic_dl);
        map.put("KW·H",R.drawable.ic_dl);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datas = new ArrayList<>();
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFloor();
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        adapter = new Myadapter(R.layout.item_current_data, datas);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                System.out.println(datas.get(position).datapoint);
                Intent intent = new Intent(getActivity(), ZHCurHisActivity.class);
                intent.putExtra("paramName",datas.get(position).name);
                intent.putExtra("datapoint",datas.get(position).datapoint);
                intent.putExtra("unit",datas.get(position).unit);
                intent.putExtra("sno",sno);
                intent.putExtra("title",datas.get(position).name+"历史记录");
                startActivity(intent);
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        adapter.setHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.update_time,null));
    }

    private class Myadapter extends BaseQuickAdapter<ZHEnergyShishiEntity.InfoBean, BaseViewHolder> {

        public Myadapter(@LayoutRes int layoutResId, @Nullable List<ZHEnergyShishiEntity.InfoBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ZHEnergyShishiEntity.InfoBean item) {
            helper.setText(R.id.name, item.name)
                    .setText(R.id.value, item.value + item.unit);
            ImageView imageView = helper.getView(R.id.image);
            String unit = item.unit.toUpperCase();

            if (unit!=null){
                if (map.get(unit)!=null)
                    imageView.setImageResource(map.get(unit));

            }



        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        getFloor();
    }

    private void getFloor() {
        binding.refreshLayout.setRefreshing(true);
        RetrofitHelper.getApi().getZHEnergyShishi(sno)
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
                    public void onNext(ZHEnergyShishiEntity info) {
                        if (binding.refreshLayout != null)
                            binding.refreshLayout.setRefreshing(false);

                        TextView updataName = (TextView) adapter.getHeaderLayout().findViewById(R.id.updateTime);
                        updataName.setText(info.updated_time);
                        datas.clear();
                        datas.addAll(info.info);
                        adapter.notifyDataSetChanged();


                    }
                });
    }




}
