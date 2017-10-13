package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/10/12.
 * Des:
 */

public class ZHBFloorEntity {

    /**
     * code : 1
     * message : 查询成功
     * building_ammeter3_id : 1
     * data : [{"floor":"一号1层","floor_ammeter3_id":12,"sno":"222160041717","ammeter3_place":"南楼一层"},{"floor":"一号2层","floor_ammeter3_id":13,"sno":"222160041688","ammeter3_place":"南楼二层"},{"floor":"一号3层","floor_ammeter3_id":14,"sno":"222160041735","ammeter3_place":"南楼三层"},{"floor":"一号4层","floor_ammeter3_id":15,"sno":"222160041752","ammeter3_place":"南楼四层"},{"floor":"一号5层","floor_ammeter3_id":16,"sno":"222160041703","ammeter3_place":"南楼五层"},{"floor":"一号6层","floor_ammeter3_id":17,"sno":"222160041678","ammeter3_place":"南楼六层"},{"floor":"一号7层","floor_ammeter3_id":18,"sno":"222160041673","ammeter3_place":"南楼七层"},{"floor":"一号8层","floor_ammeter3_id":19,"sno":"222160041708","ammeter3_place":"南楼八层"}]
     */

    public int code;
    public String message;
    public String building_ammeter3_id;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * floor : 一号1层
         * floor_ammeter3_id : 12
         * sno : 222160041717
         * ammeter3_place : 南楼一层
         */

        public String floor;
        public String floor_ammeter3_id;
        public String sno;
        public String ammeter3_place;
    }
}
