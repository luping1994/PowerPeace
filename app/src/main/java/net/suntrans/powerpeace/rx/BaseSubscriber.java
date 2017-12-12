package net.suntrans.powerpeace.rx;

import android.content.Context;


import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.api.ApiErrorCode;
import net.suntrans.powerpeace.api.ApiErrorHelper;
import net.suntrans.powerpeace.api.ApiException;

import rx.Subscriber;

/**
 * Created by Looney on 2017/9/6.
 */

public class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        if (!UiUtils.isNetworkAvailable()) {
            this.onError(new ApiException(ApiErrorCode.ERROR_NO_INTERNET, "network interrupt"));
            this.unsubscribe();
            return;
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        ApiErrorHelper.handleCommonError(context,e);
    }

    @Override
    public void onNext(T t) {

    }
}
