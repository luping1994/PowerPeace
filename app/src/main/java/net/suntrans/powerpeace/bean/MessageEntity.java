package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/10/26.
 * Des:
 */

public class MessageEntity {


    /**
     * code : 1
     * message : 查询成功
     * info : [{"id":1,"title":"致2017届新生的一封信","des":"致2017届新生的一封信","time":"2017-10-25 20:48:29","url":"http://gszy.suntrans-cloud.com/message/1"}]
     */

    public int code;
    public String message;
    public List<Msg> info;

    public static class Msg {
        /**
         * id : 1
         * title : 致2017届新生的一封信
         * des : 致2017届新生的一封信
         * time : 2017-10-25 20:48:29
         * url : http://gszy.suntrans-cloud.com/message/1
         */

        public int id;
        public String title;
        public String des;
        public String time;
        public String url;
    }
}
