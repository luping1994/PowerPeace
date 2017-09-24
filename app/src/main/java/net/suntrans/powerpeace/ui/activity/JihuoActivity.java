package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivityJihuoBinding;
import net.suntrans.powerpeace.utils.StatusBarCompat;

public class JihuoActivity extends AppCompatActivity {

    private ActivityJihuoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jihuo);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jihuo);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("激活账号");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
