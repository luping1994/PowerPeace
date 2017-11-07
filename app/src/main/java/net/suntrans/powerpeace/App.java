package net.suntrans.powerpeace;

import android.content.Intent;

import com.pgyersdk.Pgy;
import com.tencent.bugly.crashreport.CrashReport;

import net.suntrans.looney.AppBase;

import static net.suntrans.powerpeace.BuildConfig.DEBUG;

/**
 * Created by Looney on 2017/8/31.
 */

public class App extends AppBase {


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
//
//    public static SharedPreferences sharedPreferences;
//    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
//        application =this;
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        startService(new Intent(this, MyService.class));
        if (!DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "fd77fe012a", false);
        }
        Pgy.init(this, "c49751bc841e2b3a6115ced0b185e789");
    }
}
