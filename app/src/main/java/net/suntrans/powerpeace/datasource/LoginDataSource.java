package net.suntrans.powerpeace.datasource;

import net.suntrans.powerpeace.api.Api;
import net.suntrans.powerpeace.api.RetrofitHelper;

import rx.Observable;

/**
 * Created by Looney on 2017/12/8.
 * Des:
 */

public class LoginDataSource {

    public static Observable Login(String username, String password) {
        Api api = RetrofitHelper.getApi();
        return api.login(username, password, "password", "100001", "peS4zinqLC2x5pSc2Li98whTbSaC0d1OwrYsqQpL");
    }
}
