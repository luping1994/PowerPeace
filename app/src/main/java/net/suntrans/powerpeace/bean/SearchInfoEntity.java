package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/26.
 */

public class SearchInfoEntity extends ResultBody {

    public List<SearchInfo> studentinfo;
    public List<SearchInfo> roominfo;

    public static class SearchInfo {

        public String studentID;
        public String departmentName;
        public String floor_name;
        public String room_id;
        public String building_name;
        public String departmentID;
        public String name;
        public String floor;
        public String building;
        public String room_sn;
        public int type;

    }


}
