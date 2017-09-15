package net.suntrans.powerpeace;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;

import net.suntrans.powerpeace.databinding.ActivityStuMainBinding;
import net.suntrans.powerpeace.ui.activity.BasedActivity;
import net.suntrans.powerpeace.ui.fragment.SusheDetailFragment;
import net.suntrans.powerpeace.utils.StatusBarCompat;

/**
 * Created by Looney on 2017/9/15.
 */

public class StudentMainActivity extends BasedActivity {

    private ActivityStuMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stu_main);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("我的宿舍");

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawer.setDrawerListener(toggle);
        toggle.syncState();
        String room_id = App.getSharedPreferences().getString("room_id","-1");
        SusheDetailFragment fragment =SusheDetailFragment.newInstance(room_id, Constants.ADMIN);
        getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment).commit();

    }
}
