package net.suntrans.powerpeace.ui.fragment;

import net.suntrans.powerpeace.api.Api;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.stateview.StateView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Looney on 2017/8/31.
 */

public class BasedFragment extends LazyLoadFragment {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onUnsubscribe();
    }
}
