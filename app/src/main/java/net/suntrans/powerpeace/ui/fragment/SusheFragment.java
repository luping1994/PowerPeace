package net.suntrans.powerpeace.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.databinding.FragmentSusheBinding;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 宿舍Fragment
 */
public class SusheFragment extends BasedFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FragmentSusheBinding binding;
    protected StateView stateView;

    private ArrayAdapter<String> susheAdapter;
    private ArrayAdapter<String> buildingAdapter;
    private ArrayAdapter<String> floorAdapter;
    private List<String> susheDatas;
    private List<String> buildingDatas;
    private List<String> floorDatas;

    private List<MenuBean.InfoBean> datas = new ArrayList<>();

    public SusheFragment() {
    }

    public static SusheFragment newInstance(String param1, String param2) {
        SusheFragment fragment = new SusheFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sushe, container, false);
        stateView = StateView.inject(binding.root);
        return binding.getRoot();
    }

    int buildingPosition =0;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        susheDatas = new ArrayList<>();
        buildingDatas = new ArrayList<>();
        floorDatas = new ArrayList<>();

        susheAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner,R.id.tv_spinner, susheDatas);
        buildingAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner,R.id.tv_spinner, buildingDatas);
        floorAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner,R.id.tv_spinner, floorDatas);

        binding.xueyuan.setAdapter(susheAdapter);
        binding.building.setAdapter(buildingAdapter);
        binding.floor.setAdapter(floorAdapter);

        binding.xueyuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                buildingPosition= position;
                buildingDatas.clear();
                for (int i=0;i<datas.get(position).sublist.size();i++){
                    buildingDatas.add(datas.get(position).sublist.get(i).building+"舍");
                }
                buildingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                floorDatas.clear();
                floorDatas.add("所有");
                for (int i=0;i<datas.get(buildingPosition).sublist.get(position).floors.size();i++){
                    floorDatas.add(datas.get(buildingPosition).sublist.get(position).floors.get(i).floor+"层");
                }
                floorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        LogUtil.i("第一次可见");
        getData();
    }

    private void getData(){
        stateView.showLoading();
        addSubscription(api.getThreeMenu(), new Subscriber<MenuBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                stateView.showRetry();
            }

            @Override
            public void onNext(MenuBean o) {
                stateView.showContent();
                LogUtil.i(o.message);
                susheDatas.clear();
                datas.clear();
                datas.addAll(o.info);
                for (MenuBean.InfoBean info :
                        datas) {
                    susheDatas.add(info.departmentName);
                }
                susheAdapter.notifyDataSetChanged();
            }
        });
    }
}
