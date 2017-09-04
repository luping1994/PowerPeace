package net.suntrans.powerpeace.api;


import net.suntrans.powerpeace.bean.LoginEntity;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.bean.StudentInfoEntity;
import net.suntrans.powerpeace.bean.SusheEntity;

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
     * 获取管理员首页三级菜单
     *
     * @param
     * @return
     */
    @POST("api/inquiry/Inquiry_Department_Info")
    Observable<MenuBean> getThreeMenu();

    /**
     * 管理员查询宿舍列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_Room_Info")
    Observable<SusheEntity> getSusheInfo(@Field("departmentID") String departmentID,
                                         @Field("building") String building,
                                         @Field("floor") String floor);

    /**
     * 管理员查询宿舍详细信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_Room_Detail_Byrid")
    Observable<SusheEntity> getSusheDetail(@Field("room_id") String room_id);

    /**
     * 管理员查询学生
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_Student_Info")
    Observable<StudentInfoEntity> getStudent(@Field("departmentID") String departmentID,
                                                   @Field("building") String building,
                                                   @Field("floor") String floor);


}
