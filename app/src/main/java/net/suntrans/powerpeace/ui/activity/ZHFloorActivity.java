package net.suntrans.powerpeace.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.powerpeace.BuildConfig;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.FloorManagerInfo;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.bean.ZHBFloorEntity;
import net.suntrans.powerpeace.databinding.ActivityAboutBinding;
import net.suntrans.powerpeace.databinding.ActivityZongheFloorBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
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

public class ZHFloorActivity extends BasedActivity {
    private ActivityZongheFloorBinding binding;
    private List<ZHBFloorEntity.DataBean> datas;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_zonghe_floor);

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
        datas = new ArrayList<>();
        adapter = new MyAdapter(R.layout.item_zh_building, datas);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFloor();
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ZHFloorActivity.this,ZHFloorEnergyActivity.class);
                intent.putExtra("floor_ammeter3_id",datas.get(position).floor_ammeter3_id);
                intent.putExtra("sno",datas.get(position).sno);
                intent.putExtra("title",datas.get(position).ammeter3_place+"综合数据");
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends BaseQuickAdapter<ZHBFloorEntity.DataBean, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<ZHBFloorEntity.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ZHBFloorEntity.DataBean item) {
            helper.setText(R.id.name, item.floor);

        }
    }


    private void getFloor() {
        api.getZongheBuildingFloor(getIntent().getStringExtra("ammeter3_id"))
                .compose(this.<ZHBFloorEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZHBFloorEntity>() {
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
                    public void onNext(ZHBFloorEntity zhbFloorEntity) {
                        if (binding.refreshLayout != null)
                            binding.refreshLayout.setRefreshing(false);
                        datas.clear();
                        datas.addAll(zhbFloorEntity.data);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}
