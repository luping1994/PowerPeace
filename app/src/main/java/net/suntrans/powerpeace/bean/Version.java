package net.suntrans.powerpeace.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2017/9/28.
 */

public class Version {

    /**
     * code : 0
     * message :
     * data : {"lastBuild":"9","downloadURL":"https://www.pgyer.com/app/installUpdate/26f8fb29460e2dd7ecbcb18c252a7857?sig=uMZ9fXGZT3F%2BjS8QPRS%2BNvGKg8Jgr4BSGY5khQRw2269AQAtPVtHZu6zdsmP0WzJ","versionCode":"6","versionName":"0.1.7","appUrl":"https://www.pgyer.com/4qzv","build":"9","releaseNote":"日常更新"}
     */

    public int code;
    public String message;
    public VersionInfo data;

    public static class VersionInfo implements Parcelable {
        /**
         * lastBuild : 9
         * downloadURL : https://www.pgyer.com/app/installUpdate/26f8fb29460e2dd7ecbcb18c252a7857?sig=uMZ9fXGZT3F%2BjS8QPRS%2BNvGKg8Jgr4BSGY5khQRw2269AQAtPVtHZu6zdsmP0WzJ
         * versionCode : 6
         * versionName : 0.1.7
         * appUrl : https://www.pgyer.com/4qzv
         * build : 9
         * releaseNote : 日常更新
         */

        public String lastBuild;
        public String downloadURL;
        public String versionCode;
        public String versionName;
        public String appUrl;
        public String build;
        public String releaseNote;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.lastBuild);
            dest.writeString(this.downloadURL);
            dest.writeString(this.versionCode);
            dest.writeString(this.versionName);
            dest.writeString(this.appUrl);
            dest.writeString(this.build);
            dest.writeString(this.releaseNote);
        }

        public VersionInfo() {
        }

        protected VersionInfo(Parcel in) {
            this.lastBuild = in.readString();
            this.downloadURL = in.readString();
            this.versionCode = in.readString();
            this.versionName = in.readString();
            this.appUrl = in.readString();
            this.build = in.readString();
            this.releaseNote = in.readString();
        }

        public static final Parcelable.Creator<VersionInfo> CREATOR = new Parcelable.Creator<VersionInfo>() {
            @Override
            public VersionInfo createFromParcel(Parcel source) {
                return new VersionInfo(source);
            }

            @Override
            public VersionInfo[] newArray(int size) {
                return new VersionInfo[size];
            }
        };
    }
}
