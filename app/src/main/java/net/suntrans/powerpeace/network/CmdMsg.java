package net.suntrans.powerpeace.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2017/6/16.
 */

public class CmdMsg implements Parcelable {

    public String msg;
    public int status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msg);
        dest.writeInt(this.status);
    }

    public CmdMsg() {
    }

    protected CmdMsg(Parcel in) {
        this.msg = in.readString();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<CmdMsg> CREATOR = new Parcelable.Creator<CmdMsg>() {
        @Override
        public CmdMsg createFromParcel(Parcel source) {
            return new CmdMsg(source);
        }

        @Override
        public CmdMsg[] newArray(int size) {
            return new CmdMsg[size];
        }
    };
}
