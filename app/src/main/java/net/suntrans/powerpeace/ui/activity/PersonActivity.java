package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivityPersonalBinding;
import net.suntrans.powerpeace.utils.StatusBarCompat;

/**
 * Created by Looney on 2017/9/14.
 */

public class PersonActivity extends BasedActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPersonalBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_personal);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("个人资料");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.xueyuan.setOnClickListener(this);
        binding.name.setOnClickListener(this);
        binding.phone.setOnClickListener(this);
        binding.mima.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
