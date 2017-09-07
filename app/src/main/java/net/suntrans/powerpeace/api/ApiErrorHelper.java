package net.suntrans.powerpeace.api;

import android.content.Context;
import android.content.Intent;

import net.suntrans.looney.utils.UiUtils;
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
//                UiUtils.showToast("您的身份已过期,请重新登录");
//                context.startActivity(new Intent(context, AlertActivity.class));
//                ((BasedActivity)context).overridePendingTransition(0,0);
//                ActivityUtils.showLoginOutDialogFragmentToActivity(((BasedActivity)context).getSupportFragmentManager(),"Alert");
//                AlertDialog.Builder builder = new AlertDialog.Builder(App.getApplication());
//                AlertDialog dialog = builder.setMessage("您的身份已过期,请重新登录")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ((BasedActivity)context).killAll();
//                            }
//                        }).create();
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
//                if (!dialog.isShowing()) {//此时提示框未显示
//                    dialog.show();
//                }
            } else if (code == ApiErrorCode.ERROR_NO_INTERNET) {
                UiUtils.showToast("网络连接不可用");
            }else if (code == ApiErrorCode.ERROR){
                UiUtils.showToast(((ApiException)e).msg);
            }
        } else {
            UiUtils.showToast("服务器错误");
        }
    }
}
