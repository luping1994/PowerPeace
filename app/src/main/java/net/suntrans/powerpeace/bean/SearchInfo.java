package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/26.
 */

public class SearchInfo extends ResultBody {

    public List<StudentinfoBean> studentinfo;
    public List<RoominfoBean> roominfo;

    public static class StudentinfoBean {
        /**
         * studentID : 115504101
         * departmentName : 和平校区
         * floor_name : 一层
         * room_id : 1143
         * building_name : 一号学生公寓
         * departmentID : 1
         * name : 陈冬玲
         * floor : 1
         * building : 1
         */
        public String studentID;
        public String departmentName;
        public String floor_name;
        public String room_id;
        public String building_name;
        public String departmentID;
        public String name;
        public String floor;
        public String building;
    }

    public static class RoominfoBean {
        /**
         * departmentName : 和平校区
         * floor_name : 一层
         * room_id : 1101
         * building_name : 一号学生公寓
         * departmentID : 1
         * floor : 1
         * building : 1
         */
        public String departmentName;
        public String floor_name;
        public String room_id;
        public String building_name;
        public String departmentID;
        public String floor;
        public String building;
    }
}
