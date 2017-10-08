package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/10/6.
 */

public class BuildingResult extends ResultBody<List<BuildingResult.Building>>{

    /**
     * code : 1
     * message : 查询成功
     * info : [{"id":3,"title":"一号学生公寓","alpha":1,"num":471},{"id":4,"title":"二号学生公寓","alpha":2,"num":460},{"id":5,"title":"三号学生公寓","alpha":3,"num":477},{"id":6,"title":"四号学生公寓","alpha":4,"num":477},{"id":7,"title":"五号学生公寓","alpha":5,"num":477}]
     */
    public static class  Building {
        /**
         * id : 3
         * title : 一号学生公寓
         * alpha : 1
         * num : 471
         */
        public String id;
        public String title;
        public String alpha;
        public String num;

    }
}
