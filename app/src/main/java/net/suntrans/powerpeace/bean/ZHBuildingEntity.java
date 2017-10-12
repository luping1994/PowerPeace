package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/10/12.
 */
public class ZHBuildingEntity extends ResultBody<List<ZHBuildingEntity.ZHBuilding>> {
    public static class ZHBuilding {
        public String id;
        public String title;
        public List<SN> ammeter3;

        public static class SN {
            /**
             * building_ammeter3_id : 1
             * floor_id : 3
             * title : 1#S总
             * name : 南楼
             * sno : 222160041743
             */
            public String building_ammeter3_id;
            public String floor_id;
            public String title;
            public String name;
            public String sno;
            public boolean isHeader = false;
        }
    }
}
