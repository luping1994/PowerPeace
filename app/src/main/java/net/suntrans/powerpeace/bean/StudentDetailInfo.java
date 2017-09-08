package net.suntrans.powerpeace.bean;

import java.util.List;

/**
 * Created by Looney on 2017/9/8.
 */

public class StudentDetailInfo {

    public long studentID;
    public String major;
    public int departmentID;
    public int dormitory;
    public long telephone;
    public String academy;
    public int building;

    /**
     *  “info”:{
                 "studentID": 2013301470027,
                 "major": "11",
                 "departmentID": 1,
                 "dormitory": 507,
                 "telephone": 13247128702,
                 "academy": "动力与机械学院",
                 "building": 1,
                 "sheyou":[
                            {
                                "studentID": 2013301470027,
                               "telephone": 13247128702,
                                "name":"张三"
                            }
                         ]
             }

     */

}
