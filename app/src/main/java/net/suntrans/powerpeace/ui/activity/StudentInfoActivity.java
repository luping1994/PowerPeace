package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.bean.StudentDetailInfo;
import net.suntrans.powerpeace.databinding.ActivityStudentInfoBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import java.util.List;

/**
 * Created by Looney on 2017/9/8.
 */

public class StudentInfoActivity extends BasedActivity {

    private ActivityStudentInfoBinding binding;
    private String studentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_info);

        StatusBarCompat.compat(binding.headerView);

        binding.toolbar.setTitle(getIntent().getStringExtra("name"));

        studentID = getIntent().getStringExtra("studentID");
//        LogUtil.i(studentID);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        addSubscription(RetrofitHelper.getApi().getStudentInfoDetail(studentID),new BaseSubscriber<ResultBody<List<StudentDetailInfo>>>(this){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                UiUtils.showToast(getString(R.string.tips_check_info_error));
            }

            @Override
            public void onNext(ResultBody<List<StudentDetailInfo>> listResultBody) {
                List<StudentDetailInfo> info = listResultBody.info;
                setData(info.get(0));
            }
        });
    }

    private void setData(StudentDetailInfo info) {
        binding.xuehao.setText(info.studentID+"");
        binding.yuanxi.setText(info.academy+"");
        binding.zhuanye.setText(info.major+"");
        binding.dianhua.setText(info.telephone+"");
        binding.sushe.setText(info.academy+"-"+info.building+"-"+info.dormitory);
    }
}
