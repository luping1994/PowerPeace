package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivityFindPasswordBinding;
import net.suntrans.powerpeace.utils.StatusBarCompat;

/**
 * Created by Looney on 2017/9/24.
 */

public class FindPasswordActivity extends BasedActivity {

    private ActivityFindPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("找回密码");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
