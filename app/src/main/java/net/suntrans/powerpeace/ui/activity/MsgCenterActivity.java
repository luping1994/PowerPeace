package net.suntrans.powerpeace.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.EditView;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.Constants;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.MessageCenter;
import net.suntrans.powerpeace.bean.MessageEntity;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.databinding.ActivityMsgCenBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
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
    private List<MessageCenter.Msg> datas;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_msg_cen);

        StatusBarCompat.compat(binding.headerView);


        binding.toolbar.setTitle(R.string.title_msg_center);
        setSupportActionBar(binding.toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        datas = new ArrayList<>();

        binding.recyclerView.addItemDecoration(new DefaultDecoration());


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
        getDataPrimiry();
    }

    private void getDataPrimiry() {
        page = 1;
        datas.clear();
        adapter = null;
        adapter = new MsgAdapter(R.layout.item_msg, datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                int role = App.getSharedPreferences().getInt("role", -1);
                if (role != Constants.ROLE_ADMIN && role != Constants.ROLE_SCHOOL_LEADER) {
                    return;
                }

                new AlertDialog.Builder(MsgCenterActivity.this)
                        .setMessage("是否删除")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(datas.get(position).id + "");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }).create().show();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, binding.recyclerView);
        binding.recyclerView.setAdapter(adapter);
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int role = App.getSharedPreferences().getInt("role", -1);
        if (role == Constants.ROLE_ADMIN || role == Constants.ROLE_SCHOOL_LEADER) {
            getMenuInflater().inflate(R.menu.menu_add, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            startActivity(new Intent(this, MsgAddActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int page = 1;

    private void getData() {
//        binding.refreshLayout.setRefreshing(true);
        addSubscription(RetrofitHelper.getApi().getNoticeIndex(page + ""), new BaseSubscriber<MessageCenter>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
//                binding.refreshLayout.setRefreshing(false);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.tips.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(MessageCenter info) {
                datas.addAll(info.info.lists);
                if (info.info.lists == null || info.info.lists.size() == 0) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.loadMoreComplete();
                    page++;
                }
//                binding.refreshLayout.setRefreshing(false);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.tips.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void delete(String id) {
        addSubscription(RetrofitHelper.getApi().deleteNotice(id), new BaseSubscriber<ResultBody>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                super.onError(e);
            }

            @Override
            public void onNext(ResultBody info) {
                UiUtils.showToast(info.message);
                getDataPrimiry();
            }
        });
    }


    private class MsgAdapter extends BaseQuickAdapter<MessageCenter.Msg, BaseViewHolder> {

        public MsgAdapter(int layoutResId, @Nullable List<MessageCenter.Msg> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MessageCenter.Msg item) {
            helper.setText(R.id.msg, item.message)
                    .setText(R.id.time, item.created_at);
        }
    }
}
