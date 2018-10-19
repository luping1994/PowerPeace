package net.suntrans.powerpeace.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.ui.activity.MsgCenterActivity;
import net.suntrans.powerpeace.ui.activity.YichangActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Looney on 2017/6/8.
 */

public class JpushReceive extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;
    private SoundPool sp;
    private int load_id;
    private AssetManager assets;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            LogUtil.d(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(TAG, "接受到推送下来的通知");
//            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtil.d(TAG, "用户点击打开了通知");
            openNotification(context, bundle);

        } else {
            LogUtil.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        LogUtil.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtil.e("message:" + message);
        LogUtil.e("extras:" + extras);
        if (sp == null)
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        if (assets == null)
            assets = context.getAssets();
        try {
            JSONObject jsonObject = new JSONObject(extras);
            String sound = jsonObject.getString("sound");
            AssetFileDescriptor assetFileDescriptor = assets.openFd("sound/" + sound);
            load_id = sp.load(assetFileDescriptor, 1);
            sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    int play = sp.play(load_id, 0.8f, 0.8f, 1, 0, 1.0f);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void openNotification(Context context, Bundle bundle) {

        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtil.e("extras:" + extras);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(extras);
            int type = jsonObject.getInt("type");
            if (type == 1) {
                Intent intent = new Intent(context, MsgCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", "消息中心");
                context.startActivity(intent);
            } else if (type == 2) {
                Intent intent = new Intent(context, YichangActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", "异常提示");
                context.startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
