package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivityHelpBinding;
import net.suntrans.powerpeace.utils.StatusBarCompat;

/**
 * Created by Looney on 2017/9/13.
 */

public class HelpActivity extends BasedActivity {

    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("帮助中心");
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
