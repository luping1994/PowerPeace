package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/1.
 */

public class SusheEntity extends ResultBody<List<SusheEntity.InfoBean>>{
    public static class InfoBean {

        public String departmentName;
        public String floor_name;
        public String building_name;
        public int departmentID;
        public int floor;
        public int building;
        public List<SusheInfo> sublist;

    }

    public static class SusheInfo {
        public String room_id;
        public String dormitory;
        public String status;
    }
}
