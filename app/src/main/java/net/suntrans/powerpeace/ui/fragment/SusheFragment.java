package net.suntrans.powerpeace.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.bean.SusheEntity;
import net.suntrans.powerpeace.bean.SusheSelection;
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

    private ArrayAdapter<String> xueyuanAdapter;
    private ArrayAdapter<String> buildingAdapter;
    private ArrayAdapter<String> floorAdapter;
    private List<String> xueyuanMenuDatas;
    private List<String> buildingDatas;
    private List<String> floorDatas;

    private List<SusheSelection> susheDatas;


    private List<MenuBean.InfoBean> datas = new ArrayList<>();
    private MyAdapter adapter;

    private String currentXueyuan = "0";
    private String currentBuilding = "0";
    private String currentFloor = "0";

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

    int xueyuanPosition = 0;
    int buildingPostion = 0;
    int floorPostion = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        xueyuanMenuDatas = new ArrayList<>();
        buildingDatas = new ArrayList<>();
        floorDatas = new ArrayList<>();


        xueyuanAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, R.id.tv_spinner, xueyuanMenuDatas);
        buildingAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, R.id.tv_spinner, buildingDatas);
        floorAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, R.id.tv_spinner, floorDatas);

        binding.xueyuan.setAdapter(xueyuanAdapter);
        binding.building.setAdapter(buildingAdapter);
        binding.floor.setAdapter(floorAdapter);
        binding.xueyuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                xueyuanPosition = position;
                buildingDatas.clear();
                for (int i = 0; i < datas.get(position).sublist.size(); i++) {
                    buildingDatas.add(datas.get(position).sublist.get(i).building + "舍");
                }
                buildingAdapter.notifyDataSetChanged();
                currentXueyuan = datas.get(position).departmentID + "";
                if (binding.building.getSelectedItemPosition()==0){
                    floorDatas.clear();
                    floorDatas.add("所有");
                    for (int i = 0; i < datas.get(xueyuanPosition).sublist.get(0).floors.size(); i++) {
                        int floor = datas.get(xueyuanPosition).sublist.get(0).floors.get(i).floor;
                        floorDatas.add(floor + "层");
                    }
                    floorAdapter.notifyDataSetChanged();
                    binding.floor.setSelection(0);
                }else {
                    binding.building.setSelection(0);
                }


                getSusheDatas(currentXueyuan, datas.get(position).sublist.get(0).building + "", "0");

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                buildingPostion = position;
                floorDatas.clear();
                floorDatas.add("所有");
                for (int i = 0; i < datas.get(xueyuanPosition).sublist.get(position).floors.size(); i++) {
                    int floor = datas.get(xueyuanPosition).sublist.get(position).floors.get(i).floor;
                    floorDatas.add(floor + "层");
                }
                floorAdapter.notifyDataSetChanged();

                currentBuilding = datas.get(xueyuanPosition).sublist.get(position).building + "";

//              getSusheDatas(currentXueyuan, datas.get(position).sublist.get(0).building + "", currentFloor);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                floorPostion = position;
                if (position == 0) {
                    currentFloor = 0 + "";
                } else {
                    currentFloor = datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.get(position - 1).floor + "";
                }
//                getSusheDatas(currentXueyuan, currentBuilding, currentFloor);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getSusheDatas(currentXueyuan, currentBuilding, currentFloor);
            }
        });
        susheDatas = new ArrayList<>();
        adapter = new MyAdapter(R.layout.item_sushe, R.layout.item_suhe_header, susheDatas);
        binding.recyclerView.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSusheDatas(currentXueyuan,currentBuilding,currentFloor);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UiUtils.showToast("我被点击了"+position);
            }
        });
    }

    class MyAdapter extends BaseSectionQuickAdapter<SusheSelection, BaseViewHolder> {

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param layoutResId      The layout resource id of each item.
         * @param sectionHeadResId The section head layout id for each item
         * @param data             A new list is created out of this one to avoid mutable list
         */
        public MyAdapter(int layoutResId, int sectionHeadResId, List<SusheSelection> data) {
            super(layoutResId, sectionHeadResId, data);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, SusheSelection item) {
            helper.setText(R.id.headerName, item.header);
            helper.setText(R.id.name, item.susheName);
        }

        @Override
        protected void convert(BaseViewHolder helper, SusheSelection item) {
            helper.setText(R.id.name, item.susheName);
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        LogUtil.i("第一次可见");
        getMenuData();
    }

    private boolean isFristSelected = true;

    private void getMenuData() {
        stateView.showLoading();
        binding.recyclerView.setVisibility(View.INVISIBLE);
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
                LogUtil.i(o.message);

                if (o.info == null || o.info.size() == 0) {
                    throw new RuntimeException("菜单为空");
                }
                xueyuanMenuDatas.clear();
                datas.clear();
                datas.addAll(o.info);
                for (MenuBean.InfoBean info :
                        datas) {
                    xueyuanMenuDatas.add(info.departmentName);
                }
                xueyuanAdapter.notifyDataSetChanged();
                getSusheDatas(o.info.get(0).departmentID + "", o.info.get(0).sublist.get(0).building + "", 0 + "");
            }
        });
    }

    private void getSusheDatas(String departmentID, String building, String floor) {
        System.out.println(departmentID+"."+building+"."+floor);
        addSubscription(api.getSusheInfo(departmentID, building, floor), new Subscriber<SusheEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                stateView.showRetry();
                binding.refreshLayout.setRefreshing(false);

            }

            @Override
            public void onNext(SusheEntity o) {
                binding.refreshLayout.setRefreshing(false);
                if (o.info == null || o.info.size() == 0) {
                    stateView.showEmpty();
                    binding.recyclerView.setVisibility(View.INVISIBLE);
                    return;
                }


                susheDatas.clear();
                for (int i = 0; i < o.info.size(); i++) {
                    for (int j = 0; j < o.info.get(i).sublist.size(); j++) {
                        SusheSelection susheSelection =
                                new SusheSelection(j == 0 ? true : false, o.info.get(i).departmentName + "-"
                                        + o.info.get(i).building + "舍-" + o.info.get(i).floor + "层");
                        susheSelection.susheName = o.info.get(i).sublist.get(j).dormitory + "";
                        susheDatas.add(susheSelection);
                    }
                }
                binding.recyclerView.setVisibility(View.VISIBLE);
                stateView.showContent();
                adapter.notifyDataSetChanged();
            }
        });
    }
}
