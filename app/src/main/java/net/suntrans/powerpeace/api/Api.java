package net.suntrans.powerpeace.api;


import net.suntrans.powerpeace.bean.HisEntity;
import net.suntrans.powerpeace.bean.LoginEntity;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.bean.RoomInfolEntity;
import net.suntrans.powerpeace.bean.StudentDetailInfo;
import net.suntrans.powerpeace.bean.StudentInfoEntity;
import net.suntrans.powerpeace.bean.SusheEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.bean.ZongheEntity;

import java.util.List;

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
    Observable<RoomInfolEntity> getSusheDetail(@Field("room_id") String room_id);

    /**
     * 管理员查询宿舍详细信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_Room_Detail_Bysid")
    Observable<RoomInfolEntity> getSusheDetailBysid(@Field("username") String username);

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

    /**
     * 管理员查询学生
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_Student_Detail_BySid")
    Observable<ResultBody<List<StudentDetailInfo>>> getStudentInfoDetail(@Field("studentID") String studentID);

    /**
     * 管理员查询历史数据
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_Ammeter_History")
    Observable<HisEntity> getMeterHis(@Field("room_id") String room_id,
                                      @Field("data_type") String data_type);

    /**
     * 管理员查询综合数据
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_3Ammeter_Current")
    Observable<ZongheEntity> getZongheData(@Field("departmentID") String departmentID,
                                           @Field("building") String building);

    /**
     *学生用户通过用户名查询用户信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_StudentInfo_Byusername")
    Observable<UserInfoEntity> getUserInfo(@Field("username") String username);


}
