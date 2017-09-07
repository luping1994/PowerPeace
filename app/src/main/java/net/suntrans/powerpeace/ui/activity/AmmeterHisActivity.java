package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.HisEntity;
import net.suntrans.powerpeace.databinding.ActivityAmmeterHisBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2017/9/7.
 */

public class AmmeterHisActivity extends BasedActivity {

    private static final java.lang.String TAG = "AmmeterHisActivity";
    private ActivityAmmeterHisBinding binding;
    private String room_id;
    private String paramName;
    private Map<String, String> dictionaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ammeter_his);
        room_id = getIntent().getStringExtra("room_id");
        paramName = getIntent().getStringExtra("paramName");
        binding.toolbar.setTitle(getIntent().getStringExtra("title")+paramName);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        initData();
    }

    private void initData() {
        dictionaries = new HashMap<>();
        dictionaries.put("电压","1");
        dictionaries.put("电流","2");
        dictionaries.put("功率","3");
        dictionaries.put("功率因数","4");
        dictionaries.put("电表值","5");
        dictionaries.put("用电量","6");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    private void getData(){
        addSubscription(api.getMeterHis(room_id,dictionaries.get(paramName)),new BaseSubscriber<HisEntity>(this){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(HisEntity hisEntity) {
                super.onNext(hisEntity);
                LogUtil.i(TAG,hisEntity.toString());
            }
        });
    }
}
