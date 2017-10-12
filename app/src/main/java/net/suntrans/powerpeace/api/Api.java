package net.suntrans.powerpeace.api;


import net.suntrans.powerpeace.bean.AccountInfo;
import net.suntrans.powerpeace.bean.Ameter3Entity;
import net.suntrans.powerpeace.bean.BuildingResult;
import net.suntrans.powerpeace.bean.ChannelInfo;
import net.suntrans.powerpeace.bean.ChannelStatus;
import net.suntrans.powerpeace.bean.EleInfo;
import net.suntrans.powerpeace.bean.FloorManagerInfo;
import net.suntrans.powerpeace.bean.HisEntity;
import net.suntrans.powerpeace.bean.LogInfoEntity;
import net.suntrans.powerpeace.bean.LoginEntity;
import net.suntrans.powerpeace.bean.MenuBean;
import net.suntrans.powerpeace.bean.MsgEntity;
import net.suntrans.powerpeace.bean.ResultBody;
import net.suntrans.powerpeace.bean.RoomInfolEntity;
import net.suntrans.powerpeace.bean.SearchInfoEntity;
import net.suntrans.powerpeace.bean.StudentDetailInfo;
import net.suntrans.powerpeace.bean.StudentInfo;
import net.suntrans.powerpeace.bean.StudentInfoEntity;
import net.suntrans.powerpeace.bean.SusheEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.bean.UserInfoEntityOld;
import net.suntrans.powerpeace.bean.ZHBFloorEntity;
import net.suntrans.powerpeace.bean.ZHBuildingEntity;
import net.suntrans.powerpeace.bean.ZHEnergyEntity;
import net.suntrans.powerpeace.bean.ZongheEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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
    @POST("/oauth/token")
    Observable<LoginEntity> login(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("grant_type") String grant_type,
                                  @Field("client_id") String client_id,
                                  @Field("client_secret") String client_secret);

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
    Observable<HisEntity> getMeterHis(@FieldMap Map<String, String> map);

    /**
     * 管理员查询综合数据
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_3Ammeter_Current")
    Observable<ZongheEntity> getZongheData(@Field("departmentID") String departmentID,
                                           @Field("building") String building,
                                           @Field("floor") String floor);

    /**
     * 学生用户通过用户名查询用户信息
     *
     * @param
     * @return
     */
    @POST("api/account/baseInfo")
    Observable<UserInfoEntity> getUserInfo();

    /**
     * 学生用户通过用户名查询用户信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_StudentInfo_Byusername")
    Observable<UserInfoEntityOld> getUserInfo(@Field("username") String username, @Field("role") String role);

    /**
     * 学生用户通过用户名查询rizhi
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_Channel_Control_Log")
    Observable<LogInfoEntity> getLogInfo(@Field("room_id") String room_id,
                                         @Field("inquirytime") String time);


    @FormUrlEncoded
    @POST("api/feedback")
    Observable<ResultBody> postSuggestion(@Field("suggestion") String suggestion);

    @FormUrlEncoded
    @POST("api/account/password")
    Observable<ResultBody> changePassword(@Field("oldPwd") String oldPwd, @Field("newPwd") String newPwd);

    @FormUrlEncoded
    @POST("api/account/profile")
    Observable<ResultBody> changePhone(@Field("tel") String tel);

    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_search")
    Observable<SearchInfoEntity> search(@Field("info") String info);

    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_message_bystuid")
    Observable<MsgEntity> getMsg(@Field("studentID") String studentID);

    @FormUrlEncoded
    @POST("api/workman/switch/channel")
    Observable<ResultBody> control(@Field("vtype") String vtype,
                                   @Field("addr") String addr,
                                   @Field("num") String num,
                                   @Field("cmd") String cmd);

    @FormUrlEncoded
    @POST("api/portal/channel")
    Observable<ResultBody<List<ChannelInfo>>> getRoomChannel(@Field("room_id") String room_id);

    /**
     * 楼栋管理员的获取楼层信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/building/floorByID")
    Observable<ResultBody<List<FloorManagerInfo>>> getManagerFloorInfo(@Field("floor_id") String floor_id);

    /**
     * 楼层全关
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/workman/switch/floor")
    Observable<ResultBody> switchFloor(@FieldMap Map<String, String> map);


    @POST("api/building/index")
    Observable<BuildingResult> getBuildings();


    @FormUrlEncoded
    @POST("api/portal/account")
    Observable<ResultBody<AccountInfo>> getAccountInfo(@Field("room_id") String room_id);

    @FormUrlEncoded
    @POST("api/portal/ammeter")
    Observable<ResultBody<List<EleInfo>>> getEleInfo(@Field("room_id") String room_id);

    @FormUrlEncoded
    @POST("api/portal/student")
    Observable<ResultBody<List<StudentInfo>>> getStuInfo(@Field("room_id") String room_id);

    @FormUrlEncoded
    @POST("api/status/slc/byRoomID")
    Observable<ResultBody<List<ChannelStatus>>> getChannelStatusOnly(@Field("room_id") String room_id);

    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_Ammeter_UseValue")
    Observable<Ameter3Entity> getAmmeter3Data(@Field("room_id") String room_id, @Field("date") String date);

    /**
     * 综合获取楼栋信息
     *
     * @return
     */
    @POST("api/inquiry/Inquiry_3Ammeter_List")
    Observable<ZHBuildingEntity> getZongheBuilding();

    /**
     * 综合获取楼栋-楼层信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_3Ammeter_Floor")
    Observable<ZHBFloorEntity> getZongheBuildingFloor(@Field("ammeter3_id") String ammeter3_id);

    /**
     * 综合获取详细信息
     * @return
     */
    @FormUrlEncoded
    @POST("api/inquiry/Inquiry_3Ammeter_Energy")
    Observable<ZHEnergyEntity> getZongheBuildingEnergy(@Field("type") String type, @Field("id") String id);
}
