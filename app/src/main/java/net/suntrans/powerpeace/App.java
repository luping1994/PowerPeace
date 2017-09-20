package net.suntrans.powerpeace;

import android.app.Application;
import android.content.SharedPreferences;

import com.pgyersdk.crash.PgyCrashManager;

import static net.suntrans.powerpeace.BuildConfig.DEBUG;
import android.content.Context;

import net.suntrans.looney.AppBase;

/**
 * Created by Looney on 2017/8/31.
 */

public class App extends AppBase{

//    public static SharedPreferences sharedPreferences;
//    private static Application application;
//    public static Application getApplication() {
//        return application;
//    }
//
//    public static SharedPreferences getSharedPreferences() {
//        if (sharedPreferences == null) {
//            sharedPreferences = getApplication().getSharedPreferences("suntransconfig", Context.MODE_PRIVATE);
//        }
//        return sharedPreferences;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
//        application =this;
        if (!DEBUG){
            PgyCrashManager.register(this);
        }
    }
}
