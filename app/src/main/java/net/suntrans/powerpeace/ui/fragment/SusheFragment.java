package net.suntrans.powerpeace.ui.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.ListDropDownAdapter;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.bean.SusheEntity;
import net.suntrans.powerpeace.bean.SusheSelection;
import net.suntrans.powerpeace.databinding.FragmentSusheBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.activity.SusheDetailActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 宿舍Fragment
 */
public class SusheFragment extends BasedFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final java.lang.String TAG = "SusheFragment";


    private FragmentSusheBinding binding;
    protected StateView stateView;

    private List<View> popupViews = new ArrayList<>();

    private List<String> xueyuanMenuDatas;
    private List<String> buildingDatas;
    private List<String> floorDatas;

    private ListDropDownAdapter xueyuanAdapter;
    private ListDropDownAdapter buildingAdapter;
    private ListDropDownAdapter floorAdapter;

    private List<SusheSelection> susheDatas;


    private List<MenuBean.InfoBean> datas = new ArrayList<>();
    private MyAdapter adapter;
    private String[] headers;


    private int mRefreshType = STATE_VIEW_REFRESH;
    private static final int SWIP_REFRESH_LAYOUT = 0x01;
    private static final int STATE_VIEW_REFRESH = 0x02;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                mRefreshType = STATE_VIEW_REFRESH;
                getData();
            }
        });
        susheDatas = new ArrayList<>();
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        adapter = new MyAdapter(R.layout.item_sushe, R.layout.item_suhe_header, susheDatas);
        binding.recyclerView.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshType = SWIP_REFRESH_LAYOUT;
                getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                UiUtils.showToast("我被点击了" + position);
                Intent intent = new Intent(getActivity(), SusheDetailActivity.class);
                intent.putExtra("title", susheDatas.get(position).susheName);
                intent.putExtra("room_id", susheDatas.get(position).room_id);
                intent.putExtra("whole_name", susheDatas.get(position).wholeName);
                startActivity(intent);

            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        initSpinner();
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

        final ListView floorView = new ListView(getContext());
        floorAdapter = new ListDropDownAdapter(getContext(), floorDatas);
        floorView.setDividerHeight(0);
        floorView.setAdapter(floorAdapter);

        popupViews.add(xueyuanView);
        popupViews.add(buildView);
        popupViews.add(floorView);

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
                floorDatas.add("全部楼层");
                for (int i = 0; i < datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.size(); i++) {
                    floorDatas.add(datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.get(i).floor_name);
                }
                binding.headerMenu.setTabText(4, "全部楼层");
