package net.suntrans.powerpeace.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.ChannelAdapter;
import net.suntrans.powerpeace.api.ApiErrorCode;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.ChannelInfo;
import net.suntrans.powerpeace.bean.ChannelStatus;
import net.suntrans.powerpeace.bean.ControlBody;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.databinding.FragmentControlBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.activity.LogActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/10/6.
 */

public class ControlFragment extends BasedFragment {

    private FragmentControlBinding binding;
    private String room_id;
    private ChannelAdapter adapter;
    private StateView stateView;
    private Observable<ResultBody<List<ChannelInfo>>> getDataBody;
    private Observable<ResultBody<List<ChannelStatus>>> getStatusBody;

    public static ControlFragment newInstace(String room_id) {
        ControlFragment fragment = new ControlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("room_id", room_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_control, container, false);
        stateView = StateView.inject(binding.content);
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getData(room_id);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        datas = new ArrayList<>();
        room_id = getArguments().getString("room_id");
        adapter = new ChannelAdapter(getContext(), datas);
        adapter.setListener(new ChannelAdapter.onSwitchClickListener() {
            @Override
            public void onSwitchClick(int position) {
                if (position == -1) {
                    UiUtils.showToast(getString(R.string.tips_please_not_contrl_quickly));
                    return;
                }
                ControlBody controlBody = new ControlBody();
                controlBody.dev = "4100";
                controlBody.ac = "swh";
                controlBody.wp = 701;
                controlBody.chd = Integer.parseInt(datas.get(position).id);

                controlBody.num = Integer.parseInt(datas.get(position).num);
//                    controlBody.r = Integer.parseInt(param);
                controlBody.cmd = Integer.parseInt(datas.get(position).status.equals("0") ? "1" : "0");
                controlBody.addr = datas.get(position).addr;
                controlBody.mui = "0";
//                System.out.println(controlBody.toString());
                sendOrderToSwitch(controlBody);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(room_id);
            }
        });

        binding.rizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), LogActivity.class);
                intent2.putExtra("title", getActivity().getIntent().getStringExtra("title"));
                intent2.putExtra("room_id", room_id);
                startActivity(intent2);
            }
        });
        getData(room_id);
    }

    private List<ChannelInfo> datas;


    private LoadingDialog dialog;

    private void sendOrderToSwitch(ControlBody order) {
        if (dialog == null) {
            dialog = new LoadingDialog(getContext());
            dialog.setCancelable(false);
            dialog.setWaitText(getString(R.string.please_wait));
        }
        dialog.show();
        RetrofitHelper.getApi().control(getString(R.string.switch_code), order.addr + "",
                order.num + "", order.cmd + "")
                .compose(this.<ResultBody>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody>(getContext()) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        if (dialog != null)
                            dialog.dismiss();
                    }

                    @Override
                    public void onNext(ResultBody resultBody) {

                        if (resultBody.code == ApiErrorCode.OK) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getStatus(room_id);
                                }
                            }, 500);
                        } else {
                            UiUtils.showToast(resultBody.message);
                            if (dialog != null)
                                dialog.dismiss();
                        }
                    }
                });
    }

    private void getData(String room_id) {
        stateView.showLoading();
        binding.recyclerView.setVisibility(View.INVISIBLE);
        getDataBody = RetrofitHelper.getApi().getRoomChannel(room_id)
                .compose(this.<ResultBody<List<ChannelInfo>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        getDataBody.subscribe(new BaseSubscriber<ResultBody<List<ChannelInfo>>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
                if (dialog != null)
                    dialog.dismiss();
                stateView.showRetry();
                binding.recyclerView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(ResultBody<List<ChannelInfo>> listResultBody) {
                super.onNext(listResultBody);
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
                if (dialog != null)
                    dialog.dismiss();
                datas.clear();
                datas.addAll(listResultBody.info);
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
        getDataBody = RetrofitHelper.getApi().getRoomChannel(room_id)
                .compose(this.<ResultBody<List<ChannelInfo>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        getDataBody.subscribe(new BaseSubscriber<ResultBody<List<ChannelInfo>>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
                if (dialog != null)
                    dialog.dismiss();

            }

            @Override
            public void onNext(ResultBody<List<ChannelInfo>> listResultBody) {
                super.onNext(listResultBody);
                if (binding.refreshLayout != null)
                    binding.refreshLayout.setRefreshing(false);
                if (dialog != null)
                    dialog.dismiss();
                datas.clear();
                datas.addAll(listResultBody.info);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getStatus(String room_id) {

        RetrofitHelper.getApi().getChannelStatusOnly(room_id)
                .compose(this.<ResultBody<List<ChannelStatus>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<List<ChannelStatus>>>(getContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        if (binding.refreshLayout != null)
                            binding.refreshLayout.setRefreshing(false);
                        if (dialog != null)
                            dialog.dismiss();
                    }

                    @Override
                    public void onNext(ResultBody<List<ChannelStatus>> info) {
                        super.onNext(info);
                        if (binding.refreshLayout != null)
                            binding.refreshLayout.setRefreshing(false);
                        if (dialog != null)
                            dialog.dismiss();
                        if (info.info == null) {
                            UiUtils.showToast(getString(R.string.data_empty));
                            return;
                        }
                        for (int i = 0; i < datas.size(); i++) {
                            for (int j = 0; j < info.info.size(); j++) {
                                if (datas.get(i).num.equals(info.info.get(j).num)) {
                                    datas.get(i).status = info.info.get(j).status;
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();

                    }
                });
    }

    private Handler handler = new Handler();

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
