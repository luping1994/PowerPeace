package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivityJihuoBinding;
import net.suntrans.powerpeace.ui.fragment.JihuoFragmentStep1;
import net.suntrans.powerpeace.ui.fragment.JihuoFragmentStep2;
import net.suntrans.powerpeace.utils.StatusBarCompat;

public class JihuoActivity extends BasedActivity implements JihuoFragmentStep1.JihuoFragment1Listener
,JihuoFragmentStep2.JihuoFragment2Listener{

    private ActivityJihuoBinding binding;
    private JihuoFragmentStep2 fragment;
    private JihuoFragmentStep1 fragmentStep1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jihuo);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jihuo);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle(R.string.title_jihuo);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        fragmentStep1 = (JihuoFragmentStep1) getSupportFragmentManager().findFragmentByTag("STEP1");
        if (fragmentStep1==null){
            fragmentStep1 = new JihuoFragmentStep1();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.content, fragmentStep1, "STEP1");
//        transaction.addToBackStack(null);
        transaction.commit();
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

    private void navitiveToNextFragment(){
        fragment = (JihuoFragmentStep2) getSupportFragmentManager().findFragmentByTag("STEP2");
        if (fragment == null) {
            fragment = new JihuoFragmentStep2();
        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.content, fragment, "STEP2");
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
