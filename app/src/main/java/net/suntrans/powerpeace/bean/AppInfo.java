package net.suntrans.powerpeace.bean;

/**
 * Created by Looney on 2017/10/30.
 * Des:
 */

public class AppInfo {


    /**
     * code : 0
     * message :
     * data : {"buildBuildVersion":"21","downloadURL":"https://www.pgyer.com/app/installUpdate/6a335861ce14ee127245f3c591b8e03d?sig=Ds5X%2BwJuq2gDSMmPK0MxM3eRn9X4sySSFZkFWqq%2BbTXvgIcHn25j0rpdLXPwbpKW","buildVersionNo":"23","buildVersion":"0.4.0","buildShortcutUrl":"https://www.pgyer.com/4qzv","buildUpdateDescription":"update"}
     */

    public int code;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * buildBuildVersion : 21
         * downloadURL : https://www.pgyer.com/app/installUpdate/6a335861ce14ee127245f3c591b8e03d?sig=Ds5X%2BwJuq2gDSMmPK0MxM3eRn9X4sySSFZkFWqq%2BbTXvgIcHn25j0rpdLXPwbpKW
         * buildVersionNo : 23
         * buildVersion : 0.4.0
         * buildShortcutUrl : https://www.pgyer.com/4qzv
         * buildUpdateDescription : update
         */

        public String buildBuildVersion;
        public String downloadURL;
        public String buildVersionNo;
        public String buildVersion;
        public String buildShortcutUrl;
        public String buildUpdateDescription;
    }
}
