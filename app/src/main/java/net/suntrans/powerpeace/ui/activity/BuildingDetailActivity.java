package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivityBuildDetailBinding;
import net.suntrans.powerpeace.ui.fragment.BasedFragment;
import net.suntrans.powerpeace.ui.fragment.FloorManagerFragment;
import net.suntrans.powerpeace.utils.StatusBarCompat;


/**
 * Created by Looney on 2017/7/24.
 */

public class BuildingDetailActivity extends BasedActivity implements BasedFragment.OnFragmentInteractionListener {
    private ActivityBuildDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_build_detail);

        StatusBarCompat.compat(binding.headerView);
        binding.toolbar.setTitle(getIntent().getStringExtra("title"));

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        FloorManagerFragment floorManagerFragment = FloorManagerFragment.newInstance(getIntent().getStringExtra("floor_id"));
        getSupportFragmentManager().beginTransaction().replace(R.id.content,floorManagerFragment).commitAllowingStateLoss();
    }


    @Override
    public void sendOrder(String s) {

    }
}
