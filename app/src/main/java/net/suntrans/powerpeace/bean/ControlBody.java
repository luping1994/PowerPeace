package net.suntrans.powerpeace.bean;

/**
 * Created by Looney on 2017/9/22.
 */

public class ControlBody {

    /**
     * device : 4100
     * action : switch
     * user_id : 123
     * web_type : 701
     * room_id : 12
     * channel_id : 2
     * command : 1
     */
    public String dev;
    public String ac;
    public int ud;
    public int wp;
//    public int rd;
    public int chd;
    public int cmd;
    public int num;
    public int rd;
    public String addr;
    public String mui;

    @Override
    public String toString() {
        return "ControlBody{" +
                "dev='" + dev + '\'' +
                ", ac='" + ac + '\'' +
                ", ud=" + ud +
                ", wp=" + wp +
                ", chd=" + chd +
                ", cmd=" + cmd +
                ", num=" + num +
                ", rd=" + rd +
                ", addr='" + addr + '\'' +
                ", mui='" + mui + '\'' +
                '}';
    }
}
