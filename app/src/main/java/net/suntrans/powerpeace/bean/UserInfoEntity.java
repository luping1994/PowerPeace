package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/15.
 */

public class UserInfoEntity extends ResultBody<UserInfoEntity.UserInfo>{

    /**
     * code : 1
     * message : Inquiry_StudentInfo_Byusername Success
     * info : [{"studentID":2013301470027,"room_id":21,"IDnumber":0,"major":"66","name":"王晓青","telephone":0,"academy":"动力与机械学院"}]
     */

//    public int code;
//    public String message;
//    public List<UserInfo> info;

//    @Override
//    public String toString() {
//        return info.get(0).toString();
//    }


    public static class UserInfo {
        /**
         * role_id : 4
         * role_name : 学生
         * role_code : ROLE_STUDENT
         * login_num : 22
         * id : 1
         * username : 112501036
         * room_id : 1052
         */

        public int role_id;
        public String role_name;
        public String role_code;
        public String login_num;
        public String id;
        public String username;
        public String room_id;
        public String floor_id;

    }
}
