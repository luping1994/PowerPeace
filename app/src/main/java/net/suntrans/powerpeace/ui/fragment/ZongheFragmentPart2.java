package net.suntrans.powerpeace.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.ZHEnergyEntity;
import net.suntrans.powerpeace.bean.ZHEnergyShishiEntity;
import net.suntrans.powerpeace.databinding.FragmentZhPart1Binding;
import net.suntrans.powerpeace.databinding.FragmentZhPart2Binding;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;

import java.util.ArrayList;
import java.util.List;

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
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        adapter = new Myadapter(R.layout.item_current_data, datas);
        binding.recyclerView.setAdapter(adapter);
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
            String name = item.name;

            if (name.contains(""))
            imageView.setImageResource(R.drawable.ic_dianya);
            imageView.setImageResource(R.drawable.ic_gonglv);
            imageView.setImageResource(R.drawable.ic_dl);
            imageView.setImageResource(R.drawable.ic_dianliu);


        }
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
                    public void onNext(ZHEnergyShishiEntity info) {
                        if (binding.refreshLayout != null)
                            binding.refreshLayout.setRefreshing(false);

                        datas.clear();
                        datas.addAll(info.info);
                        adapter.notifyDataSetChanged();

                    }
                });
    }

}
