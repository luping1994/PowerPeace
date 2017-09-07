package net.suntrans.powerpeace.bean;

/**
 * Created by Looney on 2017/9/7.
 */

public class ResultBody<T> {
    public int code;
    public String message;
    public T info;

    public boolean isOk() {
        return code==1;
    }
}
