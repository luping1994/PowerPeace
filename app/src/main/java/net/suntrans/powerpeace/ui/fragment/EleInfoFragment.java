package net.suntrans.powerpeace.ui.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.EleInfo;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.databinding.FragmentEleInfoBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.activity.AmmeterHisActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.databinding.DataBindingUtil.inflate;


public class EleInfoFragment extends BasedFragment {
    private static final String ARG_PARAM1 = "room_id";
    private String room_id;
    private FragmentEleInfoBinding binding;
    private MyAdapter adapter;

    private StateView stateView;

    public EleInfoFragment() {
        // Required empty public constructor
    }

    public static EleInfoFragment newInstance(String room_id) {
        EleInfoFragment fragment = new EleInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, room_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            room_id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ele_info, container, false);
        stateView = StateView.inject(binding.content);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    private List<EleInfo> datas;

    private void init() {
        datas = new ArrayList<>();
        adapter = new MyAdapter(R.layout.item_ele_info, datas);
        binding.recyclerView.setAdapter(adapter);
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(room_id);
            }
        });
        binding.recyclerView.addItemDecoration(new DefaultDecoration());

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent2 = new Intent(getActivity(), AmmeterHisActivity.class);
                intent2.putExtra("title", getActivity().getIntent().getStringExtra("title"));
                intent2.putExtra("paramName", datas.get(position).name);
                intent2.putExtra("code", datas.get(position).code);
                intent2.putExtra("room_id", room_id);
                startActivity(intent2);
            }
        });

        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getData(room_id);
            }
        });
        getData(room_id);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class MyAdapter extends BaseQuickAdapter<EleInfo, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<EleInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EleInfo item) {
            helper.setText(R.id.name, item.name)
                    .setText(R.id.value, item.value + item.unit);
            ImageView imageView = helper.getView(R.id.image);
            if (item.imgId != 0)
                imageView.setImageResource(item.imgId);
        }
    }

    private void getData(String room_id) {
        stateView.showLoading();
        binding.recyclerView.setVisibility(View.INVISIBLE);
        RetrofitHelper.getApi().getEleInfo(room_id)
                .compose(this.<ResultBody<List<EleInfo>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<List<EleInfo>>>(getContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (binding.refreshLayout != null) {
                            binding.refreshLayout.setRefreshing(false);
                        }
                        e.printStackTrace();
                        stateView.showRetry();
                        binding.recyclerView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(ResultBody<List<EleInfo>> info) {
                        if (binding.refreshLayout != null) {
                            binding.refreshLayout.setRefreshing(false);
                        }
                        if (info != null) {
                            if (info.info != null) {
                                datas.clear();

                                for (int i = 0; i < info.info.size(); i++) {
                                    if (info.info.get(i).code.equals("100001")) {
                                        info.info.get(i).imgId = R.drawable.ic_dianya;
                                    }
                                    if (info.info.get(i).code.equals("100002")) {
                                        info.info.get(i).imgId = R.drawable.ic_dianliu;
                                    }
                                    if (info.info.get(i).code.equals("100003")) {
                                        info.info.get(i).imgId = R.drawable.ic_gonglv;
                                    }
                                    if (info.info.get(i).code.equals("100004")) {
                                        info.info.get(i).imgId = R.drawable.ic_gonglvyinsu;
                                    }
                                    if (info.info.get(i).code.equals("100005")) {
                                        info.info.get(i).imgId = R.drawable.ic_dl;
                                    }
                                }
                                datas.addAll(info.info);
                            } else {
                                UiUtils.showToast("数据为空");

                            }
                        } else {
                            UiUtils.showToast("数据为空");
                        }

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

    private void refreshData(String room_id) {
        addSubscription(api.getEleInfo(room_id), new BaseSubscriber<ResultBody<List<EleInfo>>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onNext(ResultBody<List<EleInfo>> info) {
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
                if (info != null) {
                    if (info.info != null) {
                        datas.clear();

                        for (int i = 0; i < info.info.size(); i++) {
                            if (info.info.get(i).code.equals("100001")) {
                                info.info.get(i).imgId = R.drawable.ic_dianya;
                            }
                            if (info.info.get(i).code.equals("100002")) {
                                info.info.get(i).imgId = R.drawable.ic_dianliu;
                            }
                            if (info.info.get(i).code.equals("100003")) {
                                info.info.get(i).imgId = R.drawable.ic_gonglv;
                            }
                            if (info.info.get(i).code.equals("100004")) {
                                info.info.get(i).imgId = R.drawable.ic_gonglvyinsu;
                            }
                            if (info.info.get(i).code.equals("100005")) {
                                info.info.get(i).imgId = R.drawable.ic_dl;
                            }
                        }
                        datas.addAll(info.info);
                        adapter.notifyDataSetChanged();
                    } else {
                        UiUtils.showToast("数据为空");

                    }
                } else {
                    UiUtils.showToast("数据为空");
                }
            }
        });
    }

}
