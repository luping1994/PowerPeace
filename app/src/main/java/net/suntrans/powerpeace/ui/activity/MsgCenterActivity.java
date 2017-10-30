package net.suntrans.powerpeace.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.MessageEntity;
import net.suntrans.powerpeace.databinding.ActivityMsgCenBinding;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Looney on 2017/9/19.
 */

public class MsgCenterActivity extends BasedActivity {

    private ActivityMsgCenBinding binding;
    private List<MessageEntity.Msg> datas;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_msg_cen);

        StatusBarCompat.compat(binding.headerView);

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        binding.toolbar.setTitle(R.string.title_msg_center);
        setSupportActionBar(binding.toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        datas = new ArrayList<>();
        adapter = new MsgAdapter(R.layout.item_msg, datas);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                System.out.println("onclick");
                Intent intent = new Intent(MsgCenterActivity.this, MsgDetailActivity.class);
                intent.putExtra("url",datas.get(position).url);
                startActivity(intent);
            }
        });

    }


    private Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        binding.refreshLayout.setRefreshing(true);
        addSubscription(api.getMessage(), new Subscriber<MessageEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                binding.refreshLayout.setRefreshing(false);
                binding.recyclerView.setVisibility(View.INVISIBLE);
                binding.tips.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(MessageEntity info) {
                datas.clear();
                datas.addAll(info.info);
                binding.refreshLayout.setRefreshing(false);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.tips.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private class MsgAdapter extends BaseQuickAdapter<MessageEntity.Msg, BaseViewHolder> {


        public MsgAdapter(int layoutResId, @Nullable List<MessageEntity.Msg> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MessageEntity.Msg item) {
            helper.setText(R.id.msg, item.title)
                    .setText(R.id.time, item.time);
        }
    }
}
