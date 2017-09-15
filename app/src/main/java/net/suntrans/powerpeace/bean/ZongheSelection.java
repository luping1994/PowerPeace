package net.suntrans.powerpeace.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Looney on 2017/9/1.
 */

public class ZongheSelection extends SectionEntity<SusheEntity.SusheInfo> {

    public static final String TYPE_THERE_METER_INFO = "THERE_METER_INFO";


    public String name;
    public String value;
    public String type;

    public ZongheSelection(boolean isHeader, String header) {
        super(isHeader, header);
    }
}