//                floorAdapter.notifyDataSetChanged();

                xueyuanAdapter.setCheckItem(position);
                buildingAdapter.setCheckItem(0);
                floorAdapter.setCheckItem(0);


                mRefreshType = STATE_VIEW_REFRESH;
                getSusheDatas(datas.get(xueyuanPosition).departmentID + "",
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
                floorDatas.add("全部楼层");
                for (int i = 0; i < datas.get(xueyuanPosition).sublist.get(position).floors.size(); i++) {
                    floorDatas.add(datas.get(xueyuanPosition).sublist.get(position).floors.get(i).floor_name);
                }
                binding.headerMenu.setTabText(4, "全部楼层");
//                floorAdapter.notifyDataSetChanged();
                binding.headerMenu.closeMenu();

                buildingPostion = position;
                floorPostion = 0;


                xueyuanAdapter.setCheckItem(position);
                buildingAdapter.setCheckItem(position);
                floorAdapter.setCheckItem(0);

                mRefreshType = STATE_VIEW_REFRESH;
                getSusheDatas(datas.get(xueyuanPosition).departmentID + "",
                        datas.get(xueyuanPosition).sublist.get(buildingPostion).building + "",
                        "0");
            }
        });

        floorView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (floorPostion == position) {
                    binding.headerMenu.closeMenu();
                    return;
                }

                floorAdapter.setCheckItem(position);
                binding.headerMenu.setTabText(floorDatas.get(position));

                floorPostion = position;

                binding.headerMenu.closeMenu();


                mRefreshType = STATE_VIEW_REFRESH;
                if (position == 0) {
                    getSusheDatas(datas.get(xueyuanPosition).departmentID + "",
                            datas.get(xueyuanPosition).sublist.get(buildingPostion).building + "",
                            "0");
                } else {
                    getSusheDatas(datas.get(xueyuanPosition).departmentID + "",
                            datas.get(xueyuanPosition).sublist.get(buildingPostion).building + "",
                            datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.get(floorPostion - 1).floor + "");
                }

            }
        });
    }

    class MyAdapter extends BaseSectionQuickAdapter<SusheSelection, BaseViewHolder> {


        public MyAdapter(int layoutResId, int sectionHeadResId, List<SusheSelection> data) {
            super(layoutResId, sectionHeadResId, data);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, SusheSelection item) {
            helper.setText(R.id.headerName, item.header);
            helper.setText(R.id.name, item.susheName);
            ImageView view = helper.getView(R.id.image);
//            DrawableCompat.setTint(view.getDrawable(), getContext().getResources().getColor(R.color.colorPrimary));

            helper.setText(R.id.status, item.status.equals("0") ? "已锁定" : "正常");
            if (item.status.equals("0")) {
                helper.setTextColor(R.id.status, Color.RED);
            } else {
                helper.setTextColor(R.id.status, Color.GRAY);
            }
        }

        @Override
        protected void convert(BaseViewHolder helper, SusheSelection item) {
            ImageView view = helper.getView(R.id.image);
//            DrawableCompat.setTint(view.getDrawable(), getContext().getResources().getColor(R.color.colorPrimary));

            helper.setText(R.id.name, item.susheName);
            helper.setText(R.id.status, item.status.equals("0") ? "已锁定" : "正常");
            if (item.status.equals("0")) {
                helper.setTextColor(R.id.status, Color.RED);
            } else {
                helper.setTextColor(R.id.status, Color.GRAY);
            }

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
        RetrofitHelper.getApi().getThreeMenu()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<MenuBean>(getActivity()){
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        stateView.showRetry();
                    }

                    @Override
                    public void onNext(MenuBean o) {
                        super.onNext(o);
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
                        floorDatas.add("全部楼层");
                        for (MenuBean.InfoBean.SublistBeanX.SublistBean floor :
                                fristFloors) {
                            floorDatas.add(floor.floor_name);
                        }

                        xueyuanAdapter.notifyDataSetChanged();
                        buildingAdapter.notifyDataSetChanged();
                        floorAdapter.notifyDataSetChanged();
                        headers = new String[]{xueyuanMenuDatas.get(0), buildingDatas.get(0), "全部楼层"};
                        binding.headerMenu.setDropDownMenu(Arrays.asList(headers), popupViews, binding.root);

                        getSusheDatas(o.info.get(0).departmentID + "", o.info.get(0).sublist.get(0).building + "", "0");
                    }
                });
