package net.suntrans.powerpeace.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.ZHBuildingAdapter;
import net.suntrans.powerpeace.bean.ZHBuildingEntity;
import net.suntrans.powerpeace.databinding.FragmentZongheNewBinding;
import net.suntrans.powerpeace.ui.activity.ZHBuildingEnergyActivity;
import net.suntrans.powerpeace.ui.activity.ZHFloorActivity;
import net.suntrans.powerpeace.ui.activity.ZHFloorEnergyActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/10/11.
 */

public class ZongheFragmentNew extends BasedFragment {

    private FragmentZongheNewBinding binding;
    private List<ZHBuildingEntity.ZHBuilding> datas;
    private ZHBuildingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zonghe_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datas = new ArrayList<>();
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        adapter = new ZHBuildingAdapter(datas, getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setGroupIndicator(null);
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.recyclerView.setDivider(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        adapter.setOnChildClickListener(new ZHBuildingAdapter.OnChild$ParentClickListener() {
            @Override
            public void onChildClick(int grouPposition, int childPosition) {
//                System.out.println(datas.get(grouPposition).ammeter3.get(childPosition).name);
                Intent intent = new Intent(getActivity(), ZHFloorActivity.class);
                intent.putExtra("title",datas.get(grouPposition).title+datas.get(grouPposition).ammeter3.get(childPosition).name);
                intent.putExtra("ammeter3_id",datas.get(grouPposition).ammeter3.get(childPosition).building_ammeter3_id);
                startActivity(intent);
            }

            @Override
            public void onGroupButtonClick(int grouPposition) {
                Intent intent = new Intent(getActivity(),ZHBuildingEnergyActivity.class);
                intent.putExtra("id",datas.get(grouPposition).id);
                intent.putExtra("title",datas.get(grouPposition).title+"综合数据");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        getData();
    }

    private void getData() {
        api.getZongheBuilding()
                .compose(this.<ZHBuildingEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZHBuildingEntity>() {
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
                    public void onNext(ZHBuildingEntity zhBuildingEntity) {
                        binding.refreshLayout.setRefreshing(false);
                        datas.clear();
                        datas.addAll(zhBuildingEntity.info);
                        System.out.println(datas.get(0).ammeter3.size());

                        adapter.notifyDataSetChanged();
                        for (int i=0; i<datas.size(); i++) {
                            binding.recyclerView.expandGroup(i);
                        };
                    }
                });
    }

}
