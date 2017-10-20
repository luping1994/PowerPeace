package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.SearchAdapter;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.SearchInfoEntity;
import net.suntrans.powerpeace.databinding.ActivitySearchBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/9/27.
 */

public class SearchActivity extends BasedActivity {

    private ActivitySearchBinding binding;
    private List<SearchInfoEntity.SearchInfo> datas;
    private SearchAdapter adapter;
    private Subscription subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle(R.string.title_search);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init() {
        datas = new ArrayList<>();
        adapter = new SearchAdapter(datas, this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        };
        binding.searchText.addTextChangedListener(watcher);

    }

    private void search(String text) {
        if (TextUtils.isEmpty(text)) {
            datas.clear();
            adapter.notifyDataSetChanged();
            binding.tips.setVisibility(View.VISIBLE);
            binding.content.setVisibility(View.INVISIBLE);
            return;
        }
        binding.recyclerView.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.stopScroll();
        if (subscribe!=null){
            subscribe.unsubscribe();
        }
        subscribe = RetrofitHelper.getApi().search(text)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<SearchInfoEntity>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        datas.clear();
                        adapter.notifyDataSetChanged();
                        binding.tips.setVisibility(View.VISIBLE);
                        binding.content.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(SearchInfoEntity searchInfoEntity) {
                        super.onNext(searchInfoEntity);
                        binding.progressBar.setVisibility(View.INVISIBLE);

                        List<SearchInfoEntity.SearchInfo> studentinfo = searchInfoEntity.studentinfo;
                        List<SearchInfoEntity.SearchInfo> roominfo = searchInfoEntity.roominfo;
                        for (SearchInfoEntity.SearchInfo info :
                                studentinfo) {
                            info.type = SearchAdapter.SEARCH_STU;
                        }
                        for (SearchInfoEntity.SearchInfo info :
                                roominfo) {
                            info.type = SearchAdapter.SEARCH_ROOM;
                        }
                        datas.clear();
                        datas.addAll(studentinfo);
                        datas.addAll(roominfo);
                        if (datas.size() == 0) {
                            binding.tips.setVisibility(View.VISIBLE);
                            binding.content.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tips.setVisibility(View.INVISIBLE);
                            binding.content.setVisibility(View.VISIBLE);
                        }
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        adapter.setStuCount(studentinfo.size());
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        if(subscribe!=null){
            if (!subscribe.isUnsubscribed()){
                subscribe.unsubscribe();
            }
        }

        super.onDestroy();

    }
}