//        addSubscription(RetrofitHelper.getApi().getThreeMenu(), new BaseSubscriber<MenuBean>(getActivity()) {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                e.printStackTrace();
//                stateView.showRetry();
//            }
//
//            @Override
//            public void onNext(MenuBean o) {
////                LogUtil.i(o.message);
//
//                if (o.info == null || o.info.size() == 0) {
//                    throw new RuntimeException("菜单为空");
//                }
//                if (xueyuanMenuDatas != null)
//                    xueyuanMenuDatas.clear();
//                datas.clear();
//                datas.addAll(o.info);
//
//                for (MenuBean.InfoBean info :
//                        datas) {
//                    xueyuanMenuDatas.add(info.departmentName);
//
//                }
//
//                List<MenuBean.InfoBean.SublistBeanX> fristbuildings = datas.get(0).sublist;
//                for (MenuBean.InfoBean.SublistBeanX build :
//                        fristbuildings) {
//                    buildingDatas.add(build.building_name);
//
//                }
//                List<MenuBean.InfoBean.SublistBeanX.SublistBean> fristFloors = datas.get(0).sublist.get(0).floors;
//                floorDatas.add("全部楼层");
//                for (MenuBean.InfoBean.SublistBeanX.SublistBean floor :
//                        fristFloors) {
//                    floorDatas.add(floor.floor_name);
//                }
//
//                xueyuanAdapter.notifyDataSetChanged();
//                buildingAdapter.notifyDataSetChanged();
//                floorAdapter.notifyDataSetChanged();
//                headers = new String[]{xueyuanMenuDatas.get(0), buildingDatas.get(0), "全部楼层"};
//                binding.headerMenu.setDropDownMenu(Arrays.asList(headers), popupViews, binding.root);
//
//                getSusheDatas(o.info.get(0).departmentID + "", o.info.get(0).sublist.get(0).building + "", "0");
//            }
//        });
    }

    private void getSusheDatas(String departmentID, String building, String floor) {
        LogUtil.i(TAG, departmentID + "." + building + "." + floor);
        if (mRefreshType == STATE_VIEW_REFRESH) {
            stateView.showLoading();
        } else if (mRefreshType == SWIP_REFRESH_LAYOUT) {

        }
        RetrofitHelper.getApi().getSusheInfo(departmentID, building, floor)
                .compose(this.<SusheEntity>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<SusheEntity>(getActivity()){

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        stateView.showRetry();
                        binding.refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(SusheEntity o) {
                        super.onNext(o);
                        if (o.info == null || o.info.size() == 0 ) {
                            if (mRefreshType == STATE_VIEW_REFRESH) {
                                stateView.showEmpty();
                                binding.recyclerView.setVisibility(View.INVISIBLE);
                            } else {
                                binding.refreshLayout.setRefreshing(false);
                            }
                            return;
                        }

                        susheDatas.clear();
                        for (int i = 0; i < o.info.size(); i++) {
                            for (int j = 0; j < o.info.get(i).sublist.size(); j++) {
                                SusheSelection susheSelection =
                                        new SusheSelection(j == 0 ? true : false, o.info.get(i).departmentName + "-"
                                                + o.info.get(i).building_name + "-" + o.info.get(i).floor_name + "");
                                susheSelection.susheName = o.info.get(i).sublist.get(j).dormitory + "";
                                susheSelection.room_id = o.info.get(i).sublist.get(j).room_id + "";
                                susheSelection.wholeName = o.info.get(i).departmentName + "-"
                                        + o.info.get(i).building + "舍-" + o.info.get(i).sublist.get(j).dormitory;
                                susheSelection.status = o.info.get(i).sublist.get(j).status;
                                susheDatas.add(susheSelection);
                            }
                        }
                        binding.refreshLayout.setRefreshing(false);
                        if (susheDatas.size()==0){
                            stateView.showEmpty();
                            binding.recyclerView.setVisibility(View.INVISIBLE);
                        }else {
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            stateView.showContent();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
//        addSubscription(RetrofitHelper.getApi().getSusheInfo(departmentID, building, floor), new BaseSubscriber<SusheEntity>(getActivity()) {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                e.printStackTrace();
//                stateView.showRetry();
//                binding.refreshLayout.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onNext(SusheEntity o) {
//                if (o.info == null || o.info.size() == 0 ) {
//                    if (mRefreshType == STATE_VIEW_REFRESH) {
//                        stateView.showEmpty();
//                        binding.recyclerView.setVisibility(View.INVISIBLE);
//                    } else {
//                        binding.refreshLayout.setRefreshing(false);
//                    }
//                    return;
//                }
//
//                susheDatas.clear();
//                for (int i = 0; i < o.info.size(); i++) {
//                    for (int j = 0; j < o.info.get(i).sublist.size(); j++) {
//                        SusheSelection susheSelection =
//                                new SusheSelection(j == 0 ? true : false, o.info.get(i).departmentName + "-"
//                                        + o.info.get(i).building_name + "-" + o.info.get(i).floor_name + "");
//                        susheSelection.susheName = o.info.get(i).sublist.get(j).dormitory + "";
//                        susheSelection.room_id = o.info.get(i).sublist.get(j).room_id + "";
//                        susheSelection.wholeName = o.info.get(i).departmentName + "-"
//                                + o.info.get(i).building + "舍-" + o.info.get(i).sublist.get(j).dormitory;
//                        susheSelection.status = o.info.get(i).sublist.get(j).status;
//                        susheDatas.add(susheSelection);
//                    }
//                }
//                binding.refreshLayout.setRefreshing(false);
//                if (susheDatas.size()==0){
//                    stateView.showEmpty();
//                    binding.recyclerView.setVisibility(View.INVISIBLE);
//                }else {
//                    binding.recyclerView.setVisibility(View.VISIBLE);
//                    stateView.showContent();
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
    }

    private void getData() {
        if (datas.size() == 0) {
            getMenuData();
            return;
        }
        String floorParm = "0";
        if (floorPostion == 0) {
            floorParm = "0";
        } else {
            floorParm = datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.get(floorPostion - 1).floor + "";
        }
        getSusheDatas(datas.get(xueyuanPosition).departmentID + "",
                datas.get(xueyuanPosition).sublist.get(buildingPostion).building + "", floorParm);
    }

}
