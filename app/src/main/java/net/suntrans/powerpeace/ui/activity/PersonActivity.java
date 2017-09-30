package net.suntrans.powerpeace.ui.activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.ApiHelper;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.LogInfoEntity;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.databinding.ActivityPersonalBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.fragment.ChangeNameFragment;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.suntrans.powerpeace.R.id.signOut;

/**
 * Created by Looney on 2017/9/14.
 */

public class PersonActivity extends BasedActivity implements View.OnClickListener, ChangeNameFragment.ChangeNameListener {
    private static final String TAG = "PersonActivity";
    private static final String FRG_TAG = "CHANGE_NAME";
    private int currentSelected;
    private ActivityPersonalBinding binding;
    private ApiHelper helper;
    private String username;
    private int role;

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


        username = App.getSharedPreferences().getString("username", "-1");
        role = App.getSharedPreferences().getInt("role", 1);

        binding.xueyuanRl.setOnClickListener(this);
        binding.nameRl.setOnClickListener(this);
        binding.phoneRl.setOnClickListener(this);
        binding.mima.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo(username, role + "");
    }

    @Override
    public void onClick(View v) {
        currentSelected = v.getId();
        ChangeNameFragment fragment = null;
        switch (v.getId()) {
            case R.id.xueyuanRl:
                return;
            case R.id.nameRl:
                return;
            case R.id.phoneRl:
                fragment = ChangeNameFragment.newInstance(ChangeNameFragment.TYPE_SINGLE_TEXT, "请输入新的手机号码");
                break;
            case R.id.mima:
                fragment = ChangeNameFragment.newInstance(ChangeNameFragment.TYPE_PASSWORD_TEXT, "请输入新密码");
                break;
        }
        fragment.setListener(this);
        fragment.show(getSupportFragmentManager(), FRG_TAG);
    }


    @Override
    public void changeName(String... name) {
        LogUtil.i(name[0] + "," + name[1] + "," + name[2]);
        switch (currentSelected) {
            case R.id.xueyuanRl:
                return;
            case R.id.nameRl:
                return;
            case R.id.phoneRl:
                if (!TextUtils.isEmpty(name[0]))
                    modifyPhone(name[0]);
                else
                    UiUtils.showToast("手机号不能为空");
                break;
            case R.id.mima:
                if (name[1].equals(name[2]))
                    modifyPassword(name[0], name[1]);
                else
                    UiUtils.showToast("两次输入的密码不一致");
                break;
        }
    }

    private void modifyPassword(String oldpass, String newPass) {
        addSubscription(api.changePassword(oldpass, newPass), new BaseSubscriber<ResultBody>(this) {
            @Override
            public void onNext(ResultBody resultBody) {
                super.onNext(resultBody);
                if (resultBody.code == 1) {
                    new AlertDialog.Builder(PersonActivity.this)
                            .setCancelable(false)
                            .setMessage("你已修改密码,需要重新登录")
                            .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    signOut();
                                }
                            }).create().show();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void modifyPhone(String phone) {
        if (!phone.matches("^(((13[0-9]{1})|(15[0-35-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\d{8})$")){
            UiUtils.showToast("手机号码格式不正确!");
            return;
        }
        addSubscription(api.changePhone(phone), new BaseSubscriber<ResultBody>(this) {
            @Override
            public void onNext(ResultBody resultBody) {
                super.onNext(resultBody);
                UiUtils.showToast("修改成功");
                getUserInfo(username, role + "");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getUserInfo(String userName, String role) {
        RetrofitHelper.getApi()
                .getUserInfo(userName, role)
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
//                        System.out.println(info.toString());
                        if (info.code == 1) {
                            binding.name.setText(info.info.get(0).name);
                            binding.phone.setText(info.info.get(0).telephone);
                            binding.xueyuan.setText(info.info.get(0).academy + "-" + info.info.get(0).major);
                        } else {

                        }
                    }
                });
    }

    private void signOut() {
        App.getSharedPreferences().edit()
                .putString("token", "-1")
                .commit();
        killAll();
        Intent intent = new Intent(this, Login1Activity.class);
        intent.putExtra(Login1Activity.EXTRA_TRANSITION, Login1Activity.TRANSITION_SLIDE_BOTTOM);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions transitionActivity = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, transitionActivity.toBundle());
        } else {
            startActivity(intent);
        }
    }
}
