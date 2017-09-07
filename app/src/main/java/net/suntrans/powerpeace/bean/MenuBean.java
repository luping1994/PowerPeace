package net.suntrans.powerpeace.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2017/8/31.
 */

public class MenuBean extends ResultBody<List<MenuBean.InfoBean>>{

    /**
     * code : 1
     * message : Inquiry_Department_Info Success
     * info : [{"departmentName":"工学部","sublist":[{"sublist":[{"floor":2},{"floor":1}],"building":1}],"departmentID":1},{"departmentName":"文理学部","sublist":[{"sublist":[{"floor":2}],"building":2},{"sublist":[{"floor":1}],"building":1}],"departmentID":2},{"departmentName":"文理学部","sublist":[{"sublist":[{"floor":0}],"building":2},{"sublist":[{"floor":0}],"building":3},{"sublist":[{"floor":0}],"building":4}],"departmentID":0}]
     */

//    public int code;
//    public String message;
//    public List<InfoBean> info;

    public static class InfoBean {
        /**
         * departmentName : 工学部
         * sublist : [{"sublist":[{"floor":2},{"floor":1}],"building":1}]
         * departmentID : 1
         */

        public String departmentName;
        public int departmentID;
        public List<SublistBeanX> sublist;

        public static class SublistBeanX {
            /**
             * sublist : [{"floor":2},{"floor":1}]
             * building : 1
             */
            public int building;
            public String building_name;

            @SerializedName("sublist")
            public List<SublistBean> floors;

            public static class SublistBean {
                /**
                 * floor : 2
                 */

                public int floor;
                public String floor_name;
            }
        }
    }
}
