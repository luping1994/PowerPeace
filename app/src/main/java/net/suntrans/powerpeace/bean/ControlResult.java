package net.suntrans.powerpeace.bean;

/**
 * Created by Looney on 2017/9/22.
 */

public class ControlResult {

    /**
     * code : 200
     * result : {"addr":"00000b9e","num":2,"status":2}
     */

    public int code;
    public Result result;

    public static class Result {
        /**
         * addr : 00000b9e
         * num : 2
         * status : 2
         */

        public String addr;
        public String num;
        public String status;
    }
}
