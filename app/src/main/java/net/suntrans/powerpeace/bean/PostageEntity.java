package net.suntrans.powerpeace.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/9/5.
 */

public class PostageEntity {
    public int code;
    public String msg;
    public List<Map<String, String>> lists;


    public static class PostageInfo {
        public String msg;//信息 充值记录的msg示例：xxx充值30.00元 用电记录的示例:
        public String created_at;//时间
        public String money;
        public String type;//type为1代表用电记录为2代表充值记录
    }

    /**
     *
     * {
     *     "code":200;
     *     "msg":"请求成功!"
     *     "lists":[
     *        {
     *          "msg":"王铁锤充值30.00元",
     *          "created_at":"2017-09-04",
     *          "type":"2"
     *          "money":30.00
     *        },
     *        {
     *           "msg":"宿舍用电2.0度",
     *          "created_at":"2017-09-04",
     *          "type":"1"
     *          "money":3.00
     *        }
     *
     *     ]
     * }
     *
     */
}
