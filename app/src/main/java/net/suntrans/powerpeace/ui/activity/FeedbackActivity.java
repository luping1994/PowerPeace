package net.suntrans.powerpeace.ui.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.databinding.ActivityFeedbackBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/9/14.
 */

public class FeedbackActivity extends BasedActivity implements View.OnClickListener {

    private int currentSelected;
    private ActivityFeedbackBinding binding;
    private int mCheckedTypeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle(R.string.title_feedback);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

//        binding.commit.setOnClickListener(this);

        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                mCheckedTypeId = checkedId;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.commit) {
            commitSuggestion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private LoadingDialog dialog;

    @Override
    public void onClick(View v) {

    }

    private void commitSuggestion() {
        String type = "";
        if (mCheckedTypeId == R.id.chanpinjianyi) {
            type = getString(R.string.product_suggest);
        } else if (mCheckedTypeId == R.id.chengxucuowu) {
            type = getString(R.string.progress_error);
        } else {
            UiUtils.showToast(getString(R.string.title_feedback_type));
            return;
        }
        String qus = binding.jianyi.getText().toString();
        String email = binding.email.getText().toString();
        if (TextUtils.isEmpty(qus)) {
            UiUtils.showToast(getString(R.string.tips_content_is_empty));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            UiUtils.showToast(getString(R.string.tips_contacts_is_empty));
            return;
        }
        if (!email.matches("^[_a-zA-Z0-9\\-\\.]+@([\\-_a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,3}$") && !email.matches("^[1-9]\\d{4,12}$")) {
            UiUtils.showToast(getString(R.string.tips_contacts_is_error));
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText(getString(R.string.tips_please_wait));
        }
        dialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(getString(R.string.json_feedback_type), type);
            jsonObject.put(getString(R.string.json_feedback_content), qus);
            jsonObject.put(getString(R.string.json_feedback_contacts), email);
        } catch (Exception e) {
            UiUtils.showToast(getString(R.string.tips_progress_is_crash));
            e.printStackTrace();
        }
        LogUtil.i(jsonObject.toString());
        RetrofitHelper.getApi().postSuggestion(jsonObject.toString())
                .compose(this.<ResultBody>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        dialog.dismiss();


                    }

                    @Override
                    public void onNext(ResultBody resultBody) {
                        dialog.dismiss();
                        new AlertDialog.Builder(FeedbackActivity.this)
                                .setCancelable(false)
                                .setMessage(R.string.tips_feedbak_success)
                                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).create().show();

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
