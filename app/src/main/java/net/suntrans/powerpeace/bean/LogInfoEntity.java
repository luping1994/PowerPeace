package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/18.
 */

public class LogInfoEntity extends ResultBody<List<LogInfoEntity.LogInfo>>{



    public static class LogInfo {
        /**
         * user_id : 0
         * port : 31
         * name : null
         * created_at : 2017-08-22 16:31:11
         * message : 关闭了总开关
         * status : false
         */

        public int user_id;
        public int port;
        public Object name;
        public String created_at;
        public String message;
        public boolean status;
        public String type="1";
    }
}
