package net.suntrans.powerpeace.adapter;

/**
 * Created by Looney on 2017/9/13.
 */

public class NavViewItem {
    private int imageResID;
    private String name;

    public NavViewItem(int imageResID, String name) {
        this.imageResID = imageResID;
        this.name = name;
    }

    public int getImageResID() {
        return imageResID;
    }

    public void setImageResID(int imageResID) {
        this.imageResID = imageResID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
