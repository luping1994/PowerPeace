package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivityMsgCenBinding;
import net.suntrans.powerpeace.databinding.ActivityMsgDetailBinding;
import net.suntrans.powerpeace.utils.StatusBarCompat;

/**
 * Created by Looney on 2017/9/19.
 */

public class MsgDetailActivity extends BasedActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivityMsgDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_msg_detail);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle(R.string.title_msg_detail);
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        WebSettings settings = binding.webview.getSettings();

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

        String url = getIntent().getStringExtra("url");
        binding.webview.loadUrl(url);
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              binding.refreshLayout.setRefreshing(false);
            }
        },1600);
    }

    private Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.webview.removeAllViews();
        binding.webview.destroy();
        handler.removeCallbacksAndMessages(null);
    }
}
