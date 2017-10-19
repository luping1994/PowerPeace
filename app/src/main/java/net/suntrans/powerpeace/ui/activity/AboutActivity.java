package net.suntrans.powerpeace.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.suntrans.powerpeace.BuildConfig;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.databinding.ActivityAboutBinding;
import net.suntrans.powerpeace.utils.StatusBarCompat;


/**
 * Created by Looney on 2017/7/24.
 */

public class AboutActivity extends BasedActivity {
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        StatusBarCompat.compat(binding.headerView);
        binding.toolbar.setTitle(R.string.activity_title_about);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.version.setText(BuildConfig.VERSION_NAME);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void share(String desc) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_download_url));
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, desc));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            share(getString(R.string.title_share_app));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
