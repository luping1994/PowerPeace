package net.suntrans.powerpeace.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.security.PublicKey;

/**
 * Created by Looney on 2017/9/5.
 */

public class RoomInfoSelection extends SectionEntity {
    public static final String TYPE_ACCOUNT = "account";
    public static final String TYPE_STATUS_DES = "status_des";
    public static final String TYPE_DEV_CHANNEL= "dev_channel";
    public static final String TYPE_METER_INFO= "meter_info";
    public static final String TYPE_ROOM_STU= "room_stu";
    public static final String TYPE_WHOLE_NAME= "whole_name";
    public static final String TYPE_CHANNEL_LOG= "channel_log";

    public String type;
    public String value;
    public boolean status;
    public int imgResId=-1;
    public String  id;
    public String  num;
    public String  studentID;
    public String unit="";
    public String name;
    public String data_type;
    public String addr;
    public RoomInfoSelection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public RoomInfoSelection(Object o) {
        super(o);
    }


}
