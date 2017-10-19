package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivityFindPasswordBinding;
import net.suntrans.powerpeace.ui.fragment.FindPassFragment1;
import net.suntrans.powerpeace.ui.fragment.FindPassFragmentStep2;
import net.suntrans.powerpeace.ui.fragment.JihuoFragmentStep2;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import static android.R.attr.fragment;

/**
 * Created by Looney on 2017/9/24.
 */

public class FindPasswordActivity extends BasedActivity implements FindPassFragment1.FindPassFragment1Listener
        , FindPassFragmentStep2.FindPassFragmentListener {

    private ActivityFindPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle(R.string.verfied_shengfen);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FindPassFragment1 findPassFragment1 = new FindPassFragment1();
        getSupportFragmentManager().beginTransaction().replace(R.id.content,findPassFragment1,"FINDSTEP1")
                .commit();
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            // Lastly, it will rely on the system behavior for back
            super.onBackPressed();
        }
    }

    private void navitiveToNextFragment() {
        FindPassFragmentStep2 fragment = (FindPassFragmentStep2) getSupportFragmentManager().findFragmentByTag("FINDSTEP2");
        if (fragment == null) {
            fragment = new FindPassFragmentStep2();
        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.content, fragment, "FINDSTEP2");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onNextClicked() {
        navitiveToNextFragment();
    }

    @Override
    public void onJihuoClicked() {

    }

    @Override
    public void updateTitle(String title) {
        binding.toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                // Lastly, it will rely on the system behavior for back
                super.onOptionsItemSelected(item);
            }
        }
        return true;
    }
}
