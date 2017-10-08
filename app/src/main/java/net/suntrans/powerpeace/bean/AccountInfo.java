package net.suntrans.powerpeace.bean;

/**
 * Created by Looney on 2017/10/6.
 */

public class AccountInfo {


    /**
     * totaluse : 0
     * monthuse : 0
     * balans : 0.00
     * status : 正常
     * dayuse : 0
     */

    private String totaluse;
    private String monthuse;
    private String balans;
    private String status;
    private String dayuse;

    public String getTotaluse() {
        return totaluse;
    }

    public void setTotaluse(String totaluse) {
        this.totaluse = totaluse;
    }

    public String getMonthuse() {
        return monthuse;
    }

    public void setMonthuse(String monthuse) {
        this.monthuse = monthuse;
    }

    public String getBalans() {
        return balans;
    }

    public void setBalans(String balans) {
        this.balans = balans;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDayuse() {
        return dayuse;
    }

    public void setDayuse(String dayuse) {
        this.dayuse = dayuse;
    }
}
