package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/1.
 */

public class StudentEntity {


    /**
     * room_stu : [{"studentID":2013301470027,"name":"王晓青","telephone":13247128702,"academy":"动力与机械学院"},{"studentID":null,"name":"ss","telephone":null,"academy":null},{"studentID":1,"name":"bb","telephone":null,"academy":"1"},{"studentID":null,"name":"cc","telephone":null,"academy":null}]
     * meter_info : [{"PR_value":1,"E_value":0,"I_value":0,"V_value":240.7,"P_value":0,"id":1}]
     * code : 1
     * dev_channel : [{"num":1,"name":"照明","id":1,"status":false},{"num":2,"name":"插座","id":2,"status":false},{"num":3,"name":"阳台","id":3,"status":false},{"num":4,"name":"卫生间","id":4,"status":false},{"num":5,"name":"备用","id":5,"status":false},{"num":6,"name":"备用","id":6,"status":false}]
     * message : Inquiry_RoomDetail_RoomId Success
     * account : [{"id":1,"balans":2}]
     */

    public int code;
    public String message;
    public List<RoomStuBean> room_stu;
    public List<MeterInfoBean> meter_info;
    public List<DevChannelBean> dev_channel;
    public List<AccountBean> account;

    public static class RoomStuBean {
        /**
         * studentID : 2013301470027
         * name : 王晓青
         * telephone : 13247128702
         * academy : 动力与机械学院
         */

        public String studentID;
        public String name;
        public String telephone;
        public String academy;
    }

    public static class MeterInfoBean {
        /**
         * PR_value : 1
         * E_value : 0
         * I_value : 0
         * V_value : 240.7
         * P_value : 0
         * id : 1
         */
        public int PR_value;
        public int E_value;
        public int I_value;
        public double V_value;
        public int P_value;
        public int id;
    }

    public static class DevChannelBean {
        /**
         * num : 1
         * name : 照明
         * id : 1
         * status : false
         */
        public int num;
        public String name;
        public int id;
        public boolean status;
    }

    public static class AccountBean {
        /**
         * id : 1
         * balans : 2
         */
        public int id;
        public int balans;
    }
}
