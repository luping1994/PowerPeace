package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/4.
 */

public class StudentInfoEntity {

    /**
     * code : 1
     * message : Inquiry_Student_Info success
     * info : [{"departmentName":"工学部","room_id":1,"sublist":[{"studentID":2013301470027,"name":"王晓青","academy":"动力与机械学院"},{"studentID":null,"name":"ss","academy":null},{"studentID":1,"name":"bb","academy":"1"},{"studentID":null,"name":"cc","academy":null}],"departmentID":1,"dormitory":"101","floor":"1","building":"1"},{"departmentName":"工学部","room_id":8,"sublist":[],"departmentID":1,"dormitory":"102","floor":"1","building":"1"},{"departmentName":"工学部","room_id":5,"sublist":[],"departmentID":1,"dormitory":"103","floor":"1","building":"1"}]
     */

    public int code;
    public String message;
    public List<InfoBean> info;

    public static class InfoBean {
        /**
         * departmentName : 工学部
         * room_id : 1
         * sublist : [{"studentID":2013301470027,"name":"王晓青","academy":"动力与机械学院"},{"studentID":null,"name":"ss","academy":null},{"studentID":1,"name":"bb","academy":"1"},{"studentID":null,"name":"cc","academy":null}]
         * departmentID : 1
         * dormitory : 101
         * floor : 1
         * building : 1
         */

        public String departmentName;
        public int room_id;
        public int departmentID;
        public String dormitory;
        public String floor;
        public String building;
        public List<SublistBean> sublist;

        public static class SublistBean {
            /**
             * studentID : 2013301470027
             * name : 王晓青
             * academy : 动力与机械学院
             */

            public long studentID;
            public String name;
            public String academy;
        }
    }
}
