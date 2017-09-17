package net.suntrans.powerpeace;

import com.pgyersdk.crash.PgyCrashManager;

import static net.suntrans.powerpeace.BuildConfig.DEBUG;

/**
 * Created by Looney on 2017/8/31.
 */

public class App extends net.suntrans.looney.App{
    @Override
    public void onCreate() {
        super.onCreate();
        if (!DEBUG){
            PgyCrashManager.register(this);
        }
    }
}
