package net.suntrans.powerpeace.api;


import net.suntrans.powerpeace.bean.LoginEntity;
import net.suntrans.powerpeace.bean.MenuBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Looney on 2017/1/4.
 */

public interface Api {


    /**
     * 登录
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/account/login")
    Observable<LoginEntity> login(@Field("username") String username,@Field("password") String password);

    /**
     * 登录
     *
     * @param
     * @return
     */
    @POST("api/inquiry/Inquiry_Department_Info")
    Observable<MenuBean> getThreeMenu();


}
