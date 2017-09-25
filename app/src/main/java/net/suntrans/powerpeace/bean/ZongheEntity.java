package net.suntrans.powerpeace.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/9/15.
 */

public class ZongheEntity {

    /**
     * code : 1
     * message : Inquiry_3Ammeter_Current Success
     * info : [{"cV_value":"1.0V","bA_value":"1.23A","rP_value":"1.0W","PR_value":1,"E_value":"5.01KW.H","bV_value":"1.1V","aA_value":"1.0A","cA_value":"1.0A","aV_value":"1.1V","E_day_value":"1.0KW.H","aP_value":"1.22W","E_month_value":"5.0KW.H"}]
     */

    public int code;
    public String message;
//    public List<Map<String ,String>> info;
    public List<Zonghe> info;

    public static class Zonghe {
        /**
         * cV_value : 1.0V
         * bA_value : 1.23A
         * rP_value : 1.0W
         * PR_value : 1
         * E_value : 5.01KW.H
         * bV_value : 1.1V
         * aA_value : 1.0A
         * cA_value : 1.0A
         * aV_value : 1.1V
         * E_day_value : 1.0KW.H
         * aP_value : 1.22W
         * E_month_value : 5.0KW.H
         */

        public String cV_value;
        public String bA_value;
        public String rP_value;
        public String PR_value;
        public String E_value;
        public String bV_value;
        public String aA_value;
        public String cA_value;
        public String aV_value;
        public String E_day_value;
        public String aP_value;
        public String E_month_value;

        @Override
        public String toString() {
            return "Zonghe{" +
                    "cV_value='" + cV_value + '\'' +
                    ", bA_value='" + bA_value + '\'' +
                    ", rP_value='" + rP_value + '\'' +
                    ", PR_value=" + PR_value +
                    ", E_value='" + E_value + '\'' +
                    ", bV_value='" + bV_value + '\'' +
                    ", aA_value='" + aA_value + '\'' +
                    ", cA_value='" + cA_value + '\'' +
                    ", aV_value='" + aV_value + '\'' +
                    ", E_day_value='" + E_day_value + '\'' +
                    ", aP_value='" + aP_value + '\'' +
                    ", E_month_value='" + E_month_value + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return info.toString();
    }
}
