package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.pgyersdk.feedback.PgyFeedbackShakeManager;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.databinding.ActivityFeedbackBinding;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/9/14.
 */

public class FeedbackActivity extends BasedActivity implements View.OnClickListener{

    private int currentSelected;
    private ActivityFeedbackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("反馈中心");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyFeedbackShakeManager.unregister();
    }

    private void getUserInfo(String userName) {
        RetrofitHelper.getApi()
                .getUserInfo(userName)
                .compose(this.<UserInfoEntity>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserInfoEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast("连接服务器出错了,登录失败");

                    }

                    @Override
                    public void onNext(UserInfoEntity info) {
                        System.out.println(info.toString());

                    }
                });
    }


}
