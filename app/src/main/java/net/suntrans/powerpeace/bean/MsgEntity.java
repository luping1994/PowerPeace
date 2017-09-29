package net.suntrans.powerpeace.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2017/9/29.
 */

public class MsgEntity extends ResultBody<List<MsgEntity.MessageItem>> {



    public static class MessageItem {
        /**
         * classID : 6
         * name : http://g.suntrans.net:8088/SuntransTest-Peace/message/letter.html
         * created_at : 2017-10-01
         * type : 700
         * class_name : 致2017年新生的一封信
         */

        public String classID;
        public String name;
        public String created_at;
        public String type;
        public String class_name;
    }
}
