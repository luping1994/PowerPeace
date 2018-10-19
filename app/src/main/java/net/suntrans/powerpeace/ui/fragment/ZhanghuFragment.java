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

import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.AccountInfo;
import net.suntrans.powerpeace.bean.ChannelInfo;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.databinding.FragmentZhanghuBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.activity.AmmeterHisActivity;
import net.suntrans.powerpeace.ui.activity.ElecHisActivity;
import net.suntrans.powerpeace.ui.activity.PostageHisActivity;

import java.util.List;

/**
 * Created by Looney on 2017/10/6.
 */

public class ZhanghuFragment extends BasedFragment implements View.OnClickListener {

    private FragmentZhanghuBinding binding;
    private String room_id;

    public static ZhanghuFragment newInstace(String room_id){
        ZhanghuFragment fragment = new ZhanghuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("room_id",room_id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zhanghu,container,false);
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
        room_id = getArguments().getString("room_id");
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(room_id);
            }
        });
        binding.balansLL.setOnClickListener(this);
        binding.dayuseLL.setOnClickListener(this);
        binding.monthuseLL.setOnClickListener(this);
        binding.totaluseLL.setOnClickListener(this);
        binding.statusLL.setOnClickListener(this);
        getData(room_id);
    }

    private List<ChannelInfo> datas;


    private LoadingDialog dialog;


    private void getData(String room_id) {
        addSubscription(RetrofitHelper.getApi().getAccountInfo(room_id),new BaseSubscriber<ResultBody<AccountInfo>>(getContext()){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                if (binding.refreshLayout!=null)
                    binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(ResultBody<AccountInfo> info) {
                super.onNext(info);
                if (binding.refreshLayout!=null)
                    binding.refreshLayout.setRefreshing(false);
                binding.time.setText(info.updated_at);

                refreshLayout(info.info);
            }
        });
    }

    private void refreshLayout(AccountInfo info) {
        binding.dayuse.setText(info.getDayuse()==null?"--":info.getDayuse()+"kW·h");
        binding.balans.setText(info.getBalans()==null?"--":info.getBalans()+getString(R.string.unit_rmb));
        binding.monthuse.setText(info.getMonthuse()==null?"--":info.getMonthuse()+"kW·h");
        binding.status.setText(info.getStatus()==null?"--":info.getStatus().equals("正常")?getString(R.string.item_normal):info.getStatus());
        binding.totaluse.setText(info.getTotaluse()==null?"--":info.getTotaluse()+"kW·h");
    }


    private Handler handler = new Handler();

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.statusLL)
            return;
        if (v.getId()==R.id.monthuseLL
                || v.getId()==R.id.dayuseLL
                || v.getId() == R.id.totaluseLL) {
            Intent intent3 = new Intent(getActivity(), ElecHisActivity.class);
            intent3.putExtra("title", getActivity().getIntent().getStringExtra("title"));
            intent3.putExtra("paramName", getString(R.string.item_yongdianliang));
            intent3.putExtra("room_id", room_id);
            startActivity(intent3);
        } else if (v.getId()==R.id.balansLL) {
            Intent intent3 = new Intent(getActivity(), PostageHisActivity.class);
            intent3.putExtra("title", getActivity().getIntent().getStringExtra("title"));
            intent3.putExtra("paramName", getString(R.string.item_yongdianliang));
            intent3.putExtra("room_id", room_id);
            startActivity(intent3);
        }
    }
}
