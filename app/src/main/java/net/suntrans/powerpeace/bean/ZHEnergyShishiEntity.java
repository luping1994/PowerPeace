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
     * info : [{"unit":"V","name":"A相电压","datapoint":100201,"value":"228.1"},{"unit":"V","name":"B相电压","datapoint":100202,"value":"228.8"},{"unit":"V","name":"C相电压","datapoint":100203,"value":"227.7"},{"unit":"A","name":"A相电流","datapoint":100204,"value":"0.010"},{"unit":"A","name":"B相电流","datapoint":100205,"value":"0.012"},{"unit":"A","name":"C相电流","datapoint":100206,"value":"0.012"},{"unit":"kW","name":"总有用功率","datapoint":100207,"value":"0.0036"},{"unit":"kW","name":"A相有用功率","datapoint":100208,"value":"0.0008"},{"unit":"kW","name":"B相有用功率","datapoint":100209,"value":"0.0016"},{"unit":"kW","name":"C相有用功率","datapoint":100210,"value":"0.0012"},{"unit":"kW","name":"总无用功率","datapoint":100211,"value":"-0.0044"},{"unit":"kW","name":"A相无用功率","datapoint":100212,"value":"-0.0014"},{"unit":"kW","name":"B相无用功率","datapoint":100213,"value":"-0.0014"},{"unit":"kW","name":"C相无用功率","datapoint":100214,"value":"-0.0015"},{"unit":"","name":"总功率因素","datapoint":100215,"value":"0.639"},{"unit":"","name":"A相功率因素","datapoint":100216,"value":"0.517"},{"unit":"","name":"B相功率因素","datapoint":100217,"value":"0.749"},{"unit":"","name":"C相功率因素","datapoint":100218,"value":"0.602"},{"unit":"度","name":"总用电量","datapoint":100219,"value":"34.99"},{"unit":"度","name":"尖用电量","datapoint":100220,"value":"0.00"},{"unit":"度","name":"峰用电量","datapoint":100221,"value":"11.33"},{"unit":"度","name":"平用电量","datapoint":100222,"value":"11.49"},{"unit":"度","name":"谷用电量","datapoint":100223,"value":"12.17"},{"unit":"度","name":"电表温度","datapoint":100224,"value":"16.50"}]
     */

    public int code;
    public String message;
    public List<InfoBean> info;

    public static class InfoBean {
        /**
         * unit : V
         * name : A相电压
         * datapoint : 100201
         * value : 228.1
         */

        public String unit;
        public String name;
        public String datapoint;
        public String value;
    }
}
