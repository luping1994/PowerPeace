package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import net.suntrans.powerpeace.Constants;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivitySusheDetailBinding;
import net.suntrans.powerpeace.ui.fragment.BasedFragment;
import net.suntrans.powerpeace.ui.fragment.SusheDetailFragment;
import net.suntrans.powerpeace.utils.StatusBarCompat;

/**
 * Created by Looney on 2017/9/4.
 */

public class SusheDetailActivity extends BasedActivity implements BasedFragment.OnFragmentInteractionListener {

    private ActivitySusheDetailBinding binding;
    private String room_id;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sushe_detail);
        StatusBarCompat.compat(binding.headerView);//适配状态栏

        init();

        binding.toolbar.setTitle(getIntent().getStringExtra("title") + getString(R.string.detail_info));
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        room_id = getIntent().getStringExtra("room_id");
        SusheDetailFragment fragment = SusheDetailFragment.newInstance(room_id, String.valueOf(Constants.ROLE_ADMIN));
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();

    }


    private void init() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void sendOrder(String s) {

    }
}
