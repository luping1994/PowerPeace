package net.suntrans.powerpeace.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.EditView;
import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.Constants;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.PayResult;
import net.suntrans.powerpeace.rx.RxBus;

import org.json.JSONException;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Looney on 2017/5/28.
 */

public class PayActivity extends BasedActivity {

    private TextView mobile;
    private EditView money;
    private Button button;
    private IWXAPI wxapi;
    private LoadingDialog dialog;
    private Subscription subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        wxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        mobile = (TextView) findViewById(R.id.mobile);
        money = (EditView) findViewById(R.id.money);
        button = (Button) findViewById(R.id.pay);
        button.setEnabled(false);
        mobile.setText(App.getSharedPreferences().getString("username", "--"));

        dialog = new LoadingDialog(this);
        dialog.setCancelable(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                System.out.println(s.length());
                if (s.length() == 0) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        money.setText(s);
                        money.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    money.setText(s);
                    money.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        money.setText(s.subSequence(0, 1));
                        money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        money.addTextChangedListener(watcher);

        subscribe = RxBus.getInstance().toObserverable(PayResult.class)
                .compose(this.<PayResult>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PayResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PayResult s) {

                        dialog.dismiss();
                        if (s.errorCode == 0) {
                            new IosAlertDialog(PayActivity.this)
                                    .setMsg("支付成功!")
                                    .setNegativeButton("关闭", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    }).show();
                        } else if (s.errorCode == -1) {
                            UiUtils.showToast("支付失败,服务器错误");
                        } else if (s.errorCode == -2) {
                            UiUtils.showToast("你已取消支付");
                        }
                    }
                });
    }

    public void pay(View view) {
        try {
            payUseWX();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void payUseWX() throws JSONException {
        if (!wxapi.isWXAppInstalled()) {
            UiUtils.showToast("您未安装微信客户端");
            return;
        }
        dialog.setWaitText("正在发起微信支付");
        dialog.show();
        String moneys = money.getText().toString();
//        RetrofitHelper.getCookieApi().getPayObj(moneys, "充电储值", "1")
//                .compose(this.<PayObj>bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<PayObj>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        UiUtils.showToast("获取订单失败");
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onNext(PayObj payObj) {
////                        System.out.println(payObj.toString());
//                        dialog.dismiss();
//                        if (wxapi == null) {
//                            wxapi = WXAPIFactory.createWXAPI(PayActivity.this, Constants.APP_ID);
//                        }
//                        wxapi.registerApp(Constants.APP_ID);
//                        PayReq req = new PayReq();
//
//                        req.appId = payObj.appid;  // 测试用appId
//                        req.partnerId = payObj.partnerid;
//                        req.prepayId = payObj.prepayid;
//                        req.nonceStr = payObj.noncestr;
//                        req.timeStamp = payObj.timestamp;
//                        req.packageValue = payObj.packages;
//                        req.sign = payObj.sign;
//
//                        wxapi.sendReq(req);
//                    }
//                });

    }

    @Override
    protected void onDestroy() {
        wxapi.detach();
//        wxapi.unregisterApp();

        if (!subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
        }
        super.onDestroy();

    }
}
