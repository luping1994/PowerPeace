package net.suntrans.powerpeace.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.FragmentAdapter;
import net.suntrans.powerpeace.databinding.FragmentSusheDetailBinding;

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
