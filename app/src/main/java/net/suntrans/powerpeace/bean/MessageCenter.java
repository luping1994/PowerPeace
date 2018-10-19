package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2018/10/17.
 * Des:
 */
public class MessageCenter extends ResultBody<MessageCenter.Info> {


    public static class Info{
        public List<Msg> lists;
    }

    public static class Msg{

        public int id;
        public int sys_id;
        public int user_id;
        public String username;
        public String time_start;
        public String time_stop;
        public String created_at;
        public String message;
    }


}
