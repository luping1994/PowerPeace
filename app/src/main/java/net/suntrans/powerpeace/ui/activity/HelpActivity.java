package net.suntrans.powerpeace.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.suntrans.powerpeace.MainActivity;
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

        binding.toolbar.setTitle("帮助与反馈");
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        WebSettings settings = binding.webview.getSettings();
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                System.out.println("我被执行了");
                binding.webview.reload();
            }
        });
        settings.setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        binding.webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //get the newProgress and refresh progress bar
                binding.progressbar.setProgress(newProgress);
                if (newProgress==100){
                    binding.progressbar.setVisibility(View.GONE);
                }
            }
        });

        binding.webview.loadUrl("http://g.suntrans.net:8088/SuntransTest-Peace/help/");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fankui,menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webview.canGoBack()) {
            binding.webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (binding.webview.canGoBack()) {
                binding.webview.goBack();// 返回前一个页面
                return true;
            }else {
                finish();
            }
        }
        if (item.getItemId() == R.id.feedback){
            Intent intent = new Intent();
            intent.setClass(this, FeedbackActivity.class);
            startActivity(intent);

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.webview.destroy();
    }
}
