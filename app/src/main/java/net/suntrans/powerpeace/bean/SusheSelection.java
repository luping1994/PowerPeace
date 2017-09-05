package net.suntrans.powerpeace.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Looney on 2017/9/1.
 */

public class SusheSelection extends SectionEntity<SusheEntity.SusheInfo> {

    public String susheName;
    public String room_id;
    public String wholeName;
    public SusheSelection(boolean isHeader, String header) {
        super(isHeader, header);
    }
}
