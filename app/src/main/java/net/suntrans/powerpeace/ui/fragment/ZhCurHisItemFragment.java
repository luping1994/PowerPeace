package net.suntrans.powerpeace.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.HisEntity;
import net.suntrans.powerpeace.databinding.FragmentItemZhCurHisBinding;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/10/23.
 * Des:
 */

public class ZhCurHisItemFragment extends Fragment {

    FragmentItemZhCurHisBinding binding;
    private MyAdapter adapter;
    private List<HisEntity.EleParmHisItem> datas = new ArrayList<>();

    public static ZhCurHisItemFragment newInstance(ArrayList<HisEntity.EleParmHisItem> datas) {
        ZhCurHisItemFragment fragment = new ZhCurHisItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("datas", datas);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_zh_cur_his, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = getArguments().getParcelableArrayList("datas");
        adapter = new MyAdapter(R.layout.item_his, datas);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
    }


    class MyAdapter extends BaseQuickAdapter<HisEntity.EleParmHisItem, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<HisEntity.EleParmHisItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HisEntity.EleParmHisItem item) {
            helper.setText(R.id.value, item.data == null ? "0.00" : item.data);
            helper.setText(R.id.time, item.created_at == null ? "0.00" : item.created_at);
        }
    }

    public void setData(List<HisEntity.EleParmHisItem> data) {

        datas.clear();
//        if (mDisplayType == DISPLAY_WEEK) {
//            datas.addAll(hisEntity.week_data);
//        } else if (mDisplayType == DISPLAY_MONTH) {
//            datas.addAll(hisEntity.month_data);
//        } else if (mDisplayType == DISPLAY_DAY) {
//            datas.addAll(hisEntity.day_data);
//        }
        datas.addAll(data);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}
