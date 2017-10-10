package net.suntrans.powerpeace.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle.android.FragmentEvent;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.FragmentAdapter;
import net.suntrans.powerpeace.api.ApiErrorCode;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.ControlBody;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.bean.RoomInfoSelection;
import net.suntrans.powerpeace.databinding.FragmentSusheDetailBinding;
import net.suntrans.powerpeace.ui.activity.AmmeterHisActivity;
import net.suntrans.powerpeace.ui.activity.LogActivity;
import net.suntrans.powerpeace.ui.activity.PostageHisActivity;
import net.suntrans.powerpeace.ui.activity.StudentInfoActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/9/13.
 */

public class SusheDetailFragment extends BasedFragment {

    private FragmentSusheDetailBinding binding;

    public static SusheDetailFragment newInstance(String param, String role) {
        SusheDetailFragment fragment = new SusheDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param", param);
        bundle.putString("role", role);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sushe_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        String room_id = getArguments().getString("param");

        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager());
        ControlFragment controlFragment = ControlFragment.newInstace(room_id);
        ZhanghuFragment zhanghuFragment = ZhanghuFragment.newInstace(room_id);
        EleInfoFragment eleInfoFragment = EleInfoFragment.newInstance(room_id);
        StudentInfoFragment studentInfoFragment = StudentInfoFragment.newInstance(room_id);
        adapter.addFragment(controlFragment,getString(R.string.ele_state));
        adapter.addFragment(zhanghuFragment,getString(R.string.account_info));
        adapter.addFragment(eleInfoFragment,getString(R.string.ele_info));
        adapter.addFragment(studentInfoFragment,getString(R.string.str_info));

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(4);

        binding.tabs.setupWithViewPager(binding.viewPager);
    }



}
