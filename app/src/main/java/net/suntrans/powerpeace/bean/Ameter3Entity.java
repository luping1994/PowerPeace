package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class Ameter3Entity extends ResultBody<Ameter3Entity.DataBean>{


    public static class DataBean {
        /**
         * sno : 222170077877
         * eletricity_day : [{"y":"0.00","x":0},{"y":"0.00","x":1},{"y":"0.80","x":2},{"y":"0.00","x":3},{"y":"0.00","x":4},{"y":"0.80","x":5},{"y":"0.00","x":6},{"y":"0.80","x":7},{"y":"0.00","x":8},{"y":"0.00","x":9},{"y":"0.80","x":10},{"y":"0.00","x":11},{"y":"0.00","x":12},{"y":"0.80","x":13},{"y":"0.00","x":14},{"y":"0.00","x":15},{"y":"0.80","x":16},{"y":"0.00","x":17},{"y":"0.00","x":18},{"y":"0.00","x":19},{"y":"0.00","x":20},{"y":"0.00","x":21},{"y":"0.00","x":22},{"y":"0.00","x":23}]
         * eletricity_month : [{"y":"2.40","x":3},{"y":"2.00","x":4},{"y":"0.00","x":14},{"y":"2.40","x":15},{"y":"4.80","x":16},{"y":"0.00","x":17}]
         * eletricity_year : [{"y":"11.60","x":8}]
         */

        public String sno;
        public List<HisItem> electricity_day;
        public List<HisItem> electricity_month;
        public List<HisItem> electricity_year;


        public static class HisItem {
            /**
             * y : 0.00
             * x : 0
             */
            public String data;
            public int x;

        }


    }
}
