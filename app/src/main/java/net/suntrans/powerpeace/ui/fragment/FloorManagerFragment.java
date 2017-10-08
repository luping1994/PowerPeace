package net.suntrans.powerpeace.ui.fragment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.FloorManagerInfo;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.databinding.FragmentFloorManagerBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by Looney on 2017/10/5.
 */

public class FloorManagerFragment extends BasedFragment {
    FragmentFloorManagerBinding binding;
    private MyAdapter adapter;
    private List<FloorManagerInfo> datas;
    private StateView stateView;

    public static FloorManagerFragment newInstance(String floor_id){
        FloorManagerFragment floorManagerFragment = new FloorManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("floor_id",floor_id);
        floorManagerFragment.setArguments(bundle);
        return floorManagerFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_floor_manager, container, false);
        stateView = StateView.inject(binding.content);
        return binding.getRoot();
    }

    private String floor_id;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        datas = new ArrayList<>();
        adapter = new MyAdapter(R.layout.item_floor_manager, datas);
        floor_id = getArguments().getString("floor_id");
        binding.recyclerView.setAdapter(adapter);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.open) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("是否打开该层开关?")
                            .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switchFloor("1", datas.get(position).id, datas.get(position).alpha);
                                }
                            }).setNegativeButton(R.string.qvxiao, null).create().show();

                } else if (view.getId() == R.id.close) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("是否关闭该层开关?")
                            .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switchFloor("0", datas.get(position).id, datas.get(position).alpha);

                                }
                            }).setNegativeButton(R.string.qvxiao, null).create().show();
                }
            }
        });
        binding.quankai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.is_open_all)
                        .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchFloor("1", floor_id, "0");
                            }
                        }).setNegativeButton(R.string.qvxiao, null).create().show();
            }
        });
        binding.quanguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.is_close_all)
                        .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchFloor("0", floor_id, "0");
                            }
                        }).setNegativeButton(R.string.qvxiao, null).create().show();
            }
        });
    }

    private class MyAdapter extends BaseQuickAdapter<FloorManagerInfo, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<FloorManagerInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FloorManagerInfo item) {
            helper.setText(R.id.floor, item.title);
            helper.addOnClickListener(R.id.open)
                    .addOnClickListener(R.id.close);
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
        addSubscription(api.getManagerFloorInfo(floor_id), new BaseSubscriber<ResultBody<List<FloorManagerInfo>>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
                stateView.showRetry();
                binding.refreshLayout.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }

            @Override
            public void onNext(ResultBody<List<FloorManagerInfo>> resultBody) {
                super.onNext(resultBody);
                if (datas == null) {
                    datas = new ArrayList<FloorManagerInfo>();
                }
//                System.out.println(resultBody.message);
//                System.out.println(resultBody.info.size());
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

            }
        });
    }

    private void refreshData() {
        addSubscription(api.getManagerFloorInfo(floor_id), new BaseSubscriber<ResultBody<List<FloorManagerInfo>>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
                e.printStackTrace();
            }

            @Override
            public void onNext(ResultBody<List<FloorManagerInfo>> resultBody) {
                super.onNext(resultBody);
                if (datas == null) {
                    datas = new ArrayList<FloorManagerInfo>();
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
        addSubscription(api.switchFloor(map), new BaseSubscriber<ResultBody>(getContext()) {
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
