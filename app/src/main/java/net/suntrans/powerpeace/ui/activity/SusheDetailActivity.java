package net.suntrans.powerpeace.ui.activity;

import android.content.ComponentName;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.powerpeace.Constants;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.databinding.ActivitySusheDetailBinding;
import net.suntrans.powerpeace.network.WebSocketService;
import net.suntrans.powerpeace.rx.RxBus;
import net.suntrans.powerpeace.ui.fragment.BasedFragment;
import net.suntrans.powerpeace.ui.fragment.SusheDetailFragment;
import net.suntrans.powerpeace.utils.StatusBarCompat;

import org.json.JSONObject;

import rx.Subscriber;

/**
 * Created by Looney on 2017/9/4.
 */

public class SusheDetailActivity extends BasedActivity implements BasedFragment.OnFragmentInteractionListener {

    private ActivitySusheDetailBinding binding;
    //    private List<RoomInfoSelection> datas = new ArrayList<>();
//    private MyAdapter adapter;
//    private Map<String, String> mMeterDictionaries;//电表中英字典
//    private Map<String, String> mAccountDictionaries;//账户栏目中英字典
//    private Map<String, String> meterInfo;//电表参数map
    private String room_id;
//    private StateView stateView;
//    private String whole_name;
//    private int mRefreshType = STATE_VIEW_REFRESH;
//    private static final int SWIP_REFRESH_LAYOUT = 0x01;
//    private static final int STATE_VIEW_REFRESH = 0x02;


    private WebSocketService.ibinder ibinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ibinder = (WebSocketService.ibinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sushe_detail);
        StatusBarCompat.compat(binding.headerView);//适配状态栏

        init();

        binding.toolbar.setTitle(getIntent().getStringExtra("title") + "详细信息");
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        room_id = getIntent().getStringExtra("room_id");
        SusheDetailFragment fragment = SusheDetailFragment.newInstance(room_id, String.valueOf(Constants.ROLE_ADMIN));
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

    }

    private void init() {

        RxBus.getInstance()
                .toObserverable(String.class)
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals(WebSocketService.CONNECT_SUCCESS)) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("dev", "4100");
                                jsonObject.put("ac", "gs");
                                jsonObject.put("rd", Integer.valueOf(room_id));
                                sendOrder(jsonObject.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


//        Intent intent = new Intent();
//        intent.setClass(this, WebSocketService.class);
//        bindService(intent, connection, ContextWrapper.BIND_AUTO_CREATE);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_pay, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.pay) {
//            startActivity(new Intent(this, PayActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(connection);
    }

    @Override
    public void sendOrder(String s) {
        if (ibinder != null)
            ibinder.sendOrder(s);
    }
}
