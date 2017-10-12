package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/10/12.
 * Des:
 */

public class ZHEnergyEntity {

    /**
     * code : 1
     * message : 查询成功
     * type : ammeter3
     * data : [{"ElectricityDAY":0.53,"ElectricityMonth":1.86,"ElectricityTotal":11.61,"DayCost":null,"MonthCost":null,"YearCost":null,"TotalCost":null,"DayStore":null,"MonthStore":null,"YearStore":null,"TotalStore":null,"DayLoss":null,"MonthLoss":null,"YearLoss":null,"TotalLoss":null,"DayLossPercent":null,"title":"3#N8层","MonthLossPercent":null,"YearLossPercent":null,"TotalLossPercent":null,"sno":"222160041739","aV_value":"228.7","bV_value":"229.6","cV_value":"229.2","aA_value":"0.009","bA_value":"0.009","cA_value":"0.010","aP_value":"0.0018","rP_value":"-0.0042","ElectricityValue":"11.61"}]
     */

    public int code;
    public String message;
    public String type;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * ElectricityDAY : 0.53
         * ElectricityMonth : 1.86
         * ElectricityTotal : 11.61
         * DayCost : null
         * MonthCost : null
         * YearCost : null
         * TotalCost : null
         * DayStore : null
         * MonthStore : null
         * YearStore : null
         * TotalStore : null
         * DayLoss : null
         * MonthLoss : null
         * YearLoss : null
         * TotalLoss : null
         * DayLossPercent : null
         * title : 3#N8层
         * MonthLossPercent : null
         * YearLossPercent : null
         * TotalLossPercent : null
         * sno : 222160041739
         * aV_value : 228.7
         * bV_value : 229.6
         * cV_value : 229.2
         * aA_value : 0.009
         * bA_value : 0.009
         * cA_value : 0.010
         * aP_value : 0.0018
         * rP_value : -0.0042
         * ElectricityValue : 11.61
         */

        public String ElectricityDAY;
        public String ElectricityMonth;
        public String ElectricityTotal;
        public String DayCost;
        public String MonthCost;
        public String YearCost;
        public String TotalCost;
        public String DayStore;
        public String MonthStore;
        public String YearStore;
        public String TotalStore;
        public String DayLoss;
        public String MonthLoss;
        public String YearLoss;
        public String TotalLoss;
        public String DayLossPercent;
        public String title;
        public String MonthLossPercent;
        public String YearLossPercent;
        public String TotalLossPercent;
        public String sno;
        public String aV_value;
        public String bV_value;
        public String cV_value;
        public String aA_value;
        public String bA_value;
        public String cA_value;
        public String aP_value;
        public String rP_value;
        public String ElectricityValue;
    }
}
