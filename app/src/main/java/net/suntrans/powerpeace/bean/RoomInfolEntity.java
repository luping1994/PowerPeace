package net.suntrans.powerpeace.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/9/5.
 */

public class RoomInfolEntity extends ResultBody{


    /**
     * room_stu : [{"studentID":2013301470027,"name":"王晓青","telephone":13247128702,"academy":"动力与机械学院"},{"studentID":null,"name":"ss","telephone":null,"academy":null},{"studentID":1,"name":"bb","telephone":null,"academy":"1"},{"studentID":null,"name":"cc","telephone":null,"academy":null}]
     * meter_info : [{"PR_value":1,"E_value":0,"I_value":0,"V_value":240.7,"P_value":0,"id":1}]
     * code : 1
     * dev_channel : [{"num":1,"name":"照明","id":1,"status":false},{"num":2,"name":"插座","id":2,"status":false},{"num":3,"name":"阳台","id":3,"status":false},{"num":4,"name":"卫生间","id":4,"status":false},{"num":5,"name":"备用","id":5,"status":false},{"num":6,"name":"备用","id":6,"status":false}]
     * message : Inquiry_RoomDetail_RoomId Success
     * account : [{"id":1,"balans":2}]
     */

//    public int code;
//    public String message;
    public List<Map<String,String>> room_stu;
    public List<RoomInfoSelection> meter_info;
    public List<Map<String,String>> dev_channel;
    public List<Map<String,String>> account;

}
