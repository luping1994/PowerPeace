package net.suntrans.powerpeace.ui.fragment;


import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.EleInfo;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.bean.StudentInfo;
import net.suntrans.powerpeace.databinding.FragmentEleInfoBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.activity.AmmeterHisActivity;
import net.suntrans.powerpeace.ui.activity.StudentInfoActivity;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.List;


public class StudentInfoFragment extends BasedFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "room_id";
    // TODO: Rename and change types of parameters
    private String room_id;
    private FragmentEleInfoBinding binding;
    private MyAdapter adapter;
    private StateView stateView;


    public StudentInfoFragment() {
        // Required empty public constructor
    }

    public static StudentInfoFragment newInstance(String room_id) {
        StudentInfoFragment fragment = new StudentInfoFragment();
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

    private List<StudentInfo> datas;

    private void init() {
        datas = new ArrayList<>();
        adapter = new MyAdapter(R.layout.item_ele_info, datas);
        binding.recyclerView.setAdapter(adapter);
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFreshData(room_id);
            }
        });
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent1 = new Intent(getActivity(), StudentInfoActivity.class);
                intent1.putExtra("name", datas.get(position).name);
                intent1.putExtra("studentID", datas.get(position).studentID);
                startActivity(intent1);
            }
        });

        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getData(room_id);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(room_id);
    }

    private class MyAdapter extends BaseQuickAdapter<StudentInfo, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<StudentInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, StudentInfo item) {
            helper.setText(R.id.name, item.name)
                    .setText(R.id.value, item.academy);
            ImageView imageView = helper.getView(R.id.image);
                imageView.setImageResource(R.drawable.ic_person);
        }
    }

    private void getData(String room_id) {
        stateView.showLoading();
        binding.recyclerView.setVisibility(View.INVISIBLE);
        addSubscription(api.getStuInfo(room_id), new BaseSubscriber<ResultBody<List<StudentInfo>>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
                stateView.showRetry();
                binding.recyclerView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(ResultBody<List<StudentInfo>> info) {
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
                if (info != null) {
                    if (info.info != null) {
                        datas.clear();
                        datas.addAll(info.info);
                    } else {
                        UiUtils.showToast(getString(R.string.data_empty));

                    }
                } else {
                    UiUtils.showToast(getString(R.string.data_empty));
                }
                if (datas.size()==0){
                    stateView.showEmpty();
                    binding.recyclerView.setVisibility(View.INVISIBLE);
                }else {
                    stateView.showContent();
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void reFreshData(String room_id) {
        addSubscription(api.getStuInfo(room_id), new BaseSubscriber<ResultBody<List<StudentInfo>>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onNext(ResultBody<List<StudentInfo>> info) {
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
                if (info != null) {
                    if (info.info != null) {
                        datas.clear();
                        datas.addAll(info.info);
                        adapter.notifyDataSetChanged();
                    } else {
                        UiUtils.showToast(getString(R.string.data_empty));

                    }
                } else {
                    UiUtils.showToast(getString(R.string.data_empty));
                }
            }
        });
    }

}
