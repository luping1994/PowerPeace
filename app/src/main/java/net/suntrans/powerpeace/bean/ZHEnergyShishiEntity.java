package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/10/12.
 * Des:
 */

public class ZHEnergyShishiEntity {


    /**
     * code : 1
     * message : 查询成功
     * info : [{"id":13,"updated_at":"2017-10-16 17:22:01","sno":222160041717,"VolA":"224.9","VolB":"226.1","VolC":"225.9","IA":"0.000","IB":"0.089","IC":"0.404","ActivePower":"0.1084","ActivePowerA":"0.0000","ActivePowerB":"0.0189","ActivePowerC":"0.0895","ReactivePower":"-0.0198","ReactivePowerA":"0.0000","ReactivePowerB":"-0.0051","ReactivePowerC":"-0.0147","PowerFactor":"0.984","PowerFactorA":"1.000","PowerFactorB":"0.966","PowerFactorC":"0.987","ElectricityValue":"23.11","ElectricitySharp":"0.00","ElectricityPeak":"7.09","ElectricityFlat":"9.33","ElectricityValley":"6.68","Temp":"17.10"}]
     */

    public int code;
    public String message;
    public List<InfoBean> info;

    public static class InfoBean {
        /**
         * id : 13
         * updated_at : 2017-10-16 17:22:01
         * sno : 222160041717
         * VolA : 224.9
         * VolB : 226.1
         * VolC : 225.9
         * IA : 0.000
         * IB : 0.089
         * IC : 0.404
         * ActivePower : 0.1084
         * ActivePowerA : 0.0000
         * ActivePowerB : 0.0189
         * ActivePowerC : 0.0895
         * ReactivePower : -0.0198
         * ReactivePowerA : 0.0000
         * ReactivePowerB : -0.0051
         * ReactivePowerC : -0.0147
         * PowerFactor : 0.984
         * PowerFactorA : 1.000
         * PowerFactorB : 0.966
         * PowerFactorC : 0.987
         * ElectricityValue : 23.11
         * ElectricitySharp : 0.00
         * ElectricityPeak : 7.09
         * ElectricityFlat : 9.33
         * ElectricityValley : 6.68
         * Temp : 17.10
         */

        public int id;
        public String updated_at;
        public String sno;
        public String VolA;
        public String VolB;
        public String VolC;
        public String IA;
        public String IB;
        public String IC;
        public String ActivePower;
        public String ActivePowerA;
        public String ActivePowerB;
        public String ActivePowerC;
        public String ReactivePower;
        public String ReactivePowerA;
        public String ReactivePowerB;
        public String ReactivePowerC;
        public String PowerFactor;
        public String PowerFactorA;
        public String PowerFactorB;
        public String PowerFactorC;
        public String ElectricityValue;
        public String ElectricitySharp;
        public String ElectricityPeak;
        public String ElectricityFlat;
        public String ElectricityValley;
        public String Temp;
    }
}
