package net.suntrans.powerpeace;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import net.suntrans.powerpeace.adapter.FragmentAdapter;
import net.suntrans.powerpeace.databinding.ActivityMainBinding;
import net.suntrans.powerpeace.ui.activity.BasedActivity;
import net.suntrans.powerpeace.ui.fragment.StudentFragment;
import net.suntrans.powerpeace.ui.fragment.SusheFragment;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends BasedActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.toolbar.setTitle("登录");
        binding.toolbar.setNavigationIcon(R.drawable.ic_settings);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        init();
    }

    private void init() {
        binding.viewpager.setOffscreenPageLimit(2);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        SusheFragment fragment = new SusheFragment();
        StudentFragment fragment2 = new StudentFragment();
        adapter.addFragment(fragment,"SuShe");
        adapter.addFragment(fragment2,"Student");
        binding.viewpager.setAdapter(adapter);
        binding.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.radio0) {
                    binding.viewpager.setCurrentItem(0, false);
                }
                if (checkedId == R.id.radio1) {
                    binding.viewpager.setCurrentItem(1, false);
                }
                if (checkedId == R.id.radio2) {
                    binding.viewpager.setCurrentItem(2, false);

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            return true;
        }
        if (item.getItemId() == android.R.id.home){
            return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}
