package net.suntrans.powerpeace;

import android.content.Intent;

import com.pgyersdk.crash.PgyCrashManager;

import net.suntrans.looney.AppBase;

import static net.suntrans.powerpeace.BuildConfig.DEBUG;

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
        startService(new Intent(this,MyService.class));
        if (!DEBUG){
            PgyCrashManager.register(this);
        }
    }
}
