package net.suntrans.powerpeace.api;

import android.content.Context;
import android.content.Intent;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.powerpeace.rx.RxBus;
import net.suntrans.powerpeace.ui.activity.AlertActivity;
import net.suntrans.powerpeace.ui.activity.BasedActivity;

import java.io.IOException;
import java.util.EventListener;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Looney on 2017/9/6.
 */

public class ApiErrorHelper {
    public static void handleCommonError(final Context context, Throwable e) {
        if (e instanceof HttpException) {
            UiUtils.showToast("服务暂不可用");
        } else if (e instanceof ApiException) {
            int code = ((ApiException) e).code;
            if (code == ApiErrorCode.UNAUTHORIZED) {
                UiUtils.showToast("您的身份已过期,请重新登录");
                Intent intent = new Intent(context, AlertActivity.class);
                RxBus.getInstance().post(intent);
            } else if (code == ApiErrorCode.ERROR_NO_INTERNET) {
                UiUtils.showToast("网络连接不可用");
            } else if (code == ApiErrorCode.ERROR) {
                UiUtils.showToast(((ApiException) e).msg);
            }else {
                UiUtils.showToast("服务器错误");
            }
        } else if (e instanceof IOException) {
            UiUtils.showToast("网络连接不可用");
        } else {
            UiUtils.showToast("服务器错误");
        }
    }
}
