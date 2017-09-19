package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.ApiHelper;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.LogInfoEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.databinding.ActivityPersonalBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.fragment.ChangeNameFragment;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/9/14.
 */

public class PersonActivity extends BasedActivity implements View.OnClickListener, ChangeNameFragment.ChangeNameListener {
    private static final String TAG = "PersonActivity";
    private static final String FRG_TAG = "CHANGE_NAME";
    private int currentSelected;
    private ActivityPersonalBinding binding;
    private ApiHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle("个人资料");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.xueyuan.setOnClickListener(this);
        binding.name.setOnClickListener(this);
        binding.phone.setOnClickListener(this);
        binding.mima.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        currentSelected = v.getId();
        ChangeNameFragment fragment = null;
        switch (v.getId()) {
            case R.id.xueyuan:
                return;
            case R.id.name:
                return;
            case R.id.phone:
                fragment = ChangeNameFragment.newInstance(ChangeNameFragment.TYPE_SINGLE_TEXT, "请输入新的手机号码");

                break;
            case R.id.mima:
                fragment = ChangeNameFragment.newInstance(ChangeNameFragment.TYPE_PASSWORD_TEXT, "请输入新密码");
                break;
        }
        fragment.show(getSupportFragmentManager(), FRG_TAG);
    }


    @Override
    public void changeName(String... name) {
        System.out.println(name[0] + "," + name[1] + "," + name[2]);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getUserInfo(String userName,String role) {
        RetrofitHelper.getApi()
                .getUserInfo(userName,role)
                .compose(this.<UserInfoEntity>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<UserInfoEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(UserInfoEntity info) {
                        System.out.println(info.toString());
                        if (info.code == 1) {

                        } else {

                        }
                    }
                });
    }
}
