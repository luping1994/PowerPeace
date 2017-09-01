package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/1.
 */

public class SusheEntity {

    public int code;
    public String message;
    public List<InfoBean> info;

    public static class InfoBean {

        public String departmentName;
        public int departmentID;
        public int floor;
        public int building;
        public List<SusheInfo> sublist;

    }

    public static class SusheInfo {
        public int room_id;
        public int dormitory;
    }
}
