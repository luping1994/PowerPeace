package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.FragmentAdapter;
import net.suntrans.powerpeace.bean.ZHBFloorEntity;
import net.suntrans.powerpeace.bean.ZHEnergyEntity;
import net.suntrans.powerpeace.databinding.ActivityZongheEnergyBinding;
import net.suntrans.powerpeace.databinding.ActivityZongheFloorBinding;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.ui.fragment.BasedFragment;
import net.suntrans.powerpeace.ui.fragment.ControlFragment;
import net.suntrans.powerpeace.ui.fragment.EleInfoFragment;
import net.suntrans.powerpeace.ui.fragment.StudentInfoFragment;
import net.suntrans.powerpeace.ui.fragment.ZhanghuFragment;
import net.suntrans.powerpeace.ui.fragment.ZongheFragmentPart1;
import net.suntrans.powerpeace.ui.fragment.ZongheFragmentPart2;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Looney on 2017/7/24.
 */

public class ZHFloorEnergyActivity extends BasedActivity implements BasedFragment.OnFragmentInteractionListener{
    private ActivityZongheEnergyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_zonghe_energy);

        StatusBarCompat.compat(binding.headerView);
        binding.toolbar.setTitle(getIntent().getStringExtra("title"));

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();


    }

    private void init() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        ZongheFragmentPart1 controlFragment = ZongheFragmentPart1.newInstace(getIntent().getStringExtra("floor_ammeter3_id"));
        ZongheFragmentPart2 controlFragment2 = ZongheFragmentPart2.newInstace(getIntent().getStringExtra("sno"));

        adapter.addFragment(controlFragment,getString(R.string.zhpart1_fragment_title));
        adapter.addFragment(controlFragment2,getString(R.string.zhpart2_fragment_title));

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(2);

        binding.tabs.setupWithViewPager(binding.viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jiance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void sendOrder(String s) {

    }
}
