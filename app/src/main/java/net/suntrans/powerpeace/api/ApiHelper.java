package net.suntrans.powerpeace.api;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.bean.LogInfoEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static net.suntrans.powerpeace.api.RetrofitHelper.getApi;

/**
 * Created by Looney on 2017/9/18.
 */

public class ApiHelper {
    public static ApiHelper getInstance() {
        return new ApiHelper();
    }

    public void getUserInfo(String username, final OnDataGetListener listener) {
//        addSubscription(api.getUserInfo(username), new Subscriber<UserInfoEntity>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                UiUtils.showToast("连接服务器出错了,登录失败");
//
//            }
//
//            @Override
//            public void onNext(UserInfoEntity info) {
//                System.out.println(info.toString());
//                listener.onUserInfoReturned(info);
//            }
//        });

    }

    public void getLogInfo(String roomid,String inquirytime, final OnDataGetListener listener) {
        addSubscription(RetrofitHelper.getApi().getLogInfo(roomid,inquirytime), new Subscriber<LogInfoEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onNext(LogInfoEntity info) {
                listener.onLogInfoReturned(info);
            }
        });

    }

    public interface OnDataGetListener {
        void onUserInfoReturned(UserInfoEntity infoEntity);
        void onLogInfoReturned(LogInfoEntity infoEntity);
        void onError(Throwable e);
    }

    protected Api api = RetrofitHelper.getApi();
    protected CompositeSubscription mCompositeSubscription;

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
