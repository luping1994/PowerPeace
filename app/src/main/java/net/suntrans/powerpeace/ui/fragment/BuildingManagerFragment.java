package net.suntrans.powerpeace.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.DividerItemDecoration;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.BuildingResult;
import net.suntrans.powerpeace.bean.FloorManagerInfo;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.databinding.FragmentBuildingManagerBinding;
import net.suntrans.powerpeace.databinding.FragmentFloorManagerBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.activity.BuildingDetailActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.suntrans.powerpeace.R.id.recyclerView;

/**
 * Created by Looney on 2017/10/5.
 */

public class BuildingManagerFragment extends BasedFragment {
    FragmentBuildingManagerBinding binding;
    private MyAdapter adapter;
    private List<BuildingResult.Building> datas;
    private StateView stateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_building_manager, container, false);
        stateView = StateView.inject(binding.content);
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getFloor();
            }
        });
        return binding.getRoot();
    }

    private String floor_id;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        datas = new ArrayList<>();
        adapter = new MyAdapter(R.layout.item_building_manager, datas);
        floor_id = App.getSharedPreferences().getString("floor_id", "-1");
        binding.recyclerView.setAdapter(adapter);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("floor_id",datas.get(position).id);
                intent.putExtra("title",datas.get(position).title);
                intent.setClass(getActivity(), BuildingDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    private class MyAdapter extends BaseQuickAdapter<BuildingResult.Building, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<BuildingResult.Building> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BuildingResult.Building item) {
            helper.setText(R.id.name, item.title);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getFloor();
    }

    private void getFloor() {
        stateView.showLoading();
        binding.recyclerView.setVisibility(View.INVISIBLE);
//        System.out.println("getFloor");
        addSubscription(RetrofitHelper.getApi().getBuildings(), new BaseSubscriber<BuildingResult>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
                stateView.showRetry();
                binding.recyclerView.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }

            @Override
            public void onNext(BuildingResult resultBody) {
                super.onNext(resultBody);
                if (datas == null) {
                    datas = new ArrayList<BuildingResult.Building>();
                }
//                System.out.println(resultBody.message);
//                System.out.println(resultBody.info.size());
                datas.clear();
                datas.addAll(resultBody.info);

                if (datas.size() == 0) {
                    stateView.showRetry();
                    binding.recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    stateView.showContent();
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void refreshData() {
        addSubscription(RetrofitHelper.getApi().getBuildings(), new BaseSubscriber<BuildingResult>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
                e.printStackTrace();
            }

            @Override
            public void onNext(BuildingResult resultBody) {
                super.onNext(resultBody);
                if (datas == null) {
                    datas = new ArrayList<BuildingResult.Building>();
                }
                datas.clear();
                datas.addAll(resultBody.info);
                if (datas.size() == 0) {
                    stateView.showEmpty();
                    binding.recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    stateView.showContent();
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                binding.refreshLayout.setRefreshing(false);

            }
        });
    }

    private void switchFloor(String cmd, String id, String alpha) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("cmd", cmd);
        map.put("alpha", alpha);
        addSubscription(RetrofitHelper.getApi().switchFloor(map), new BaseSubscriber<ResultBody>(getContext()) {
            @Override
            public void onNext(ResultBody resultBody) {
                super.onNext(resultBody);
                UiUtils.showToast(resultBody.message);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


}
