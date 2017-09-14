package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.looney.fragments.DataPickerDialogFragment;
import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.PostageEntity;
import net.suntrans.powerpeace.databinding.ActivityPostageHisBinding;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.utils.StatusBarCompat;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.pgyersdk.c.a.f;
import static com.pgyersdk.c.a.m;

public class PostageHisActivity extends BasedActivity implements View.OnClickListener, DataPickerDialogFragment.OnDateSetChangerListener {

    private ActivityPostageHisBinding binding;
    private StateView stateView;
    private String date;
    private int mYear;
    private int mMonth;
    private int mDay;
    private List<PostageEntity.PostageInfo> datas;
    private List<PostageEntity.PostageInfo> copy;
    private Myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_postage_his);

        StatusBarCompat.compat(binding.headerView);


        binding.toolbar.setTitle(getIntent().getStringExtra("title") + "资费记录");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding.time.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        binding.year.setText(mYear + "年");
        binding.month.setText(mMonth + "");

        datas = new ArrayList<>();
        copy = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            boolean chongzhi = i % 2 == 0;
            PostageEntity.PostageInfo info = new PostageEntity.PostageInfo();
            if (!chongzhi) {
                info.money = "-" + i + ".00元";
                info.msg = "宿舍用电20.23度";
                info.type = "1";
                info.created_at = mYear + "年" + mMonth + "月" + mDay + "日";
                datas.add(info);
            } else {
                info.money = "+" + i + ".00元";
                info.msg = "王晓庆充值30元";
                info.type = "2";
                info.created_at = mYear + "年" + mMonth + "月" + mDay + "日";
                datas.add(info);
            }

        }
        copy.addAll(datas);
        adapter = new Myadapter(R.layout.item_postage_his, copy);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        binding.recyclerView.setAdapter(adapter);
        binding.radio1.setChecked(true);
        binding.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        copy.clear();
                        for (PostageEntity.PostageInfo info :
                                datas) {
                            if (info.type.equals("1")) {
                                copy.add(info);
                            }
                        }
                        break;
                    case R.id.radio1:
                        copy.clear();
                        copy.addAll(datas);
                        break;
                    case R.id.radio2:
                        copy.clear();
                        for (PostageEntity.PostageInfo info :
                                datas) {
                            if (info.type.equals("2")) {
                                copy.add(info);
                            }
                        }
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.time) {
            DataPickerDialogFragment fragment = (DataPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("dateDialogFragment");
            if (fragment == null) {
                fragment = new DataPickerDialogFragment();
                fragment.setListener(this);
            }
            fragment.show(getSupportFragmentManager(), "dateDialogFragment");
        }
    }

    @Override
    public void onDateSet(int year, int month, int day, String format) {
        date = format;
        binding.year.setText(year + "年");
        binding.month.setText(month + "");
        LogUtil.i(format);
    }

    class Myadapter extends BaseQuickAdapter<PostageEntity.PostageInfo, BaseViewHolder> {

        public Myadapter(@LayoutRes int layoutResId, @Nullable List<PostageEntity.PostageInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PostageEntity.PostageInfo item) {
            helper.setText(R.id.msg, item.msg);
            helper.setText(R.id.created_at, item.created_at);
            helper.setText(R.id.money, item.money);
        }
    }
}
