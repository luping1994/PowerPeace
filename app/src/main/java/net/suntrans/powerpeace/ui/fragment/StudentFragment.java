package net.suntrans.powerpeace.ui.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.ListDropDownAdapter;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.bean.StudentInfoEntity;
import net.suntrans.powerpeace.bean.StudentSelection;
import net.suntrans.powerpeace.databinding.FragmentStudentBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.activity.StudentInfoActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 学生Fragment
 */
public class StudentFragment extends BasedFragment {


    private static final java.lang.String TAG = "StudentFragment";
    private FragmentStudentBinding binding;
    protected StateView stateView;


    private List<View> popupViews = new ArrayList<>();

    private List<String> xueyuanMenuDatas;
    private List<String> buildingDatas;
    private List<String> floorDatas;

    private ListDropDownAdapter xueyuanAdapter;
    private ListDropDownAdapter buildingAdapter;
    private ListDropDownAdapter floorAdapter;

    private List<StudentSelection> susheDatas;


    private List<MenuBean.InfoBean> datas = new ArrayList<>();
    private MyAdapter adapter;
    private String[] headers;

    private int mRefreshType = STATE_VIEW_REFRESH;
    private static final int SWIP_REFRESH_LAYOUT = 0x01;
    private static final int STATE_VIEW_REFRESH = 0x02;


    public StudentFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student, container, false);
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
        adapter = new MyAdapter(R.layout.item_student, R.layout.item_student_header, susheDatas);
        binding.recyclerView.setAdapter(adapter);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshType = SWIP_REFRESH_LAYOUT;
                getData();
            }
        });
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent1 = new Intent(getContext(), StudentInfoActivity.class);
                intent1.putExtra("name", susheDatas.get(position).susheName);
                intent1.putExtra("studentID", susheDatas.get(position).studentID);
                startActivity(intent1);
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        initSpinner();
    }

    class MyAdapter extends BaseSectionQuickAdapter<StudentSelection, BaseViewHolder> {

        public MyAdapter(int layoutResId, int sectionHeadResId, List<StudentSelection> data) {
            super(layoutResId, sectionHeadResId, data);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, StudentSelection item) {
            helper.setText(R.id.headerName, item.header);
            helper.setText(R.id.name, item.susheName);
            helper.setText(R.id.academy, item.academy);
        }

        @Override
        protected void convert(BaseViewHolder helper, StudentSelection item) {
            helper.setText(R.id.name, item.susheName);
            helper.setText(R.id.academy, item.academy);
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
        addSubscription(api.getThreeMenu(), new BaseSubscriber<MenuBean>(getActivity()) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                stateView.showRetry();
            }

            @Override
            public void onNext(MenuBean o) {
                LogUtil.i(o.message);

                if (o.info == null || o.info.size() == 0) {
                    throw new RuntimeException("menu is empty");
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
                floorAdapter.notifyDataSetChanged();
                headers = new String[]{xueyuanMenuDatas.get(0), buildingDatas.get(0), floorDatas.get(0)};
                binding.headerMenu.setDropDownMenu(Arrays.asList(headers), popupViews, binding.root);

                getStudentDatas(o.info.get(0).departmentID + "", o.info.get(0).sublist.get(0).building + "", o.info.get(0).sublist.get(0).floors.get(0).floor + "");
            }
        });
    }

    private void getStudentDatas(String departmentID, String building, String floor) {
        LogUtil.i(TAG, departmentID + "." + building + "." + floor);
        if (mRefreshType == STATE_VIEW_REFRESH) {
            stateView.showLoading();
        } else if (mRefreshType == SWIP_REFRESH_LAYOUT) {

        }
        addSubscription(api.getStudent(departmentID, building, floor), new BaseSubscriber<StudentInfoEntity>(getActivity()) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                if (mRefreshType == STATE_VIEW_REFRESH) {
                    stateView.showRetry();
                } else if (mRefreshType == SWIP_REFRESH_LAYOUT) {
                    binding.refreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onNext(StudentInfoEntity o) {

                if (o.info == null || o.info.size() == 0) {
                    if (mRefreshType == STATE_VIEW_REFRESH) {
                        stateView.showEmpty();
                        binding.recyclerView.setVisibility(View.INVISIBLE);
                    } else if (mRefreshType == SWIP_REFRESH_LAYOUT) {
                        binding.refreshLayout.setRefreshing(false);
                    }
                } else {
                    susheDatas.clear();
                    for (int i = 0; i < o.info.size(); i++) {
                        for (int j = 0; j < o.info.get(i).sublist.size(); j++) {
                            StudentSelection studentSelection =
                                    new StudentSelection(j == 0 ? true : false, o.info.get(i).departmentName + "-"
                                             + o.info.get(i).dormitory + "");
                            studentSelection.susheName = o.info.get(i).sublist.get(j).name + "";
                            studentSelection.academy = o.info.get(i).sublist.get(j).academy + "";
                            studentSelection.studentID = o.info.get(i).sublist.get(j).studentID + "";
                            susheDatas.add(studentSelection);
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
                }

                adapter.notifyDataSetChanged();
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
//                floorDatas.add("所有");
                for (int i = 0; i < datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.size(); i++) {
                    floorDatas.add(datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.get(i).floor_name);
                }
                binding.headerMenu.setTabText(4, floorDatas.get(0));
//                floorAdapter.notifyDataSetChanged();

                xueyuanAdapter.setCheckItem(position);
                buildingAdapter.setCheckItem(0);
                floorAdapter.setCheckItem(0);

                mRefreshType = STATE_VIEW_REFRESH;
                getData();
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
//                floorDatas.add("所有");
                for (int i = 0; i < datas.get(xueyuanPosition).sublist.get(position).floors.size(); i++) {
                    floorDatas.add(datas.get(xueyuanPosition).sublist.get(position).floors.get(i).floor_name);
                }
                binding.headerMenu.setTabText(4, floorDatas.get(0));
//                floorAdapter.notifyDataSetChanged();
                binding.headerMenu.closeMenu();

                buildingPostion = position;
                floorPostion = 0;


                xueyuanAdapter.setCheckItem(position);
                buildingAdapter.setCheckItem(position);
                floorAdapter.setCheckItem(0);

                mRefreshType = STATE_VIEW_REFRESH;
                getData();
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
                getData();

            }
        });
    }

    private void getData() {
        if (datas.size() == 0) {
            getMenuData();
        } else {
            getStudentDatas(datas.get(xueyuanPosition).departmentID + "",
                    datas.get(xueyuanPosition).sublist.get(buildingPostion).building + "",
                    datas.get(xueyuanPosition).sublist.get(buildingPostion).floors.get(floorPostion).floor + "");
        }

    }

}
