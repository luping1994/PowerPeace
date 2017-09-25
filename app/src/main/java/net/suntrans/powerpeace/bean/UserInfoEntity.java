package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/15.
 */

public class UserInfoEntity extends ResultBody<List<UserInfoEntity.UserInfo>>{

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
         * studentID : 2013301470027
         * room_id : 21
         * IDnumber : 0
         * major : 66
         * name : 王晓青
         * telephone : 0
         * academy : 动力与机械学院
         */

        public String studentID;
        public String room_id;
        public String IDnumber;
        public String major;
        public String name;
        public String telephone;
        public String academy;



        public String tel_num;
        public String sex;
        public String email;
        public String username;
        public String status;

        @Override
        public String toString() {
            return "UserInfo{" +
                    "studentID=" + studentID +
                    ", room_id=" + room_id +
                    ", IDnumber=" + IDnumber +
                    ", major='" + major + '\'' +
                    ", name='" + name + '\'' +
                    ", telephone=" + telephone +
                    ", academy='" + academy + '\'' +
                    ", tel_num='" + tel_num + '\'' +
                    ", sex='" + sex + '\'' +
                    ", email='" + email + '\'' +
                    ", username='" + username + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
