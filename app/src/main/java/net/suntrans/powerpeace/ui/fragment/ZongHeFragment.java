package net.suntrans.powerpeace.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.ListDropDownAdapter;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.bean.ZongheEntity;
import net.suntrans.powerpeace.bean.ZongheSelection;
import net.suntrans.powerpeace.databinding.FragmentZongheBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 综合Fragment
 */

public class ZongHeFragment extends BasedFragment {


    private static final String TAG = "ZongHeFragment";
    private FragmentZongheBinding binding;
//    protected StateView stateView;


    private List<View> popupViews = new ArrayList<>();

    private List<String> xueyuanMenuDatas;
    private List<String> buildingDatas;
    private List<String> floorDatas;

    private ListDropDownAdapter xueyuanAdapter;
    private ListDropDownAdapter buildingAdapter;
    private ListDropDownAdapter floorAdapter;


    private List<MenuBean.InfoBean> datas = new ArrayList<>();
    private MyAdapter adapter;
    private String[] headers;

    private int mRefreshType = STATE_VIEW_REFRESH;
    private static final int SWIP_REFRESH_LAYOUT = 0x01;
    private static final int STATE_VIEW_REFRESH = 0x02;

    private List<ZongheSelection> adapterDatas = new ArrayList<>();
    private Map<String, String> zidian;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zonghe, container, false);
        return binding.getRoot();
    }

    int xueyuanPosition = 0;
    int buildingPostion = 0;
    int floorPostion = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initZidian();

        xueyuanMenuDatas = new ArrayList<>();
        buildingDatas = new ArrayList<>();
        floorDatas = new ArrayList<>();


        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshType = SWIP_REFRESH_LAYOUT;
                getData();
            }
        });

        adapter = new MyAdapter(R.layout.item_zonghe, R.layout.item_zonghe_header, adapterDatas);
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        initSpinner();
    }

    private void initZidian() {
        zidian = new HashMap<>();
        zidian.put("aV_value", "A相电压");
        zidian.put("bV_value", "B相电压");
        zidian.put("cV_value", "C相电压");

        zidian.put("aA_value", "A相电流");
        zidian.put("bA_value", "B相电流");
        zidian.put("cA_value", "C相电流");

        zidian.put("aP_value", "有功功率");
        zidian.put("rP_value", "无功功率");
        zidian.put("E_value", "总用电量");
        zidian.put("E_day_value", "本日用电量");
        zidian.put("E_month_value", "本月用电量");
    }

    class MyAdapter extends BaseSectionQuickAdapter<ZongheSelection, BaseViewHolder> {

        public MyAdapter(int layoutResId, int sectionHeadResId, List<ZongheSelection> data) {
            super(layoutResId, sectionHeadResId, data);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, ZongheSelection item) {
            helper.setText(R.id.name, item.name);
            helper.setText(R.id.value, item.value);
            helper.setText(R.id.headerName,item.header);
        }

        @Override
        protected void convert(BaseViewHolder helper, ZongheSelection item) {
            helper.setText(R.id.name, item.name);
            helper.setText(R.id.value, item.value);
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        getMenuData();
    }


    private void getMenuData() {

        addSubscription(api.getThreeMenu(), new BaseSubscriber<MenuBean>(getActivity()) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onNext(MenuBean o) {
                LogUtil.i(o.message);

                if (o.info == null || o.info.size() == 0) {
                    throw new RuntimeException("菜单为空");
                }
                if (xueyuanMenuDatas != null)
                    xueyuanMenuDatas.clear();
                datas.clear();
                datas.addAll(o.info);

                for (MenuBean.InfoBean info :
                        datas) {
                    xueyuanMenuDatas.add(info.departmentName);


                }

                List<MenuBean.InfoBean.SublistBeanX> fristbuildings = datas.get(0).sublist;
                for (MenuBean.InfoBean.SublistBeanX build :
                        fristbuildings) {
                    buildingDatas.add(build.building_name);

                }
                List<MenuBean.InfoBean.SublistBeanX.SublistBean> fristFloors = datas.get(0).sublist.get(0).floors;
//                floorDatas.add("所有");
                for (MenuBean.InfoBean.SublistBeanX.SublistBean floor :
                        fristFloors) {
                    floorDatas.add(floor.floor_name);
                }

                xueyuanAdapter.notifyDataSetChanged();
                buildingAdapter.notifyDataSetChanged();
//                floorAdapter.notifyDataSetChanged();
                headers = new String[]{xueyuanMenuDatas.get(0), buildingDatas.get(0)};
                binding.headerMenu.setDropDownMenu(Arrays.asList(headers), popupViews, binding.refreshLayout);

                getZonngheDatas(o.info.get(0).departmentID + "", o.info.get(0).sublist.get(0).building + "", o.info.get(0).sublist.get(0).floors.get(0).floor + "");
            }
        });
    }

    private void getZonngheDatas(String departmentID, String building, String floor) {
        LogUtil.i(TAG, departmentID + "." + building + "." + floor);

        addSubscription(api.getZongheData(departmentID, building,floor), new BaseSubscriber<ZongheEntity>(getActivity()) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                if (mRefreshType == STATE_VIEW_REFRESH) {

                } else if (mRefreshType == SWIP_REFRESH_LAYOUT) {
                    binding.refreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onNext(ZongheEntity data) {
//                System.out.println(data.toString());
//                binding.refreshLayout.setRefreshing(false);
//
//                Map<String, String> map = data.info.get(0);
//                int i = 0;
//                adapterDatas.clear();
//                for (Map.Entry<String, String> entry :
//                        map.entrySet()) {
//                    ZongheSelection da = null;
//                    if (i == 0)
//                        da = new ZongheSelection(true, "信息学院2舍");
//                    else
//                        da = new ZongheSelection(false, "信息学院2舍");
//                    i++;
//
//                    da.name = zidian.get(entry.getKey());
//                    da.value = entry.getValue();
//                    adapterDatas.add(da);
//                }
//
//                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initSpinner() {
        final ListView xueyuanView = new ListView(getContext());
        xueyuanAdapter = new ListDropDownAdapter(getContext(), xueyuanMenuDatas);
        xueyuanView.setDividerHeight(0);
        xueyuanView.setAdapter(xueyuanAdapter);


        final ListView buildView = new ListView(getContext());
        buildingAdapter = new ListDropDownAdapter(getContext(), buildingDatas);
        buildView.setDividerHeight(0);
        buildView.setAdapter(buildingAdapter);

        popupViews.add(xueyuanView);
        popupViews.add(buildView);

        xueyuanView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (xueyuanPosition == position) {
                    binding.headerMenu.closeMenu();
                    return;
                }

                binding.headerMenu.setTabText(xueyuanMenuDatas.get(position));
                buildingDatas.clear();
                for (int i = 0; i < datas.get(position).sublist.size(); i++) {
                    buildingDatas.add(datas.get(position).sublist.get(i).building_name);
                }
                binding.headerMenu.setTabText(2, buildingDatas.get(0));
                buildingAdapter.notifyDataSetChanged();
                binding.headerMenu.closeMenu();

                xueyuanPosition = position;
                buildingPostion = 0;
                floorPostion = 0;


                //更新楼层spinner
                floorDatas.clear();
                floorDatas.add("所有");
                for (int i = 0; i < datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.size(); i++) {
                    floorDatas.add(datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.get(i).floor_name);
                }
                binding.headerMenu.setTabText(4, "所有");

                xueyuanAdapter.setCheckItem(position);
                buildingAdapter.setCheckItem(0);
                floorAdapter.setCheckItem(0);


                mRefreshType = STATE_VIEW_REFRESH;
                getZonngheDatas(datas.get(xueyuanPosition).departmentID + "",
                        datas.get(xueyuanPosition).sublist.get(buildingPostion).building + "", "0");
            }
        });

        buildView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (buildingPostion == position) {
                    binding.headerMenu.closeMenu();
                    return;
                }
                binding.headerMenu.setTabText(buildingDatas.get(position));
                floorDatas.clear();
                floorDatas.add("所有");
                for (int i = 0; i < datas.get(xueyuanPosition).sublist.get(position).floors.size(); i++) {
                    floorDatas.add(datas.get(xueyuanPosition).sublist.get(position).floors.get(i).floor_name);
                }
//                binding.headerMenu.setTabText(4, "所有");
                binding.headerMenu.closeMenu();

                buildingPostion = position;
                floorPostion = 0;


                xueyuanAdapter.setCheckItem(position);
                buildingAdapter.setCheckItem(position);
//                floorAdapter.setCheckItem(0);

                mRefreshType = STATE_VIEW_REFRESH;
                getZonngheDatas(datas.get(xueyuanPosition).departmentID + "",
                        datas.get(xueyuanPosition).sublist.get(buildingPostion).building + "",
                        "0");
            }
        });

    }

    private void getData() {
        if (datas.size() == 0) {
            getMenuData();
            return;
        }
//        String floorParm = "0";
//        if (floorPostion == 0) {
//            floorParm = "0";
//        } else {
//            floorParm = datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.get(floorPostion - 1).floor + "";
//        }
        getZonngheDatas(datas.get(xueyuanPosition).departmentID + "",
                datas.get(xueyuanPosition).sublist.get(buildingPostion).building + "","");
    }

}
