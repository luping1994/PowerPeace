package net.suntrans.powerpeace.bean;

/**
 * Created by Looney on 2017/8/31.
 */

public class LoginEntity extends ResultBody<LoginEntity.InfoBean>{

    /**
     * code : 1
     * message : login success
     * info : {"role":0,"sex":0,"tel_num":"13247167990","avatar":"/images/defaultUserAvatar.jpg","creationDate":1500800741243,"userId":"a4362e8beed54112b96f282c8aa63033","email":null,"username":"suntrans","status":1}
     * token : ace3a291dc5b40d6ace30b0d61860055Mh0IH7
     */

    public String token;

    public static class InfoBean {
        /**
         * role : 0
         * sex : 0
         * tel_num : 13247167990
         * avatar : /images/defaultUserAvatar.jpg
         * creationDate : 1500800741243
         * userId : a4362e8beed54112b96f282c8aa63033
         * email : null
         * username : suntrans
         * status : 1
         */

        public int role;
        public int sex;
        public String tel_num;
        public String avatar;
        public long creationDate;
        public String userId;
        public Object email;
        public String username;
        public int status;
    }
}
